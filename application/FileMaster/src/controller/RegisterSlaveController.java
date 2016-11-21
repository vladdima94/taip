/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Exceptions.EntityAlreadyRegisteredException;
import QueryProtocol.QueryProtocol;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import servlet.FileMasterServlet;
import utils.JSONUtils.JsonUtils;
import utils.UriUtils;

/**
 *
 * @author Vlad
 */
public class RegisterSlaveController extends Controller{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, UriUtils uri){
        try {
            JSONObject requestBody = JsonUtils.readBody(request.getReader());
            if(requestBody == null)
            {
            	super.setQuickResponseMessage(400 ,"error", "No request specified!", response);
                return;
            }
            JSONArray data = (JSONArray) requestBody.get("data");
            if(data == null)
            {
            	super.setQuickResponseMessage(400 ,"error", "Invalid data format!", response);
                return;
            }
            JSONObject linkObj = (JSONObject) data.get(1);
            JSONObject tokenObj = (JSONObject) data.get(3);
            JSONObject currentDB = (JSONObject) data.get(0);
            JSONObject maxDB = (JSONObject) data.get(2);
            if(linkObj == null || tokenObj == null || currentDB == null || maxDB == null)
            {
            	super.setQuickResponseMessage(400 ,"error", "link, token, maxDB, currentDB or key not found in data JSON", response);
                return;
            }
            
            String link = (String)linkObj.get("link");
            String token = (String)tokenObj.get("token");
            String maxDBSize = (String)maxDB.get("maxDBSize");
            String currentDBSize = (String)currentDB.get("currentDBSize");
            if(link == null || token == null || currentDBSize == null || maxDBSize == null)
            {
            	super.setQuickResponseMessage(400 ,"error", "link, token, maxDB, currentDB or key not found in data JSON", response);
                return;
            }
            
            QueryProtocol queryP = new QueryProtocol();
            queryP.registerEntity(link, token, maxDBSize, currentDBSize, response);
        } catch (IOException ex) {
            FileMasterServlet.writeToLog("<ERROR> RegisterSlaveController.processRequest() : IOException(" + ex.getMessage() + ")");
            super.setQuickResponseMessage(500 , "error", "Internal Server Error. We are trying to fix it!", response);
        } catch(Exception ex)
        {
        	
        }
    }
}
