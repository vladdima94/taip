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
public class UnregisterSlaveController extends Controller{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, UriUtils uri) {
        try {
            String key = request.getParameter("key");
            if(key == null)
            {
                super.setQuickResponseMessage(400 ,"error", "Key not found", response);
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
        } catch (Exception ex) {
            
        }
    }
}
