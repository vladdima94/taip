/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchSystem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vlad
 */
public class CacheSystem {
    
    public List<String> getCachedResults(double [] data)
    {
        List<String> output = new ArrayList();
        //TODO: Search in db for cached queries
        return output;
    }
    
    public void addToCache(double [] data, List<String> images)
    {
        //TODO: add to cache with a timestamp, remove oldest from cache
        //      if is full
    }
}
