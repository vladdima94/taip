/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchSystem.SearchingAlgorithms;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import ML.Classifier;
import ML.ClassifierFactory;
import ML.Instance;
import ML.kmeas.KMeans;
import Models.ImageModel;
import dao.SearchSystemDAO;
import dao.Factory.DAOFactory;
import servlet.FileSlaveServlet;

/**
 *
 * @author Vlad
 */
public class CBIRSearchAlgorithm implements SearchAlgorithm{
    public int numberBlocksWidth = 8;
    public int numberBlocksHeigth = 8;
    
    private Classifier clusterizationAlg;
    
    @Override
    public boolean isTrained()
    {
    	return clusterizationAlg != null;
    }
    @Override
    public Classifier getClassifier()
    {
    	return clusterizationAlg;
    }
    
    @Override
    public int getFeatureSize()
    {
    	return numberBlocksWidth * numberBlocksHeigth * 6;
    }
    
    @Override
    public void train()
    {
    	clusterizationAlg = ClassifierFactory.getInstanceFactory().getClassifier(ClassifierFactory.KMEANS, Integer.parseInt(FileSlaveServlet.configParams.get("numberOfClusters")));
    	SearchSystemDAO searchDAO = (SearchSystemDAO)FileSlaveServlet.daoFactory.getDAOInstance(DAOFactory.SEARCH_SYSTEM_DAO);
    	List<Instance> instances;
		try {
			instances = searchDAO.getInstancesFromDB(this);
	    	clusterizationAlg.setInstances(instances);
	    	clusterizationAlg.train();
	    	searchDAO.updateImageToClusters((KMeans)clusterizationAlg);
	    	return;
		} catch (ClassNotFoundException ex) {
            FileSlaveServlet.writeToLog("<ERROR> CBIRSearchAlgorithm.train() : ClassNotFoundException(" + ex.getMessage() + ")");
			ex.printStackTrace();
		} catch (SQLException ex) {
            FileSlaveServlet.writeToLog("<ERROR> CBIRSearchAlgorithm.train() : SQLException(" + ex.getMessage() + ")");
			ex.printStackTrace();
		}
		clusterizationAlg = null;
    }

    

    
    //////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////// RGB FEATURES EXTRACTION ////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    public byte extractFeatures(Mat image, String outputFile)
    {
    	int rows = image.rows();
    	int cols = image.cols();
    	int blockHeigth = rows / numberBlocksHeigth;
    	int blockWidth = cols / numberBlocksWidth;
    	int sizeOfBlocks = blockHeigth*blockWidth;
    	rows = numberBlocksHeigth * blockHeigth;
    	cols = numberBlocksWidth * blockWidth;
    	Imgproc.resize(image, image, new Size(cols, rows));
    	
        double [] vectors = new double[numberBlocksWidth*numberBlocksHeigth*6]; // 3 channels, average and variance for each
        int counter = 0;
        
		for(int i = 0; i < rows; i += blockHeigth)
		for(int j = 0; j < cols; j += blockWidth)
		{
			for(int ii = i; ii < i + blockHeigth; ++ii)
			for(int jj = j; jj < j + blockWidth; ++jj)
			{
				double pixel [] = image.get(ii, jj);
				vectors[counter] += pixel[0];      // H mean
				vectors[counter+1] += pixel[1];    // S mean
				vectors[counter+2] += pixel[2];    // V mean
			}
			vectors[counter] /= sizeOfBlocks;      // H mean
			vectors[counter+1] /= sizeOfBlocks;    // S mean
			vectors[counter+2] /= sizeOfBlocks;    // V mean
			
			for(int ii = i; ii < i + blockHeigth; ++ii)
			for(int jj = j; jj < j + blockWidth; ++jj)
			{
				double pixel [] = image.get(ii, jj);
				vectors[counter+3] += Math.pow(pixel[0] - vectors[counter], 2);      // H variation
				vectors[counter+4] += Math.pow(pixel[1] - vectors[counter+1], 2);	 // S variation
				vectors[counter+5] += Math.pow(pixel[2] - vectors[counter+2], 2);    // V variation
			}
			vectors[counter+3] = Math.sqrt( vectors[counter+3] / sizeOfBlocks );     // H variation
			vectors[counter+4] = Math.sqrt( vectors[counter+4] / sizeOfBlocks );     // S variation
			vectors[counter+5] = Math.sqrt( vectors[counter+5] / sizeOfBlocks );     // V variation
			
			counter += 6;
		}
		
		writeVectorToFile(vectors, outputFile);
		image.release();
        return 1;
    }

    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    
    
    

