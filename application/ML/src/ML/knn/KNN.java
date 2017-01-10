package ML.knn;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import ML.Classifier;
import ML.Instance;
import ML.kmeas.KMeansInstance;


public class KNN extends Classifier{

	private int k;
	private List<KNNInstance> instances;
	private Random randomizer = new Random();
	
	
 	public KNN(int k)
	{
		this.k = k;
		this.instances = new LinkedList<KNNInstance>();
	}


	@Override
	public void train() {
		// Not required for KNN, just load instances
	}


	@Override
	public int classifie(Instance test) {
		double similarity, maxSimilarity;
		TreeMap<Double, Instance> topK = new TreeMap();
		
		Iterator<KNNInstance> it = instances.iterator();
		if(!it.hasNext()) return -1;
		KNNInstance temp = it.next();
		similarity = maxSimilarity = test.computeEuclidDistance(temp);
		topK.put(similarity, temp);
		for(int i = 1; i < k && it.hasNext(); ++i)
		{
			temp = it.next();
			similarity = test.computeEuclidDistance(temp);
			topK.put(similarity, temp);
			if(similarity > maxSimilarity) similarity = maxSimilarity;
		}
		
		while(it.hasNext())
		{
			temp = it.next();
			similarity = test.computeEuclidDistance(temp);
			if(similarity < maxSimilarity)
			{
				topK.put(similarity, temp);
				topK.remove(topK.size()-1);
				maxSimilarity = topK.lastKey();
			}
		}
		
		return -2;
	}


	@Override
	public void setInstances(List<Instance> data) {
		this.instances = (List<KNNInstance>)(List<?>)data;
	}


	@Override
	public void addInstance(Instance newInstance) throws Exception {
		if(newInstance != null)
		{
			if(newInstance instanceof KNNInstance) this.instances.add((KNNInstance)newInstance);
			else throw new Exception("Invalid instance type used for kMeansAlgorithm");
		}
	}


	@Override
	public void addInstances(List<Instance> newInstances) {
		if(newInstances != null)
		{
			for(Instance newInstance : newInstances)
			{
				if(newInstance instanceof KNNInstance)this.instances.add((KNNInstance)newInstance);
			}
		}
	}
	
	@Override
	public Instance getInstanceObject(int numberOfAttributes) {
		return new KNNInstance(numberOfAttributes);
	}

	@Override
	public Instance getInstanceObject(double[] data) {
		return new KNNInstance(data);
	}


	@Override
	public Collection getSimilarInstances(Instance test) {
		double similarity, maxSimilarity;
		TreeMap<Double, Instance> topK = new TreeMap();
		
		Iterator<KNNInstance> it = instances.iterator();
		if(!it.hasNext()) return null;
		KNNInstance temp = it.next();
		similarity = maxSimilarity = test.computeEuclidDistance(temp);
		topK.put(similarity, temp);
		for(int i = 1; i < k && it.hasNext(); ++i)
		{
			temp = it.next();
			similarity = test.computeEuclidDistance(temp);
			topK.put(similarity, temp);
			if(similarity > maxSimilarity) maxSimilarity = similarity;
		}
		
		while(it.hasNext())
		{
			temp = it.next();
			similarity = test.computeEuclidDistance(temp);
			if(similarity < maxSimilarity)
			{
				topK.put(similarity, temp);
				topK.remove(topK.lastKey());
				maxSimilarity = topK.lastKey();
			}
		}
		
		List<Instance> output = new LinkedList();
		for(Map.Entry<Double, Instance> row : topK.entrySet())
		{
			output.add(row.getValue());
		}
		return output;
	}
}
