import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowReader {

	public static Map<String, List<String>> parseInputData(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		List<String> destinationList = new ArrayList<String>();
		String src = null;
		Map<String, List<String>> op = new HashMap<String, List<String>>();

		try {

			String line = br.readLine();

			line = br.readLine();

			while (line != null) {
				String[] result = line.split(" ");
				List<String> tempList = new ArrayList<String>();
				for (int x = 0; x < result.length; x++) {
					if (!result[x].equals("")) {
						tempList.add(result[x]);
					}
				}

				if (src == null)
					src = tempList.get(0);

				if (src.equals(tempList.get(0))) {
					destinationList.add(tempList.get(1));
				} else {
					
					List<String> tempL = new ArrayList<String>(destinationList);

					op.put(src, tempL);
					
					src = tempList.get(0);

					destinationList.clear();
					destinationList.add(tempList.get(1));

				}

				line = br.readLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		br.close();
		return op;
	}
}
