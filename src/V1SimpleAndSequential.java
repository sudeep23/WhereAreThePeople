import java.util.InputMismatchException;
import java.util.Scanner;

public class V1SimpleAndSequential {
	
	public static int INVALID_ENTRY = 0;
	public static int EXIT = 1;
	public static int DONE = 2;
	
	float westBoundary;
	float southBoundary;
	float eastBoundary;
	float northBoundary;
	
	int x;
	int y;
	private static Scanner scanner;
	
	Rectangle usRectangle;
	Rectangle inputRecBoundary;
	public V1SimpleAndSequential(int x, int y) {
		this.x = x;
		this.y = y;
	}

	int takeInput() {
			System.out.println("Please give west, south, east, north coordinates of your query rectangle: ");
			scanner = new Scanner(System. in);

			try {
				westBoundary = scanner.nextInt();
				if(westBoundary < 1 || westBoundary > x) {
					System.out.println("Invalid Western Boundary");
					return INVALID_ENTRY;
				}

				southBoundary = scanner.nextInt();
				if(southBoundary < 1 || southBoundary > y) {
					System.out.println("Invalid Southern Boundary");
					return INVALID_ENTRY;
				}

				eastBoundary = scanner.nextInt();
				if(eastBoundary < westBoundary || eastBoundary > x) {
					System.out.println("Invalid Eastern Boundary");
					return INVALID_ENTRY;
				}

				northBoundary = scanner.nextInt();
				if(northBoundary < southBoundary || northBoundary > y) {
					System.out.println("Invalid Northern Boundary");
					return INVALID_ENTRY;
				}

				inputRecBoundary = new Rectangle(westBoundary, eastBoundary, northBoundary, southBoundary);
			} catch (InputMismatchException e) {
				return EXIT;
			}
			return DONE;
	}

	public void findUSRectangle(CensusData censusData) {
		usRectangle = new Rectangle(censusData.data[0].latitude, censusData.data[0].latitude, censusData.data[0].longitude, censusData.data[0].longitude);
		for (CensusGroup censusGroup : censusData.data) {
			if(censusGroup != null) {

				if(censusGroup.latitude < usRectangle.left) {
					usRectangle.left = censusGroup.latitude;
				}
				if(censusGroup.latitude > usRectangle.right) {
					usRectangle.right = censusGroup.latitude;
				}
				
				if(censusGroup.longitude < usRectangle.bottom) {
					usRectangle.bottom = censusGroup.longitude;
				}
				if(censusGroup.longitude > usRectangle.top) {
					usRectangle.top = censusGroup.longitude;
				}
			}
		}
	}
	public void findPopulation(CensusData censusData) {
		long totalPopulationInArea = 0;
		long totalPopulation = 0;
		for (CensusGroup censusGroup : censusData.data) {
			if(censusGroup != null) {
				totalPopulation += censusGroup.population;
				
				float latToCol = ((censusGroup.latitude-usRectangle.left)/(usRectangle.right - usRectangle.left))*x; 
				float lonToRow = ((censusGroup.longitude-usRectangle.bottom)/(usRectangle.top - usRectangle.bottom))*y; 
				
				
				if( latToCol >= inputRecBoundary.left && 
						latToCol <= inputRecBoundary.right && 
						lonToRow >= inputRecBoundary.bottom && 
						lonToRow <= inputRecBoundary.top) {
					totalPopulationInArea += censusGroup.population;
				}
			}
		}
		System.out.println("Total Poluation in the Area: " + totalPopulationInArea);
		System.out.println("Total Poluation" + totalPopulation);
		float percent = (totalPopulationInArea * 100)/totalPopulation;
		System.out.println("percent of total population: " + percent);
	
	}
}
