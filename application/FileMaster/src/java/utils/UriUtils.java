/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Vlad
 */
public class UriUtils {
    private String originalURI;
    private String [] params;
    
    
    public UriUtils(String originalURI)
    {
        this.originalURI = originalURI;
        if(this.originalURI != null)
        {
            this.params = this.originalURI.split("/");
        }
    }
    
    public String getController()        
    {
        if(params.length > 2)
        {
            return this.params[2];
        }
        return null;
    }
    public String getSubController()
    {
        if(params.length > 3)
        {
            return this.params[3];
        }
        return null;
    }       
}
