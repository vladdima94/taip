package ML;

import ML.kmeas.KMeans;
import ML.kmeas.KMeansInstance;
import ML.knn.KNN;

public class ClassifierFactory {

	public static final int KMEANS = 0;
	public static final int KNN = 1;
	
	private static ClassifierFactory instance;
	
	private ClassifierFactory(){};
	@Override
	public ClassifierFactory clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException("Trying to clone singleton object!");
	}
	
	public static synchronized ClassifierFactory getInstanceFactory()
	{
		if(instance == null)
			instance = new ClassifierFactory();
		
		return instance;
	}
	
	
	public Classifier getClassifier(int INSTANCE_TYPE, int param)
	{
		switch(INSTANCE_TYPE)
		{
			case KMEANS:{
				return new KMeans(param);
			}
			case KNN:{
				return new KNN(param);
			}
			default: return null;
		}
	}
}