    //////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////// HUE FEATURES EXTRACTION ////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////
    
    public byte extractFeaturesHUE(Mat image, String outputFile)
    {
    	int rows = image.rows();
    	int cols = image.cols();
    	int blockHeigth = rows / numberBlocksHeigth;
    	int blockWidth = cols / numberBlocksWidth;
    	int sizeOfBlocks = blockHeigth*blockWidth;
    	rows = numberBlocksHeigth * blockHeigth;
    	cols = numberBlocksWidth * blockWidth;
    	Imgproc.resize(image, image, new Size(cols, rows));
    	Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HSV);
    	
        double [] vectors = new double[numberBlocksWidth*numberBlocksHeigth*6]; // 3 channels, average and variance for each
        int counter = 0;
        
        double Hmean = 0;
        double Smean = 0;
        double Vmean = 0;
        
		for(int i = 0; i < rows; i += blockHeigth)
		for(int j = 0; j < cols; j += blockWidth)
		{
			double pixel [] = image.get(i, j);
			for(int ii = i; ii < i + blockHeigth; ++ii)
			for(int jj = j; jj < j + blockWidth; ++jj)
			{
				pixel = image.get(ii, jj);
				if(vectors[counter] < pixel[0]) vectors[counter] = pixel[0];      // H max
				if(vectors[counter+1] < pixel[1]) vectors[counter+1] = pixel[1];      // H max
				if(vectors[counter+2] < pixel[2]) vectors[counter+2] = pixel[1];      // H max
				
				Hmean += pixel[0];      // H mean
				Smean += pixel[1];    		// S mean
				Vmean += pixel[2];   		// V mean
			}
			vectors[counter] *= 2;  				// OpenCV scales H to H/2 to fit uchar;
			
			Hmean = Hmean * 2 / sizeOfBlocks;       // H mean
			Smean /= sizeOfBlocks;    				// S mean
			Vmean /= sizeOfBlocks;    				// V mean
			
			for(int ii = i; ii < i + blockHeigth; ++ii)
			for(int jj = j; jj < j + blockWidth; ++jj)
			{
				pixel = image.get(ii, jj);
				vectors[counter+3] += Math.pow(pixel[0] * 2 - Hmean, 2);      // H variation
				vectors[counter+4] += Math.pow(pixel[1] - Smean, 2);	 // S variation
				vectors[counter+5] += Math.pow(pixel[2] - Vmean, 2);    // V variation
			}
			vectors[counter+3] = Math.sqrt( vectors[counter+3] / sizeOfBlocks );     // H variation
			vectors[counter+4] = Math.sqrt( vectors[counter+4] / sizeOfBlocks );     // S variation
			vectors[counter+5] = Math.sqrt( vectors[counter+5] / sizeOfBlocks );     // V variation
			
			counter += 6;
		}
		
