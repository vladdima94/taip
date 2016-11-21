/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servlet.FileMasterServlet;
import utils.UriUtils;
import utils.JSONUtils.JsonUtils;

/**
 *
 * @author Vlad
 */
public abstract class Controller {
    public abstract void processRequest(HttpServletRequest request, HttpServletResponse response, UriUtils uri);
    
    public boolean logged;

    
    public static void setQuickResponseMessage(int responseStatus,String status , String message, HttpServletResponse response)
    {
        try {
            response.setStatus(responseStatus);
            response.setHeader("Content-Type", "application/json");
            JsonUtils responseError = new JsonUtils();
            responseError.setMessage(message);
            responseError.setStatus(status);
			responseError.writeToOutput(response.getWriter());
		} catch (IOException ex) {
            FileMasterServlet.writeToLog("<ERROR> Controller.setErrorMessage() : IOException(" + ex.getMessage() + ")");
		}
    }
}

