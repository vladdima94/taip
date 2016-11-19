package servlet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import QueryProtocol.QueryProtocol;
import controller.Controller;
import controller.ControllerFactory;
import dao.ConfigParamsDAO;
import dao.SearchSystemDAO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.UriUtils;

/**
 *
 * @author Vlad
 */
@WebServlet("/*")
public class FileSlaveServlet extends HttpServlet {
    
    
    // Logging code ////////////////////////////////////////////////////////////
    public static File logFile = new File("fileSlaveLog.txt");
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.mm.dd-hh:mm:ss");
    public static void writeToLog(String message)
    {
        synchronized(logFile)
        {
            try(FileWriter output = new FileWriter(logFile, true);
                    BufferedWriter out = new BufferedWriter(output))
            {
                out.append("[").append(sdf.format(new Date(System.currentTimeMillis()))).append("]|[").append(message).append("]\r\n").flush();
            } catch (IOException ex) {
                Logger.getLogger(FileSlaveServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            logFile.notifyAll();
        }
    }
    ////////////////////////////////////////////////////////////////////////////

    
    @Override
    public void init() throws ServletException {
        super.init(); 
        QueryProtocol.generateAdminKey();
    }

    @Override
    public void destroy() {
        super.destroy();
        QueryProtocol.sendUnregisterRequestToMaster(FileSlaveServlet.configParams.get("MasterURI"), 
                                                    FileSlaveServlet.configParams.get("requestsKey"), 
                                                    FileSlaveServlet.configParams.get("SlaveURI"));
    }
    
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////
    public static HashMap<String, String> configParams = new HashMap();
    public static int dbSize;
    public static void loadConfigParams(String path)
    {
        try {
            ConfigParamsDAO.loadConfigParams(path, configParams);
            updatedbSize();
        } catch (IOException ex) {
            FileSlaveServlet.writeToLog("<ERROR> FileSlaveServlet.loadConfigParams.loadConfigParams() : IOException(" + ex.getMessage() + ")");
            Logger.getLogger(FileSlaveServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void updatedbSize()
    {
    	SearchSystemDAO temp = new SearchSystemDAO();
    	dbSize = temp.getDBSize();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        QueryProtocol queryProtocol = new QueryProtocol();
        if(queryProtocol.validateRequest(request) == false)
        {
            //TODO: write invalid token json output
            response.setStatus(402);
            return;
        }

        UriUtils uri = new UriUtils(request.getRequestURI());
        Controller action = ControllerFactory.getInstance().getController(uri.getController());
        action.processRequest(request, response, uri);
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

  
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
