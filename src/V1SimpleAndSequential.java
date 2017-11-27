
public class V1SimpleAndSequential extends QueryAlgorithm{
	
	public V1SimpleAndSequential(int x, int y, CensusData censusData) {
		this.x = x;
		this.y = y;
		this.censusData = censusData;
	}

	public void findUSRectangle() {
		usRectangle = new Rectangle(censusData.data[0].longitude, censusData.data[0].longitude, censusData.data[0].latitude, censusData.data[0].latitude);
		Rectangle temp;
		for (int i = 1; i < censusData.data_size; i++) {
			CensusGroup censusGroup = censusData.data[i];
			if(censusGroup != null) {
				temp = new Rectangle(censusGroup.longitude, censusGroup.longitude, censusGroup.latitude, censusGroup.latitude);
				usRectangle = usRectangle.encompass(temp);
				totalPopulation += censusGroup.population;
			}
		}
	}
	
	public void findPopulation() {
		Long totalPopulationInArea = new Long(0);
		
		double westBound = (usRectangle.left + (inputRecBoundary.left - 1) * ((usRectangle.right - usRectangle.left) / x));
		double eastBound = (usRectangle.left + (inputRecBoundary.right) * ((usRectangle.right - usRectangle.left) / x));
		double northBound = (usRectangle.bottom + (inputRecBoundary.top) * ((usRectangle.top - usRectangle.bottom) / y));
		double southBound = (usRectangle.bottom + (inputRecBoundary.bottom - 1) * ((usRectangle.top - usRectangle.bottom) / y));
		
		for (CensusGroup censusGroup : censusData.data) {
			if(censusGroup != null) {
	            float groupLong = censusGroup.longitude;
	            float groupLat = censusGroup.latitude;
	            // Defaults to North and/or East in case of tie
	            if (groupLat >= southBound &&
	                    (groupLat < northBound ||
	                            (northBound - usRectangle.top) >= 0) &&
	                            (groupLong < eastBound ||
	                                    (eastBound - usRectangle.right) >= 0) &&
	                                    groupLong >= westBound)
	            	totalPopulationInArea += censusGroup.population;
			}
		}
		System.out.println("Total Population in the Area: " + totalPopulationInArea);
		System.out.println("Total Population: " + totalPopulation);
		float percent = (totalPopulationInArea.floatValue() * 100)/totalPopulation.floatValue();
		System.out.printf("Percent of total population: %.2f \n",percent);
	}
}
