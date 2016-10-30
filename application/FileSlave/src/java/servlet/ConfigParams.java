/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

/**
 *
 * @author Vlad
 */
public class ConfigParams {
    private String imageFeatureVectorsDBPath;
    private String appURI;

    public ConfigParams(String imageFeatureVectorsDBPath, String appURI, String pathToConfigFile) {
        this.imageFeatureVectorsDBPath = imageFeatureVectorsDBPath;
        this.appURI = appURI;
    }
    
    public void loadAppParams()
    {
        
    }
}
