
public class V1SimpleAndSequential extends QueryAlgorithm{
	
	public V1SimpleAndSequential(int x, int y, CensusData censusData) {
		this.x = x;
		this.y = y;
		this.censusData = censusData;
	}

	public void findUSRectangle() {
		usRectangle = new Rectangle(censusData.data[0].latitude, censusData.data[0].latitude, censusData.data[0].longitude, censusData.data[0].longitude);
		Rectangle temp;
		for (CensusGroup censusGroup : censusData.data) {
			if(censusGroup != null) {
				temp = new Rectangle(censusGroup.latitude, censusGroup.latitude, censusGroup.longitude, censusGroup.longitude);
				usRectangle = usRectangle.encompass(temp);
			}
		}
	}
	public void findPopulation() {
		Long totalPopulationInArea = new Long(0);
		Long totalPopulation = new Long(0);
		for (CensusGroup censusGroup : censusData.data) {
			if(censusGroup != null) {
				totalPopulation += censusGroup.population;

				double westBound = (usRectangle.left + (inputRecBoundary.left - 1) * (usRectangle.right - usRectangle.left) / x);
				double eastBound = (usRectangle.left + (inputRecBoundary.right) * (usRectangle.right - usRectangle.left) / x);
				double northBound = (usRectangle.bottom + (inputRecBoundary.top) * (usRectangle.top - usRectangle.bottom) / y);
				double southBound = (usRectangle.bottom + (inputRecBoundary.bottom - 1) * (usRectangle.top - usRectangle.bottom) / y);

				if (censusGroup.latitude >= westBound 
						&& censusGroup.latitude <= eastBound
						&& censusGroup.longitude >= southBound
						&& censusGroup.longitude <= northBound
						) {
					totalPopulationInArea += censusGroup.population;
				}
			}
		}
		System.out.println("Total Poluation in the Area: " + totalPopulationInArea);
		System.out.println("Total Poluation: " + totalPopulation);
		float percent = (totalPopulationInArea.floatValue() * 100)/totalPopulation.floatValue();
		System.out.printf("percent of total population: %.2f \n",percent);

	}
}
