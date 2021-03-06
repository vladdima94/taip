/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import QueryProtocol.QueryProtocol;
import controller.Controller;
import controller.ControllerFactory;
import dao.Concrete.ConcreteConfigParamsDAO;
import dao.Factory.AbstractFactory;
import dao.Factory.DAOFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Exceptions.EntityAlreadyRegisteredException;
import utils.UriUtils;

/**
 *
 * @author Vlad
 */
public class FileMasterServlet extends HttpServlet {
    
    // Logging code ////////////////////////////////////////////////////////////
    public static File logFile = new File("fileSlaveLog.txt");
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.mm.dd-hh:mm:ss");
    public static void writeToLog(String message)
    {
        synchronized(logFile)
        {
            try(FileWriter output = new FileWriter(logFile, true);
                    BufferedWriter out = new BufferedWriter(output))
            {
                out.append("[").append(sdf.format(new Date(System.currentTimeMillis()))).append("]|[").append(message).append("]\r\n").flush();
            } catch (IOException ex) {
                Logger.getLogger(FileMasterServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            logFile.notifyAll();
        }
    }
    ////////////////////////////////////////////////////////////////////////////

    public static DAOFactory daoFactory = AbstractFactory.getInstance().getDAOFactoryInstance(AbstractFactory.JDBC_DAO_FACTORY);

    
    @Override
    public void init() throws ServletException {
        super.init(); 
        QueryProtocol.generateAdminKey();
    }

    @Override
    public void destroy() {
        super.destroy();
        //TODO: unregister FileSlave from FileMaster
    }
    
    public static HashMap<String, String> configParams = new HashMap();
    public static void loadConfigParams(String path)
    {
        try {
            ConcreteConfigParamsDAO.loadConfigParams(path, configParams);
        } catch (IOException ex) {
            FileMasterServlet.writeToLog("<ERROR> FileSlaveServlet.loadConfigParams.loadConfigParams() : IOException(" + ex.getMessage() + ")");
            Logger.getLogger(FileMasterServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        
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