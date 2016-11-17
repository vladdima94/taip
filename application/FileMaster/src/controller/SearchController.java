/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import SearchSystem.SearchAlgorithmFactory;
import SearchSystem.SearchingAlgorithms.SearchAlgorithm;
import dao.SearchSystemDAO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.UriUtils;

/**
 *
 * @author Vlad
 */
public class SearchController implements Controller{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, UriUtils uri) {
        String algorithm = request.getParameter("algorithm");
        //TODO: read double [] vectors from request and search
        double [] data = null;
        
        SearchAlgorithm searchAlg = SearchAlgorithmFactory.getInstance().getAlgorithm(algorithm);
        new SearchSystemDAO().searchFiles(10, searchAlg);
    }
    
}
