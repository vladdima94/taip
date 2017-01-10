/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchSystem.SearchingAlgorithms;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import ML.Classifier;
import ML.ClassifierFactory;
import ML.Instance;
import ML.kmeas.KMeans;
import ML.kmeas.KMeansInstance;
import Models.ImageModel;
import dao.SearchSystemDAO;
import dao.Factory.DAOFactory;
import servlet.FileSlaveServlet;
import utils.ImageUtils;

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
	
    @Override
    public byte extractFeatures(Mat image, String outputFile)
    {
//    	Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HSV);
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
				vectors[counter+4] += Math.pow(pixel[0] - vectors[counter+1], 2);	 // S variation
				vectors[counter+5] += Math.pow(pixel[0] - vectors[counter+2], 2);    // V varation
			}
			vectors[counter+3] = Math.sqrt( vectors[counter+3] / sizeOfBlocks );     // H variation
			vectors[counter+4] = Math.sqrt( vectors[counter+4] / sizeOfBlocks );     // S variation
			vectors[counter+5] = Math.sqrt( vectors[counter+5] / sizeOfBlocks );     // V varation
			
			counter += 6;
		}
		
		writeVectorToFile(vectors, outputFile);
		image.release();
        return 1;
    }
    
    private void writeVectorToFile(double [] vector, String outputFile)
    {
    	try(FileOutputStream output = new FileOutputStream(outputFile);
    		FileChannel out = output.getChannel())
    	{
    			ByteBuffer buf = ByteBuffer.allocate(8 * vector.length);
    			buf.clear();
    			buf.asDoubleBuffer().put(vector);
    			out.write(buf);
//    			System.out.printf("Wrote [%d] bytes\r\n", out.write(buf));
    		}catch(IOException ex){
    			// FIXME: FAILED TO SAVE VECTOR, REMOVE FROM DB IMAGE
		}
    }
    
    private void readFile(double [] vector, String outputFile)
    {
    	try(FileInputStream output = new FileInputStream(outputFile);
    		FileChannel out = output.getChannel())
    	{
    			ByteBuffer buf = ByteBuffer.allocate(8 * vector.length);
    			buf.clear();
    			out.read(buf);
    			buf.flip();
    			buf.asDoubleBuffer().get(vector);
    		}catch(IOException ex){
    			// FIXME: FAILED TO SAVE VECTOR, REMOVE FROM DB IMAGE
		}
    }
    
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
				vectors[counter+4] += Math.pow(pixel[0] - vectors[counter+1], 2);	 // S variation
				vectors[counter+5] += Math.pow(pixel[0] - vectors[counter+2], 2);    // V varation
			}
			vectors[counter+3] = Math.sqrt( vectors[counter+3] / sizeOfBlocks );     // H variation
			vectors[counter+4] = Math.sqrt( vectors[counter+4] / sizeOfBlocks );     // S variation
			vectors[counter+5] = Math.sqrt( vectors[counter+5] / sizeOfBlocks );     // V varation
			
			counter += 6;
		}
		return vectors;
    }
    
    @Override
    public double getSimilarityLevel(double[] inputImage, double[] instance) {
        double similarity = 0;
    	for(int i = 0; i < inputImage.length && i < instance.length; i += 3)
        {
    		similarity += getDistance(inputImage[i], instance[i]);
        }
    	return 1.0 - similarity/(double)inputImage.length;
    }
    
    private double getDistance(double p1, double p2)
    {
    	return Math.abs(p1-p2)/256.0;
    }
    
    
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

//    	String outputPath = "C:\\Users\\Vlad\\Desktop\\results\\";
//    	int size = results.size();
//    	for(int i = 0; i < size; ++i)
//    	{
//    		ImageModel temp = results.get(i);
//			BufferedImage image = getImageFromLink(temp.link);
//			if(image == null || image.getType() > 7) continue;
//			Mat img = ImageUtils.Convert_BufferedImage2Mat_BGR(image);
//			Imgcodecs.imwrite(outputPath + i + ".jpg", img);
//			System.out.printf("Image [%d] similarity [%f]", i , temp.similarity);
//    	}
    }
    
    
    
	private BufferedImage getImageFromLink(String link)
	{
        URL url;
		try {
			url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setDoOutput(true);
	        conn.setRequestMethod("GET");
	        conn.setConnectTimeout(15000);
	        return ImageIO.read(conn.getInputStream());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		return null;
	}
}
