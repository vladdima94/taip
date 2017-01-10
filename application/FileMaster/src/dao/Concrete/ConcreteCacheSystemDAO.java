/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.Concrete;

import java.util.LinkedList;
import java.util.List;

import dao.CacheSystemDAO;

/**
 *
 * @author Vlad
 */
public class ConcreteCacheSystemDAO implements CacheSystemDAO{
    public void addCacheToDB(double [] data, List<String> results)
    {
        //TODO: add to cacheDB implementation
    }
    
    public List<String> getCacheFromDB(int queryID)
    {
        List<String> results = new LinkedList();
        //TODO: return cache from DB for specific queryID,
        //      update query cache timestamp !!!
        return results;
    }
}
