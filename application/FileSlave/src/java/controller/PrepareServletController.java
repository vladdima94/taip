/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlet.FileSlaveServlet;
import utils.UriUtils;

/**
 *
 * @author Vlad
 */
public class PrepareServletController implements Controller{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, UriUtils uri) {
        String configPath = request.getParameter("configFile");
        FileSlaveServlet.loadConfigParams(configPath);
    }
    
}
