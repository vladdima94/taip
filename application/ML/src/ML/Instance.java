package ML;

public abstract class Instance {
	public double [] attributes;
	
	public double computeEuclidDistance(Instance test)
	{
		double distance = 0;
		for(int i = 0; i < this.attributes.length; ++i)
		{
			distance += Math.pow(this.attributes[i] - test.attributes[i], 2);
		}
		
		return Math.sqrt(distance);
	}
}
