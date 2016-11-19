/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import SearchSystem.SearchingAlgorithms.SearchAlgorithm;
import SearchSystem.SearchingAlgorithms.SearchAlgorithmFactory;
import dao.SearchSystemDAO;
import servlet.FileSlaveServlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Models.ImageModel;
import utils.UriUtils;
import utils.JSONUtils.JSONListAdapter;
import utils.JSONUtils.JsonUtils;

/**
 *
 * @author Vlad
 */
public class SearchController implements Controller{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, UriUtils uri) {
        
    	//TODO: read image from request and give it to search algorithm //////////////////////
        double [] data = null;    
        SearchAlgorithm searchAlg = SearchAlgorithmFactory.getInstance().getAlgorithm(FileSlaveServlet.configParams.get("algorithm"));
        List<ImageModel> results = new SearchSystemDAO().searchFiles(10, searchAlg);
        //////////////////////////////////////////////////////////////////////////////////////
        
        
//        List<JSONMapObjectAdapter<String>> resultsJSON = new LinkedList<JSONMapObjectAdapter<String>>();
//        for(Map<String, String> imgData : results)
//        {
//        	JSONMapObjectAdapter<String> temp = new JSONMapObjectAdapter();
//        	temp.setData(imgData);
//        	resultsJSON.add(temp);
//        }
        JSONListAdapter<ImageModel> resultsAdapter = new JSONListAdapter();
        resultsAdapter.setData(results);
        JsonUtils responseBody = new JsonUtils();
        responseBody.setStatus("success");
        responseBody.setJSONAdapter(resultsAdapter);
        try {
			responseBody.writeToOutput(response.getWriter());
		} catch (IOException ex) {
            FileSlaveServlet.writeToLog("<ERROR> SearchController.processRequest() : IOException(" + ex.getMessage() + ")");
            response.setStatus(400);
		}
        response.setStatus(200);
    }

}
