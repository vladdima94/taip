/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QueryProtocol;

import dao.QueryProtocolDAO;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlet.FileMasterServlet;

/**
 *
 * @author Vlad
 */
public class QueryProtocol {
    private QueryProtocolDAO dataAccess = new QueryProtocolDAO();
    
    public void sendRequestsToSlaves(HttpServletRequest req, HttpServletResponse resp, double [] queryData)
    {
        try {
            //TODO: get slaves-tokens from DB and send data to them
            Map<String, String> slavesTokensPair = dataAccess.getSlavesTokensPair();
        } catch (ClassNotFoundException ex) {
            FileMasterServlet.writeToLog("<ERROR> QueryProtocol.sendRequestsToSlaves.getSlavesTokensPair() : ClassNotFoundException(" + ex.getMessage() + ")");
            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            FileMasterServlet.writeToLog("<ERROR> QueryProtocol.sendRequestsToSlaves.getSlavesTokensPair() : SQLException(" + ex.getMessage() + ")");
            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean validateRequest(HttpServletRequest request)
    {
//
//    
        return true;
//        String entity = request.getRequestURI();
//        String token = request.getParameter("token");
//        if(token == null) return false;
//        else if(token.equals(adminKey))return true;
//        try {
//            return dataAccess.validateToken(entity, token);
//        } catch (SQLException ex) {
//            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
//            FileMasterServlet.writeToLog("<ERROR> QueryProtocol.validateRequest.validateToken() : SQLException(" + ex.getMessage() + ")");
//        } catch (ClassNotFoundException ex) {
//            FileMasterServlet.writeToLog("<ERROR> QueryProtocol.validateRequest.validateToken() : ClassNotFoundException(" + ex.getMessage() + ")");
//            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return false;
    }
    
    
    public static String generateEntityToken()
    {
        return new BigInteger(130, securedRandom).toString();
    }
    
    
    public void registerEntity(String entity, String token)
    {
        try {
            dataAccess.addTokenToDB(entity, token);
        } catch (ClassNotFoundException ex) {
            FileMasterServlet.writeToLog("<ERROR> QueryProtocol.registerEntity.addTokenToDB() : ClassNotFoundException(" + ex.getMessage() + ")");
            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            FileMasterServlet.writeToLog("<ERROR> QueryProtocol.registerEntity.addTokenToDB() : SQLException(" + ex.getMessage() + ")");
            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private static String userKey = "vladdima";
    private static String adminKey;
    private static final SecureRandom securedRandom = new SecureRandom();
    public static void generateAdminKey()
    {
        try(FileWriter output = new FileWriter(System.getProperty("user.dir") + "\\MasterAdminKey.txt");
                BufferedWriter out = new BufferedWriter(output))
        {
            adminKey = new BigInteger(130, securedRandom).toString();
            out.append(adminKey).flush();
        } catch (IOException ex) {
            FileMasterServlet.writeToLog("<WARNING> QueryProtocol.generateAdminKey() : IOException(Failed to write key to adminKey.txt)");
            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static String getUserKey()
    {
        return userKey;
    }
    public static void setUserKey(String newUserKey)
    {
        userKey = newUserKey;
    }
}
