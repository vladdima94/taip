import java.util.LinkedList;
import java.util.List;

import ML.Classifier;
import ML.Instance;
import ML.ClassifierFactory;

public class Main {
	
	public static void main(String [] args)
	{
		Classifier model = ClassifierFactory.getInstanceFactory().getClassifier(ClassifierFactory.KNN, 3);
		
		
		Instance KMeansInstance1 = model.getInstanceObject(new double[]{1,1});
		Instance KMeansInstance2 = model.getInstanceObject(new double[]{1,2});
		Instance KMeansInstance3 = model.getInstanceObject(new double[]{2,1});
		Instance KMeansInstance4 = model.getInstanceObject(new double[]{3,1});
		Instance KMeansInstance5 = model.getInstanceObject(new double[]{0,1});
		
		Instance KMeansInstance6 = model.getInstanceObject(new double[]{8,9});
		Instance KMeansInstance7 = model.getInstanceObject(new double[]{10,9});
		Instance KMeansInstance8 = model.getInstanceObject(new double[]{7,9});
		Instance KMeansInstance9 = model.getInstanceObject(new double[]{9,8});
		

		Instance KMeansInstance10 = model.getInstanceObject(new double[]{1,7});
		Instance KMeansInstance11 = model.getInstanceObject(new double[]{1,8});
		Instance KMeansInstance12 = model.getInstanceObject(new double[]{1,9});
		Instance KMeansInstance13 = model.getInstanceObject(new double[]{2,8});
		Instance KMeansInstance14 = model.getInstanceObject(new double[]{2,9});
		
		List<Instance> data = new LinkedList();
		data.add(KMeansInstance1);
		data.add(KMeansInstance2);
		data.add(KMeansInstance3);
		data.add(KMeansInstance4);
		data.add(KMeansInstance5);
		data.add(KMeansInstance6);
		data.add(KMeansInstance7);
		data.add(KMeansInstance8);
		data.add(KMeansInstance9);
		data.add(KMeansInstance10);
		data.add(KMeansInstance11);
		data.add(KMeansInstance12);
		data.add(KMeansInstance13);
		data.add(KMeansInstance14);
		
		model.setInstances(data);
		model.train();

		Instance testKMeansInstance = model.getInstanceObject(new double[]{0,3});
//		System.out.println(model.classifie(testKMeansInstance));
		
		List<Instance> instancesResults = (List<Instance>) model.getSimilarInstances(testKMeansInstance);
		for(Instance instance : instancesResults)
		{
			System.out.println(instance.attributes[0] + " " + instance.attributes[1]);
		}
	}
	
	
}