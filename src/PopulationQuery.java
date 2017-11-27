

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PopulationQuery {
	// next four constants are relevant to parsing
	public static final int TOKENS_PER_LINE  = 7;
	public static final int POPULATION_INDEX = 4; // zero-based indices
	public static final int LATITUDE_INDEX   = 5;
	public static final int LONGITUDE_INDEX  = 6;

	// argument 1: file name for input data: pass this to parse
	// argument 2: number of x-dimension buckets
	// argument 3: number of y-dimension buckets
	// argument 4: -v1, -v2, -v3, -v4, or -v5
	public static void main(String[] args) {
		// FOR YOU
		String fileName = args[0];
		CensusData censusData = PopulationQuery.parse(fileName);
		int x = Integer.valueOf(args[1]);
		int y = Integer.valueOf(args[2]);

		String version = args[3];

		switch(version){
		case "-v1":
			V1SimpleAndSequential v1 = new V1SimpleAndSequential(x, y, censusData);
			v1.findUSRectangle();
			int returnType1 = QueryAlgorithm.DONE;
			do {
				returnType1 = v1.takeInput();
				if(returnType1 ==  QueryAlgorithm.DONE) {
					v1.findPopulation();
				}
			}while(returnType1 != QueryAlgorithm.EXIT);

			break;
		case "-v2":
			V2SimpleAndParallel v2 = new V2SimpleAndParallel(x, y, censusData);
			v2.findUSRectangle();
			int returnType2 = QueryAlgorithm.DONE;
			do {
				returnType2 = v2.takeInput();
				if(returnType2 ==  QueryAlgorithm.DONE) {
					v2.findPopulation();
				}
			}while(returnType2 != QueryAlgorithm.EXIT);
			break;
		case "-v3":
			V3SmarterAndSequential v3 = new V3SmarterAndSequential(x, y, censusData);
			v3.findUSRectangle();
			int returnType3 = QueryAlgorithm.DONE;
			do {
				returnType3 = v3.takeInput();
				if(returnType3 ==  QueryAlgorithm.DONE) {
					v3.findPopulation();
				}
			}while(returnType3 != QueryAlgorithm.EXIT);
			break;
		case "-v4":
			V4SmarterAndParallel v4 = new V4SmarterAndParallel(x, y, censusData);
			v4.findUSRectangle();
			int returnType4 = QueryAlgorithm.DONE;
			do {
				returnType4 = v4.takeInput();
				if(returnType4 ==  QueryAlgorithm.DONE) {
					v4.findPopulation();
				}
			}while(returnType4 != QueryAlgorithm.EXIT);
			break;
		case "-v5":
			V5SmarterAndLockedBased v5 = new V5SmarterAndLockedBased(x, y, censusData);
			v5.findUSRectangle();
			int returnType5 = QueryAlgorithm.DONE;
			do {
				returnType5 = v5.takeInput();
				if(returnType5 ==  QueryAlgorithm.DONE) {
					v5.findPopulation();
				}
			}while(returnType5 != QueryAlgorithm.EXIT);
			break;

		}

	}
	
	// parse the input file into a large array held in a CensusData object
		public static CensusData parse(String filename) {
			CensusData result = new CensusData();

			try {
				BufferedReader fileIn = new BufferedReader(new FileReader(filename));

				// Skip the first line of the file
				// After that each line has 7 comma-separated numbers (see constants above)
				// We want to skip the first 4, the 5th is the population (an int)
				// and the 6th and 7th are latitude and longitude (floats)
				// If the population is 0, then the line has latitude and longitude of +.,-.
				// which cannot be parsed as floats, so that's a special case
				//   (we could fix this, but noisy data is a fact of life, more fun
				//    to process the real data as provided by the government)
				String oneLine = fileIn.readLine(); // skip the first line

				// read each subsequent line and add relevant data to a big array
				while ((oneLine = fileIn.readLine()) != null) {
					String[] tokens = oneLine.split(",");
					if(tokens.length != TOKENS_PER_LINE)
						throw new NumberFormatException();
					int population = Integer.parseInt(tokens[POPULATION_INDEX]);
					if(population != 0)
						result.add(population,
								Float.parseFloat(tokens[LATITUDE_INDEX]),
								Float.parseFloat(tokens[LONGITUDE_INDEX]));
				}

				fileIn.close();
			} catch(IOException ioe) {
				System.err.println(ioe.getMessage());
				System.err.println("Error opening/reading/writing input or output file.");
				System.exit(1);
			} catch(NumberFormatException nfe) {
				System.err.println(nfe.toString());
				System.err.println("Error in file format");
				System.exit(1);
			}
			return result;
		}

}
