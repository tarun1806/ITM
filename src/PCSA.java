
import java.io.FileNotFoundException;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.CRC32;

import org.jfree.ui.RefineryUtilities;

public class PCSA {
	final static int m = 150;
	static Integer[][] bitSketch = new Integer[m][32];
	static final double phi = 0.77351;
	static double sum = 0;
	static double result = 0;
	static int flag = 0;
	private static final String InputFilePath = "C:\\Users\\YO BABY\\Desktop\\ITM\\FlowTraffic.txt";
	private static HashMap<Integer,Integer> resultGraph = new HashMap<Integer,Integer>();
	
	
	private int offline() {
		int count_off = 0;
		for (int i = 0; i < m; i++) {

			for (int j = bitSketch[i].length - 1; j > 0; j--) {
				if (bitSketch[i][j] != 0)
					count_off++;
				else {
					break;
				}
			}
			sum += count_off;
			count_off = 0;
		}
		double sumM = sum / m;
		result = m * (Math.pow(2, sumM)) / phi;
		int resultInt = (int) (result);
		
		return resultInt;

	}
	
	private  void online(List<String> destAddress) throws UnknownHostException {

		flag = 0;
		
		for (String it : destAddress) {
			int rowIndex = rowHash(it);
			int colIndex = count_seq_zeroes(it);
			colIndex = 31 - colIndex;
			bitSketch[rowIndex][colIndex] = 1;

		}
		
	}

	private void initBitSketch() {
		for (int i = 0; i < m; i++)
			for (int j = 0; j < 32; j++)
				bitSketch[i][j] = 0;

		sum = 0;
		result = 0;

	}
	
	private static int rowHash(String it) {

		long crc32 = 0;

		CRC32 crc = new CRC32();
		crc.update(it.getBytes());

		crc32 = crc.getValue();

		return (int) (crc32 % m);
	}

	private static int count_seq_zeroes(String address) throws UnknownHostException {

		int zeroCount = 0, check = 1;
		Boolean foundOne = false;

		while (!foundOne) {
			if ((address.hashCode() & check) == 0) {
				check = check << 1;
				zeroCount++;
			} else
				foundOne = true;
		}
		return zeroCount;
	}
	

	public static void main(String[] args) throws IOException {
		PCSA pcsaAlgo = new PCSA();
		Map<String, List<String>> inputMap = new HashMap<String, List<String>>();
		List<String> currentFlow;
		int i = 0, currentFlowSize = 0, res = 0;
		
		try {
			inputMap = InputParser.parseInputData(InputFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
		
		// Processing for all flows
		for (Entry<String, List<String>> ee : inputMap.entrySet()) {
			currentFlow = ee.getValue();
			currentFlowSize = currentFlow.size();

			// Processing for each flow
			i = 0;
			pcsaAlgo.initBitSketch();
			while (i < currentFlowSize) {
				try {
					pcsaAlgo.online(currentFlow);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				i++;
			}
			
			res = pcsaAlgo.offline();
			//System.out.println(currentFlowSize + " " + res);
			if (currentFlowSize>500 ) resultGraph.put(currentFlowSize, res);

	}
		 for(Map.Entry<Integer, Integer> entry : resultGraph.entrySet()){  // for graph
				int key = entry.getKey();
				int value = entry.getValue();
				//System.out.println("n: "+key+" n^: "+value);
	      }
		XYLineChart chart = new XYLineChart("Flow Cardinality", "Orginal vs Estimator", resultGraph);
	      chart.pack( );          
	      RefineryUtilities.centerFrameOnScreen( chart );          
	      chart.setVisible( true );
}
	
}