		writeVectorToFile(vectors, outputFile);
		image.release();
        return 1;
    }

    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    
    
    
    private void writeVectorToFile(double [] vector, String outputFile)
    {
    	try(FileOutputStream output = new FileOutputStream(outputFile);
    		FileChannel out = output.getChannel())
    	{
    			ByteBuffer buf = ByteBuffer.allocate(8 * vector.length);
    			buf.clear();
    			buf.asDoubleBuffer().put(vector);
    			out.write(buf);
    		}catch(IOException ex){
    			// FIXME: FAILED TO SAVE VECTOR, REMOVE FROM DB IMAGE
		}
    }

    
    
    
    
    
    

    //////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////// HUE FEATURES EXTRACTION ////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////
    
    public double [] extractFeaturesHUE(Mat image)
    {
    	int rows = image.rows();
    	int cols = image.cols();
    	int blockHeigth = rows / numberBlocksHeigth;
    	int blockWidth = cols / numberBlocksWidth;
    	int sizeOfBlocks = blockHeigth*blockWidth;
    	rows = numberBlocksHeigth * blockHeigth;
    	cols = numberBlocksWidth * blockWidth;
    	Imgproc.resize(image, image, new Size(cols, rows));
    	Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HSV);
    	
        double [] vectors = new double[numberBlocksWidth*numberBlocksHeigth*6]; // 3 channels, average and variance for each
        int counter = 0;
        
        double Hmean = 0;
        double Smean = 0;
        double Vmean = 0;
        
		for(int i = 0; i < rows; i += blockHeigth)
		for(int j = 0; j < cols; j += blockWidth)
		{
			double pixel [] = image.get(i, j);
			for(int ii = i; ii < i + blockHeigth; ++ii)
			for(int jj = j; jj < j + blockWidth; ++jj)
			{
				pixel = image.get(ii, jj);
				if(vectors[counter] < pixel[0]) vectors[counter] = pixel[0];      // H max
				if(vectors[counter+1] < pixel[1]) vectors[counter+1] = pixel[1];      // H max
				if(vectors[counter+2] < pixel[2]) vectors[counter+2] = pixel[1];      // H max
				
				Hmean += pixel[0];      // H mean
				Smean += pixel[1];    		// S mean
				Vmean += pixel[2];   		// V mean
			}
			vectors[counter] *= 2;  				// OpenCV scales H to H/2 to fit uchar;
			
			Hmean = Hmean * 2 / sizeOfBlocks;       // H mean
			Smean /= sizeOfBlocks;    				// S mean
			Vmean /= sizeOfBlocks;    				// V mean
			
			for(int ii = i; ii < i + blockHeigth; ++ii)
			for(int jj = j; jj < j + blockWidth; ++jj)
			{
				pixel = image.get(ii, jj);
				vectors[counter+3] += Math.pow(pixel[0] * 2 - Hmean, 2);      // H variation
				vectors[counter+4] += Math.pow(pixel[1] - Smean, 2);	 // S variation
				vectors[counter+5] += Math.pow(pixel[2] - Vmean, 2);    // V variation
			}
			vectors[counter+3] = Math.sqrt( vectors[counter+3] / sizeOfBlocks );     // H variation
			vectors[counter+4] = Math.sqrt( vectors[counter+4] / sizeOfBlocks );     // S variation
			vectors[counter+5] = Math.sqrt( vectors[counter+5] / sizeOfBlocks );     // V variation
			
			counter += 6;
		}
		
		image.release();
        return vectors;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    
    
    
    
    

    //////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////// RGB FEATURES EXTRACTION ////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////
    
    public double [] extractFeatures(Mat image)
    {
//    	Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HSV);
    	int rows = image.rows();
    	int cols = image.cols();
    	int blockHeigth = rows / numberBlocksHeigth;
    	int blockWidth = cols / numberBlocksWidth;
    	int sizeOfBlocks = blockHeigth*blockWidth;
    	System.out.println(sizeOfBlocks);
    	rows = numberBlocksHeigth * blockHeigth;
    	cols = numberBlocksWidth * blockWidth;
    	Imgproc.resize(image, image, new Size(cols, rows));
    	
        double [] vectors = new double[numberBlocksWidth*numberBlocksHeigth*6]; // 3 channels, average and variance for each
        int counter = 0;
        
		for(int i = 0; i < rows; i += blockHeigth)
		for(int j = 0; j < cols; j += blockWidth)
		{
			for(int ii = i; ii < i + blockHeigth; ++ii)
			for(int jj = j; jj < j + blockWidth; ++jj)
			{
				double pixel [] = image.get(ii, jj);
				vectors[counter] += pixel[0];      // H mean
				vectors[counter+1] += pixel[1];    // S mean
				vectors[counter+2] += pixel[2];    // V mean
			}
			vectors[counter] /= sizeOfBlocks;      // H mean
			vectors[counter+1] /= sizeOfBlocks;    // S mean
			vectors[counter+2] /= sizeOfBlocks;    // V mean
			
			for(int ii = i; ii < i + blockHeigth; ++ii)
			for(int jj = j; jj < j + blockWidth; ++jj)
			{
				double pixel [] = image.get(ii, jj);
				vectors[counter+3] += Math.pow(pixel[0] - vectors[counter], 2);      // H variation
				vectors[counter+4] += Math.pow(pixel[1] - vectors[counter+1], 2);	 // S variation
				vectors[counter+5] += Math.pow(pixel[2] - vectors[counter+2], 2);    // V varation
			}
			vectors[counter+3] = Math.sqrt( vectors[counter+3] / sizeOfBlocks );     // H variation
			vectors[counter+4] = Math.sqrt( vectors[counter+4] / sizeOfBlocks );     // S variation
			vectors[counter+5] = Math.sqrt( vectors[counter+5] / sizeOfBlocks );     // V varation
			
			counter += 6;
		}
		image.release();
		return vectors;
    }

    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////

    
    
    
    
    
    
    
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////// RGB SIMILARITY LEVEL ////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    
    @Override
    public double getSimilarityLevel(double[] inputImage, double[] instance) {
        double similarity = 0;
    	for(int i = 0; i < inputImage.length && i < instance.length; i += 3)
        {
    		similarity += getDistance(inputImage[i], instance[i]);
        }
    	return 1.0 - similarity/(double)inputImage.length;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    
    
    
    

    ///////////////////////////////////////////////////////////////////////////
    ///////////////////// HUE SIMILARITY LEVEL ////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    
    public double getSimilarityLevelHSV(double[] inputImage, double[] instance) {
        double similarity = 0;
    	for(int i = 0; i < inputImage.length && i < instance.length; i += 6)
        {
    		similarity += (1 / (1 + getHueDistance(inputImage[i], instance[i]) 
    							  + getSVDistance(inputImage[i+1], instance[i+1])
    							  + getSVDistance(inputImage[i+2], instance[i+2])));
        }
    	return similarity/64.0;
    }
    
    private static double hueCons = 1/256;
    private static double hueCons2 = Math.PI / 2;
    private double getHueDistance(double a, double b)
    {
    	return 1 - Math.pow(Math.cos(hueCons * Math.abs(a - b) * hueCons2), 2);
    }
    private double getSVDistance(double a, double b)
    {
    	return Math.abs(a-b) / 256.0;
    }
    
    
    private double getDistance(double p1, double p2)
    {
    	return Math.abs(p1-p2)/256.0;
    }

    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    
    
    
    
    
    
    
    
    public void sortData(List<ImageModel> results, Instance inputInstance)
    {
    	int featureVectorSize = getFeatureSize();
    	double [] instanceFeatureVector = new double[featureVectorSize];
    	double [] inputImageFeatureVector = inputInstance.attributes;
    	for(ImageModel image : results)
    	{
    		try
    		{
    			SearchSystemDAO.readFeatureVector(instanceFeatureVector, image.diskPath);
    			image.similarity = getSimilarityLevel(inputImageFeatureVector, instanceFeatureVector);
			}catch(IOException ex){
				
			}
    	}
    	
    	
    	Collections.sort(results);
    }
    
}
