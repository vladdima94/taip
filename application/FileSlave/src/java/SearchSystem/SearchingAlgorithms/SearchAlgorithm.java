/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchSystem.SearchingAlgorithms;

import java.util.List;

/**
 *
 * @author Vlad
 */
public interface SearchAlgorithm {
    
    public void extractFeatures(String inputDBImages);
    public double getSimilarityLevel(double [] inputImage, double [] instance);
}
