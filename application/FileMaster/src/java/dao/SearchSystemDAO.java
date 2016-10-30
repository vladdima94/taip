/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import SearchSystem.SearchingAlgorithms.SearchAlgorithm;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vlad
 */
public class SearchSystemDAO extends DAO{
    
    
    public List<String> searchFiles(double similarityThreshold, SearchAlgorithm testingAlgorithm)
    {
        List<String> results = new ArrayList();
        //TODO: get through all instances in db and use testingAlgorithm to test similarities;
        return results;
    }
}
