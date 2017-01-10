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
            Controller.setQuickResponseMessage(200, "success", "Succesfully unregistered Slave", response);
            FileMasterServlet.writeToLog("<STATUS> Succesully unregistered FileSlave with token [" + key + "]");
        } catch (Exception ex) {
            
        }
    }
}
