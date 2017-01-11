import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import org.jfree.ui.RefineryUtilities;

public class HyperLL {
	
	final static double b = 5;
	static int  b_square = (int)Math.pow(2, b);  //16
	static Integer[] M = new Integer[b_square];
	static int arraySize = 32;
	static Integer[] BIT_ARRAY = new Integer[arraySize];
	static int m = 2000000;
	static double alpha = 0.693;
	private static final String InputFilePath = "D:\\FlowTraffic.txt";
	//private static final String InputFilePath = "D:\\test.txt";
	
	private static HashMap<Integer,Integer> resultGraph = new HashMap<Integer,Integer>();

	
	private void initM() {
		for (int i = 0; i < b_square; i++)
				M[i] = 0;
	}
	

	
	private static Integer[] decToBinary(int numericalValue){
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
	
	private int firstFourDigits(String it){
	
		int a = it.hashCode() & 0x1F;
		//System.out.println(it.hashCode());
    	return a; 
	}
	
	private int bitArrayMinusFour(String it){
		int a = it.hashCode() >> (int)b;
    	
    	Integer[] newArray = HyperLL.decToBinary(a);
		
		int x = HyperLL.phoFinder(newArray);
		return x+1;
	}
	
	private static int phoFinder(Integer[] newArray){
		int pho = 0;
		int lastPos = arraySize - 1; //11
		for(int i = lastPos; i>= 0 ; i--){
			if(newArray[i]== 0){
				pho = pho+1;
			}
			else{
				break;
			}
		}
		return pho;
	}
	
	
	private int estimator(){
		double z = 0;
		double n_estimate;
		for(int i = 0; i<b_square; i++){
			z = z+ Math.pow(0.5, M[i]); 
		} 
		double m_square = Math.pow(b_square, 2);
		n_estimate = (m_square*alpha)/z;
		
		int V=0;
		
		
		if (n_estimate < (5/2)*b_square){
			for(int i = 0; i < b_square; i++) {
				if(M[i] == 0) {
					V++;
				}
			}
			if(V!=0){
			n_estimate = b_square*Math.log(b_square/V);	
			}
		}
		else if(n_estimate > (1/30)*Math.pow(2, 32)){
			double temp = n_estimate/Math.pow(2, 32);
			n_estimate = - Math.pow(2, 32)*Math.log(1-temp);
		}
			
		return (int)n_estimate;
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		HyperLL HLL = new HyperLL();
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
			
			if(currentFlowSize<4000){
			//System.out.println("currentFlowSize: "+ currentFlowSize);  //
			for (String it : currentFlow) {
				
				
				int j = HLL.firstFourDigits(it);  	//gets decimal value of the rightmost 4 bits
				int rho = HLL.bitArrayMinusFour(it); //rest of the array
				

				M[j] = Math.max(M[j], rho);
				
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
