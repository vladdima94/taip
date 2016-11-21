/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.JSONUtils.JSONListAdapter;
import utils.JSONUtils.JsonUtils;
import utils.UriUtils;

/**
 *
 * @author Vlad
 */
public class ErrorController extends Controller{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, UriUtils uri) {
        try {
            JsonUtils responseBody = new JsonUtils();
            responseBody.setStatus("Error, unknown Action!");
            
            List<String> data = new ArrayList();
            data.add("Test1");
            data.add("Test2");
            JSONListAdapter test = new JSONListAdapter();
            test.setData(data);
            responseBody.setJSONAdapter(test);
            responseBody.writeToOutput(response.getWriter());
        } catch (IOException ex) {
            Logger.getLogger(ErrorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
