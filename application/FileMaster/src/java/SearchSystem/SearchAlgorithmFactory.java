/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchSystem;

import SearchSystem.SearchingAlgorithms.CBIRSearchAlgorithm;
import SearchSystem.SearchingAlgorithms.SearchAlgorithm;

/**
 *
 * @author Vlad
 */
public class SearchAlgorithmFactory {
    private static SearchAlgorithmFactory instance;
    private SearchAlgorithmFactory(){};
    static
    {
        instance = new SearchAlgorithmFactory();
    }
    
    public static SearchAlgorithmFactory getInstance()
    {
        return instance;
    }
    
    public SearchAlgorithm getAlgorithm(String type)
    {
        if(type == null) return new CBIRSearchAlgorithm();
        switch(type)
        {
            case"CBIR": return new CBIRSearchAlgorithm();
            default: return new CBIRSearchAlgorithm();
        }
    }
}

