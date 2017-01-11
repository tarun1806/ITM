import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.CRC32;

import org.jfree.ui.RefineryUtilities;

public class HyperLogLog {
	
	final static double b = 4;
	static int  b_square = (int)Math.pow(2, b);  //16
	static Integer[] M = new Integer[b_square];
	static int arraySize = 20;
	static Integer[] BIT_ARRAY = new Integer[arraySize];
	static int m = 200000;
	static double alpha = 0.673;
	//static double alpha = 0.718;
	private static final String InputFilePath = "D:\\FlowTraffic.txt";
	//private static final String InputFilePath = "D:\\test.txt";
	
	private static HashMap<Integer,Integer> resultGraph = new HashMap<Integer,Integer>();

	
	private void initM() {
		for (int i = 0; i < b_square; i++)
				M[i] = 0;
	}
	
	private int hashFunc(String it) {

		long crc32 = 0;

		CRC32 crc = new CRC32();
		crc.update(it.getBytes());

		crc32 = crc.getValue();
		//System.out.println("I wanted to know this : " +(crc32 % m));
		return (int) (crc32 % m);
	}
	
	private Integer[] decToBinary(int numericalValue){
		Integer[] bitarrayrev = new Integer[arraySize];
    	Integer[] bitarray = new Integer[arraySize];
    	
    	for (int i = 0; i <arraySize ; i++) { 
    		   bitarrayrev[i] = numericalValue & 0x1;
    		   numericalValue = numericalValue >> 1;
    		}
    	for (int i = 0; i <arraySize; i++){
    		bitarray[i] = bitarrayrev[(arraySize-1)-i];
    	}
    	return bitarray;
	} 
	
	private int firstFourDigits(Integer[] bitarray){

    	int numericalValue = 0;
    	int lastPos = arraySize-1; //15
    	for (int i = lastPos; i > (lastPos-b) ; i--) { 
    		   if(bitarray[i] == 1){
    			   numericalValue = numericalValue + (int)Math.pow(2, (lastPos-i));
    		   }
    		}
    	return numericalValue;
	}
	
	private Integer[] bitArrayMinusFour(Integer[] array_16){
		Integer[] newArray = new Integer[(arraySize-(int)b)];
		
		for(int i=0; i<(arraySize-b); i++){
			newArray[i] = array_16[i];
		}
		return newArray;
	}
	
	private int phoFinder(Integer[] newArray){
		int pho = 0;
		int lastPos = arraySize -(int)(b+1); //11
		for(int i = lastPos; i>= 0 ; i--){
			if(newArray[i]== 0){
				pho = pho+1;
			}
			else{
				break;
			}
		}
		
		//System.out.println(pho);
		return pho+1;
	}
	private int estimator(){
		double z = 0;
		double n_estimate;
		for(int i = 0; i<b_square; i++){
			z = z+ Math.pow(0.5, M[i]); 
		} 
		double m_square = Math.pow(b_square, 2);
		n_estimate = (m_square*alpha)/z;
		//System.out.println("z: "+z+" n^ :"+n_estimate);
		return (int)n_estimate;
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		HyperLogLog HLL = new HyperLogLog();
		Map<String, List<String>> inputMap = new HashMap<String, List<String>>();
		
		
		List<String> currentFlow;
		int currentFlowSize = 0;
		
		try {
			inputMap = InputParser.parseInputData(InputFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for(Entry<String, List<String>> ee: inputMap.entrySet()){
			
			currentFlow = ee.getValue();
			currentFlowSize = currentFlow.size();
			HLL.initM();
			
			if(currentFlowSize<2000){
			//System.out.println("currentFlowSize: "+ currentFlowSize);  //
			for (String it : currentFlow) {
				int x = HLL.hashFunc(it);   					//retrieves a random number
				BIT_ARRAY = HLL.decToBinary(x); 				//converts the random number to a binary array
				int j = HLL.firstFourDigits(BIT_ARRAY);  	//gets decimal value of the rightmost 4 bits
				Integer[] w = HLL.bitArrayMinusFour(BIT_ARRAY); //rest of the array
				int rho = HLL.phoFinder(w);						//finds the run of zeros from right for the new array
				//System.out.println("rho: "+rho);//

				M[j] = Math.max(M[j], rho);
				//System.out.println("M:"+M[j]);
				//System.out.println("j: "+j);
				//System.out.println("M: "+M[j]);
			}
			
			int n_estimate = HLL.estimator();
			
			System.out.println("n: "+currentFlowSize+" n^ "+ n_estimate);
			
			resultGraph.put(currentFlowSize, n_estimate);
			
			}
		
		}
		XYLineChart chart = new XYLineChart("Flow Cardinality", "Orginal vs Estimator", resultGraph);
	      chart.pack( );          
	      RefineryUtilities.centerFrameOnScreen( chart );          
	      chart.setVisible( true );
	}

}
