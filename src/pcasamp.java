
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.CRC32;

import javax.swing.SwingUtilities;

import org.jfree.*;

public class pcasamp {
	private static final String InputFilePath = "C:\\Users\\YO BABY\\Desktop\\ITM\\FlowTraffic.txt";
	private static final int MaxBitArraySize = 35; //36 - 10%
	private static int B[] = new int[MaxBitArraySize];
	private static HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();

	private void resetBitArray() {
		int i = 0;
		for (i = 0; i < MaxBitArraySize; i++) {
			B[i] = 0;
		}
	}

	private int countZeroes() {
		int i = 0, count = 0;
		for (i = 0; i < MaxBitArraySize; i++) {
			if (B[i] == 0) {
				count++;
			}
		}
		return count;

	}

	private void setBitArray(String element) {
		long crc32 = 0;
		int bitB = 0;
		CRC32 crc = new CRC32();

		crc.update(element.getBytes());

		crc32 = crc.getValue();

		bitB = (int) (crc32 % MaxBitArraySize);

		B[Math.abs(bitB)] = 1;

	}

	public static void main(String[] args) throws IOException {

		Map<String, List<String>> inputMap = new HashMap<String, List<String>>();

		int i = 0, currentFlowSize, countZeroesVs = 0, nEstimate = 0;
		double Vs = 0;
		String currentFlowId = "";
		List<String> currentFlow;
		pcasamp probCounting = new pcasamp();
		double samplerate = 0.1;
		int noFlows = 0;
		double errorRate = 0.0;

		try {
			inputMap = InputParser.parseInputData(InputFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Processing for all flows
		for (Entry<String, List<String>> ee : inputMap.entrySet()) {
			currentFlowId = ee.getKey();
			currentFlow = ee.getValue();
			currentFlowSize = currentFlow.size();
			probCounting.resetBitArray();

			if (currentFlowSize <= 800) {
				// Processing for each flow
				i = 0;
				
				int count = 0;
				int flowCount = 0;
				while (i < currentFlowSize) {
					count++;
					if ((count % ((samplerate * 10) + 1)) == 0) {
						count = 0;
						i += (int) (10 - samplerate * 10);
					} else {
						flowCount++;
						probCounting.setBitArray(currentFlow.get(i));
					}

					i++;

				}
				if (currentFlowSize > 100) {
					//System.out.println("current flowsize : " + currentFlowSize + " count :" + flowCount);
				}
				countZeroesVs = probCounting.countZeroes();
				Vs = (double) countZeroesVs / MaxBitArraySize;

				// System.out.println("CountZeroes : " + countZeroesVs + "Vs : "
				// +
				// Vs);

				nEstimate = (int) (((-1) * MaxBitArraySize * java.lang.Math.log(Vs)) / (double) samplerate);

			//	nEstimate = (int) ( (-1) * MaxBitArraySize * (java.lang.Math.log(Vs / (double) samplerate)));

				
				// nEstimate = (int) (((-1) * MaxBitArraySize *
				// java.lang.Math.log(Vs)));

				// System.out.println("FlowId : " + currentFlowId + " n = " +
				// currentFlowSize + " n^ = " + nEstimate);

				result.put(currentFlowSize, nEstimate);
				errorRate += Math.abs( (nEstimate - currentFlowSize));
				noFlows++;
			}
			
			
		}
		double averrate=errorRate /(double) noFlows;
		averrate = Math.round(averrate * 100)/100D;
		System.out.println("Average error rate : " + averrate +"%");
		
		final XYLineChart graphPlotter = new XYLineChart("Flow Cardinality", "Bitmap size m =" + MaxBitArraySize + ", Sample rate p = "+ samplerate +", Error % = "+ averrate +"%",result );
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				graphPlotter.setVisible(true);
			}
		});

	}

}

