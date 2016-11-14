/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Exceptions.EntityAlreadyRegisteredException;
import QueryProtocol.QueryProtocol;
import java.io.IOException;
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
public class RegisterSlaveController implements Controller{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, UriUtils uri) {
        try {
            JSONObject requestBody = JsonUtils.readBody(request.getReader());
            JSONArray data = (JSONArray) requestBody.get("data");
            if(data == null)
            {
                setErrorMessage(400 , "Invalid data format!", response);
                return;
            }
            
            JSONObject linkObj = (JSONObject) data.get(0);
            JSONObject tokenObj = (JSONObject) data.get(1);
            if(linkObj == null || tokenObj == null)
            {
                setErrorMessage(400 , "link, token or key not found in data JSON", response);
                return;
            }
            
            String link = (String)linkObj.get("link");
            String token = (String)tokenObj.get("token");
            if(link == null || token == null)
            {
                setErrorMessage(400 , "link, token or key not found in data JSON", response);
                return;
            }
            
            QueryProtocol queryP = new QueryProtocol();
            queryP.registerEntity(link, token);
            
            JsonUtils responseBody = new JsonUtils();
            responseBody.setStatus("success");
            responseBody.writeToOutput(response.getWriter());
            response.setStatus(200);
            FileMasterServlet.writeToLog("<STATUS> Succesully registered FileSlave [" + link + "] with token [" + token + "]");
        } catch (IOException ex) {
            FileMasterServlet.writeToLog("<ERROR> RegisterSlaveController.processRequest() : ClassNotFoundException(" + ex.getMessage() + ")");
        } catch (EntityAlreadyRegisteredException ex) {
            try {
                FileMasterServlet.writeToLog("<ERROR> QueryProtocol.registerEntity.addTokenToDB() : EntityAlreadyRegisteredException(" + ex.getMessage() + ")");
                setErrorMessage(400 , "link or token not found in data JSON", response);
            } catch (IOException ex1) {
                FileMasterServlet.writeToLog("<ERROR> RegisterSlaveController.processRequest() : ClassNotFoundException(" + ex.getMessage() + ")");
            }
        }
    }
    
    
    public void setErrorMessage(int responseStatus, String message, HttpServletResponse response) throws IOException
    {
        response.setStatus(responseStatus);
        response.setHeader("Content-Type", "application/json");
        JsonUtils responseError = new JsonUtils();
        responseError.setMessage(message);
        responseError.setStatus("error");
        responseError.writeToOutput(response.getWriter());
    }
}
