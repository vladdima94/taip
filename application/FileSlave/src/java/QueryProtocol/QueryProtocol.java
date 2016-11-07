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
import servlet.FileSlaveServlet;

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
            FileSlaveServlet.writeToLog("<ERROR> QueryProtocol.sendRequestsToSlaves.getSlavesTokensPair() : ClassNotFoundException(" + ex.getMessage() + ")");
            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            FileSlaveServlet.writeToLog("<ERROR> QueryProtocol.sendRequestsToSlaves.getSlavesTokensPair() : SQLException(" + ex.getMessage() + ")");
            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean validateRequest(HttpServletRequest request)
    {
//        return true;
        String entity = request.getRequestURI();
        String token = request.getParameter("token");
        if(token == null) return false;
        else if(token.equals(adminKey))return true;
        try {
            return dataAccess.validateToken(entity, token);
        } catch (SQLException ex) {
            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
            FileSlaveServlet.writeToLog("<ERROR> QueryProtocol.validateRequest.validateToken() : SQLException(" + ex.getMessage() + ")");
        } catch (ClassNotFoundException ex) {
            FileSlaveServlet.writeToLog("<ERROR> QueryProtocol.validateRequest.validateToken() : ClassNotFoundException(" + ex.getMessage() + ")");
            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public String generateEntityToken(String slave)
    {
        String token = null;
        //TODO: generate user token and save it to DB using dataAccess;

        return token;
    }
    
    
    public void registerEntity(String entity, String token)
    {
        try {
            dataAccess.addTokenToDB(entity, token);
        } catch (ClassNotFoundException ex) {
            FileSlaveServlet.writeToLog("<ERROR> QueryProtocol.registerEntity.addTokenToDB() : ClassNotFoundException(" + ex.getMessage() + ")");
            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            FileSlaveServlet.writeToLog("<ERROR> QueryProtocol.registerEntity.addTokenToDB() : SQLException(" + ex.getMessage() + ")");
            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static String adminKey;
    public static void generateAdminKey()
    {
        try(FileWriter output = new FileWriter(System.getProperty("user.dir") + "\\SlaveAdminKey.txt");
                BufferedWriter out = new BufferedWriter(output))
        {
            adminKey = new BigInteger(130, new SecureRandom()).toString();
            out.append(adminKey).flush();
        } catch (IOException ex) {
            FileSlaveServlet.writeToLog("<WARNING> QueryProtocol.generateAdminKey() : IOException(Failed to write key to adminKey.txt)");
            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
