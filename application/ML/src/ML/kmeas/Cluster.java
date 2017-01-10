package ML.kmeas;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ML.Instance;

public class Cluster{
	public int clusterID;
	public List<Instance> instances;
	public Instance position;
	
	public Cluster()
	{
		this.instances = new LinkedList();
	}
	
	public Cluster(int ID)
	{
		this.clusterID = ID;
		this.position = new KMeansInstance(2);
		this.instances = new LinkedList();
	}

	public void updatePosition()
	{
		double avr;
		int numberOfInstances = instances.size();
		System.out.print("clusterID: " + this.clusterID);
		for(int i = 0; i < position.attributes.length; ++i)
		{
			avr = 0;
			for(Instance instance : instances)
			{
				avr += instance.attributes[i];
			}
			position.attributes[i] = avr / numberOfInstances;
			System.out.print(position.attributes[i] + " ");
		}
		System.out.print("\r\n");
	}
	
	
	public Cluster cloneCheap()
	{
		Cluster cheapClone = new Cluster();
		cheapClone.instances.addAll(this.instances);
		return cheapClone;
	}
	
	public void updateInstancesFrom(Cluster cluster)
	{
		instances.clear();
		instances.addAll(cluster.instances);
	}
	
	public void removeInstance(Instance temp)
	{
		Iterator<Instance> it = this.instances.iterator();
		while(it.hasNext())
		{
			Instance test = it.next();
			if(test.equals(temp))
			{
				it.remove();
				return;
			}
		}
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Cluster)
		{
			Cluster testCluster = (Cluster) obj;
			int sizeThisCluster = this.instances.size();
			int sizeTestCluster = testCluster.instances.size();
			if(sizeThisCluster != sizeTestCluster) return false;
			
			Iterator<Instance> thisClusterIterator = this.instances.iterator();
			Iterator<Instance> testClusterIterator = testCluster.instances.iterator();
			
			while(thisClusterIterator.hasNext())
			{
				Instance test1 = thisClusterIterator.next();
				Instance test2 = testClusterIterator.next();
				if(!test1.equals(test2)) return false;
			}
			
			return true;
		}
		return false;
	}
}
