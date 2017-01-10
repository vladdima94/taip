/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import SearchSystem.SearchAlgorithmFactory;
import SearchSystem.SearchingAlgorithms.SearchAlgorithm;
import dao.Concrete.ConcreteSearchSystemDAO;
import servlet.FileMasterServlet;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Exceptions.FileMasterNotConfiguredException;
import QueryProtocol.QueryProtocol;
import utils.UriUtils;
import utils.JSONUtils.JSONListAdapter;
import utils.JSONUtils.JSONMapAdapter;
import utils.JSONUtils.JSONMapObjectAdapter;
import utils.JSONUtils.JsonUtils;

/**
 *
 * @author Vlad
 */
public class SearchController extends Controller{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, UriUtils uri) {
        QueryProtocol sendRequests = new QueryProtocol();
        
        //TODO: add caching system
        
        List<Map<String, String>> results;
        try{
        	results = sendRequests.sendRequestsToSlaves(request, response, null);
        }catch(FileMasterNotConfiguredException ex){
            FileMasterServlet.writeToLog("<ERROR> SearchController.processRequest() : FileMasterNotConfiguredException(" + ex.getMessage() + ")");
            Controller.setQuickResponseMessage(500, "error", "File Master not configured yet", response);
            return;
        }
        List<JSONMapObjectAdapter<String>> resultsJSON = new LinkedList();
        if(results == null)return;
        for(Map<String, String> imgData : results)
        {
        	JSONMapObjectAdapter<String> temp = new JSONMapObjectAdapter();
        	temp.setData(imgData);
        	resultsJSON.add(temp);
        }
        JSONListAdapter<JSONMapObjectAdapter<String>> resultsAdapter = new JSONListAdapter();
        resultsAdapter.setData(resultsJSON);
        JsonUtils responseBody = new JsonUtils();
        responseBody.setStatus("success");
        responseBody.setJSONAdapter(resultsAdapter);
        try {
			responseBody.writeToOutput(response.getWriter());
		} catch (IOException ex) {
            FileMasterServlet.writeToLog("<ERROR> SearchController.processRequest() : IOException(" + ex.getMessage() + ")");
            response.setStatus(400);
		}
        response.setStatus(200);
    }
    
}
