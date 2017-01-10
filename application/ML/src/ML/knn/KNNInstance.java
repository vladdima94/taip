package ML.knn;

import ML.Instance;
import ML.kmeas.KMeansInstance;

public class KNNInstance extends Instance{
	
	public String label;

	public KNNInstance(int numberOfAttributes)
	{
		this.attributes = new double[numberOfAttributes];
	}
	
	public KNNInstance(double [] attributes)
	{
		this.attributes = attributes;
	}
	
	@Override
	public Instance clone()
	{
		Instance clonned = new KNNInstance(this.attributes.length);
		for(int i = 0 ; i < this.attributes.length; ++i)
		{
			clonned.attributes[i] = this.attributes[i];
		}
		return clonned;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Instance)
		{
			Instance instance = (Instance) obj;
			if(attributes.length != instance.attributes.length) return false;
			for(int i = 0; i < attributes.length; ++i)
			{
				if(attributes[i] != instance.attributes[i]) return false;
			}
			return true;
		}
		return false;
	}

}
