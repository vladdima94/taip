package SearchSystem.SearchingAlgorithms;

import ML.kmeas.KMeansInstance;

public class KMeansImageInstance extends KMeansInstance{

	public int imageID;
	
	public KMeansImageInstance(double[] attributes, int ID) {
		super(attributes);
		this.imageID = ID;
	}
	public KMeansImageInstance(int sizeOfFeature) {
		super(sizeOfFeature);
	}
	
}
