/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchSystem.SearchingAlgorithms;

import java.util.List;

import org.opencv.core.Mat;

import ML.Classifier;
import ML.Instance;
import Models.ImageModel;

/**
 *
 * @author Vlad
 */
public interface SearchAlgorithm {
    
	public void train();
	public Classifier getClassifier();
	public boolean isTrained();
	public int getFeatureSize();
    public byte extractFeatures(Mat image, String outputFile);
    public double [] extractFeatures(Mat image);
    public double getSimilarityLevel(double [] inputImage, double [] instance);
    public void sortData(List<ImageModel> results, Instance inputInstance);
}
