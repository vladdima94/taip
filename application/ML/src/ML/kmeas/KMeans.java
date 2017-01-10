package ML.kmeas;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ML.Classifier;
import ML.Instance;


public class KMeans extends Classifier{
	private int k;
	private Cluster [] clusters;
	private Random randomizer = new Random();
	
	
 	public KMeans(int k)
	{
		this.k = k;
		this.clusters = new Cluster[k];
		this.instances = new LinkedList<KMeansInstance>();
	}

	public int classifie(Instance test)
	{
		if(test == null) return -1;
		if(test instanceof KMeansInstance){
			double tempDistance;
			double minDistance = test.computeEuclidDistance(this.clusters[0].position);
			Cluster minCluster = this.clusters[0];
			for(int i = 1; i < this.clusters.length; ++i)
			{
				tempDistance = test.computeEuclidDistance(this.clusters[i].position);
				if(tempDistance < minDistance)
				{
					minDistance = tempDistance;
					minCluster = this.clusters[i];
				}
			}
			return minCluster.clusterID;
		}
		return -2;
	}
	
	private Cluster getCorespondingCluster(Instance test)
	{
		if(test == null) return null;
		if(test instanceof KMeansInstance){
			double tempDistance;
			double minDistance = test.computeEuclidDistance(this.clusters[0].position);
			Cluster minCluster = this.clusters[0];
			for(int i = 1; i < this.clusters.length; ++i)
			{
				tempDistance = test.computeEuclidDistance(this.clusters[i].position);
				if(tempDistance < minDistance)
				{
					minDistance = tempDistance;
					minCluster = this.clusters[i];
				}
			}
			return minCluster;
		}
		return null;
	}
	

	public void train()
	{
		

		
		System.out.println("------------ Started Trainning ------------");
		System.out.println("-  Params   -");
		int numberOfInstances = instances.size();
		System.out.printf("Features Size - [%d]\r\n", instances.get(0).attributes.length);
		System.out.printf("Number of Features - [%d]\r\n", numberOfInstances);
		System.out.printf("Number of Clusters - [%d]\r\n", k);
		System.out.println("-------------");
		Map<Integer, Boolean> cacheInitalClustersPoints = new HashMap();
		for(int i = 0; i < k ; ++i)
		{
			this.clusters[i] = new Cluster(i+1);
			int random;
			while(cacheInitalClustersPoints.containsKey(random = ((int)(randomizer.nextDouble() * 10000)) % numberOfInstances));
			cacheInitalClustersPoints.put(random, true);
			this.clusters[i].position = instances.get( random ).clone();
			
			System.out.printf("ClusterID " + this.clusters[i].clusterID + " (Instance [%d]) : ", random);
			for(int ii = 0; ii < this.clusters[i].position.attributes.length; ++ii)
			{
				System.out.print(this.clusters[i].position.attributes[ii] + " ");
			}
			System.out.print("\r\n");
			
		}
		cacheInitalClustersPoints = null;
		
		
		
		
		
		
		cachedClustersPoints = new Cluster[k];
		cacheClustersPoints();
		int i = 1;
		do
		{
			System.out.println(" ------------ ITERATION " + i + " ------------- ");
			
			//save old Clusters points
			updateCacheClustersPoints();
			
			//update points to clusters
			updateClustersInstances();
			
			//update position of clusters
			updateClustersPosition();

			System.out.println(" -------- FINISHED ITERATION " + i++ + " -------- ");
		}while(checkIfClustersInstancesChanged());
		
		cachedClustersPoints = null;
		instances = null;
	}
	
	private void updateClustersInstances()
	{
		Iterator<KMeansInstance> it = this.instances.iterator();
		while(it.hasNext())
		{
			KMeansInstance instance = it.next();
			double tempDistance;
			double minDistance = instance.computeEuclidDistance(this.clusters[0].position);
			Cluster minCluster = this.clusters[0];
			for(int i = 1; i < this.clusters.length; ++i)
			{
				tempDistance = instance.computeEuclidDistance(this.clusters[i].position);
				if(tempDistance < minDistance)
				{
					minDistance = tempDistance;
					minCluster = this.clusters[i];
				}
			}
			if(instance.clusterAsignedTo != null) instance.clusterAsignedTo.removeInstance(instance);
			instance.clusterAsignedTo = minCluster;
			minCluster.instances.add(instance);
		}
	}
	
	
	
	
	
	private void updateClustersPosition()
	{
		for(int i = 0; i < clusters.length; ++i)
			clusters[i].updatePosition();
	}
	private Cluster [] cachedClustersPoints;
	private void cacheClustersPoints()
	{
		for(int i = 0; i < cachedClustersPoints.length; ++i)
		{
			cachedClustersPoints[i] = new Cluster();//this.clusters[i].cloneCheap();
		}
	}
	private void updateCacheClustersPoints()
	{
		for(int i = 0; i < cachedClustersPoints.length; ++i)
		{
			cachedClustersPoints[i].updateInstancesFrom(this.clusters[i]);
		}
	}
	
	private boolean checkIfClustersInstancesChanged()
	{
		for(int i = 0; i < this.clusters.length; ++i)
		{
			if(!this.clusters[i].equals(this.cachedClustersPoints[i])) return true;
		}
		return false;
	}

	
	
	public void setInstances(List<Instance> data)
	{
		this.instances = (List<KMeansInstance>)(List<?>)data;
	}

 	public void addInstance(Instance newInstance) throws Exception
	{
		if(newInstance != null)
		{
			if(newInstance instanceof KMeansInstance) this.instances.add((KMeansInstance)newInstance);
			else throw new Exception("Invalid instance type used for kMeansAlgorithm");
		}
	}

	public void addInstances(List<Instance> newInstances)
	{
		if(newInstances != null)
		{
			for(Instance newInstance : newInstances)
			{
				if(newInstance instanceof KMeansInstance)this.instances.add((KMeansInstance)newInstance);
			}
		}
	}

	@Override
	public Instance getInstanceObject(int numberOfAttributes) {
		return new KMeansInstance(numberOfAttributes);
	}

	@Override
	public Instance getInstanceObject(double[] data) {
		return new KMeansInstance(data);
	}

	@Override
	public Collection getSimilarInstances(Instance test) {
		return getCorespondingCluster(test).instances;
	}

	
	public Cluster [] getClusters()
	{
		return this.clusters;
	}
}
