package ML;

import java.util.Collection;
import java.util.List;

import ML.kmeas.KMeansInstance;

public abstract class Classifier {
	
	protected List<KMeansInstance> instances;
	
	
	public abstract void train();
	public abstract int classifie(Instance test);
	public abstract void setInstances(List<Instance> data);
 	public abstract void addInstance(Instance newInstance) throws Exception;
	public abstract void addInstances(List<Instance> newInstances);
	public abstract Instance getInstanceObject(int numberOfAttributes);
	public abstract Instance getInstanceObject(double [] data);
	public abstract Collection getSimilarInstances(Instance test);

}
