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
import servlet.FileMasterServlet;
import utils.JSONUtils.JsonUtils;
import utils.UriUtils;

/**
 *
 * @author Vlad
 */
public class UnregisterSlaveController implements Controller{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, UriUtils uri) {
        try {
            String key = request.getParameter("key");
            if(key == null)
            {
                setErrorMessage(400 , "Key not found", response);
                return;
            }
            
            QueryProtocol queryP = new QueryProtocol();
            queryP.unregisterEntity(key);
            
            JsonUtils responseBody = new JsonUtils();
            responseBody.setStatus("success");
            responseBody.writeToOutput(response.getWriter());
            response.setStatus(200);
            FileMasterServlet.writeToLog("<STATUS> Succesully unregistered FileSlave with token [" + key + "]");
        } catch (IOException ex) {
            FileMasterServlet.writeToLog("<ERROR> UnregisterSlaveController.processRequest() : ClassNotFoundException(" + ex.getMessage() + ")");
        } catch (EntityAlreadyRegisteredException ex) {
            try {
                FileMasterServlet.writeToLog("<ERROR> UnregisterSlaveController.registerEntity.addTokenToDB() : EntityAlreadyRegisteredException(" + ex.getMessage() + ")");
                setErrorMessage(400 , "link or token not found in data JSON", response);
            } catch (IOException ex1) {
                FileMasterServlet.writeToLog("<ERROR> UnregisterSlaveController.processRequest() : ClassNotFoundException(" + ex.getMessage() + ")");
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
