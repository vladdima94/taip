/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QueryProtocol;

import dao.QueryProtocolDAO;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import servlet.FileSlaveServlet;
import utils.JSONUtils.JSONMapAdapter;
import utils.JSONUtils.JSONStringAdapter;
import utils.JSONUtils.JsonUtils;

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
            FileSlaveServlet.writeToLog("<ERROR> QueryProtocol.registerEntity.addTokenToDB() : ClassNotFoundException(" + ex.getMessage() + ")");
            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            FileSlaveServlet.writeToLog("<ERROR> QueryProtocol.registerEntity.addTokenToDB() : SQLException(" + ex.getMessage() + ")");
            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void sendRegisterRequestToMaster(HttpServletResponse response, String entity, String token, String link, String maxDBSize)
    {
        if(entity == null || token == null || link == null)
        {
            FileSlaveServlet.writeToLog("<ERROR> QueryProtocol.sendRegisterRequestToMaster() : NULL MasterURI or SlaveURI in config File or invalid generated token.");
        }
        try {
            URL url = new URL(entity + "/registerSlave?key=" + QueryProtocol.userRegistrationKey);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            
            JsonUtils requestBody = new JsonUtils();
            Map<String, String> data = new TreeMap();
            data.put("link", link);
            data.put("token", token);
            data.put("maxDBSize", maxDBSize);
            data.put("currentDBSize", String.valueOf(FileSlaveServlet.dbSize));
            JSONMapAdapter dataWrapper = new JSONMapAdapter();
            dataWrapper.setData(data);
            requestBody.setJSONAdapter(dataWrapper);
            requestBody.setMessage("register");
            
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            requestBody.writeToOutput(wr);
            wr.flush();
            
            JSONObject responseJSON = JsonUtils.readBody(new InputStreamReader(conn.getInputStream()));
            String status = (String) responseJSON.get("status");
            if(status == null)
            {
                FileSlaveServlet.writeToLog("<STATUS> Failed to register Slave to Master: [Invalid response from Master]");
                return;
            }
            if(status.equals("success")){
                FileSlaveServlet.writeToLog("<STATUS> Successfully registered Slave to Master key[" + token + "]");
                FileSlaveServlet.configParams.put("requestsKey", token);
                response.setStatus(200);
                PrintWriter writer = response.getWriter();
                writer.append("Success").flush();
            }
            else{
                FileSlaveServlet.writeToLog("<STATUS> Failed to registered Slave to Master key[" + token + "]");
                response.setStatus(400);
                PrintWriter writer = response.getWriter();
                writer.append(" Failed to registered Slave to Master key").flush();
            }
        } catch (MalformedURLException ex) {
            FileSlaveServlet.writeToLog("<ERROR> QueryProtocol.sendRegisterRequestToMaster() : MalformedURLException(" + ex.getMessage() + ")");
        } catch (IOException ex) {
            FileSlaveServlet.writeToLog("<ERROR> QueryProtocol.sendRegisterRequestToMaster() : IOException(" + ex.getMessage() + ")");
        }
    }
    public static void sendUnregisterRequestToMaster(String entity, String token, String link)
    {
        if(entity == null || token == null || link == null)
        {
            FileSlaveServlet.writeToLog("<ERROR> QueryProtocol.sendUnregisterRequestToMaster() : NULL MasterURI or SlaveURI in config File or invalid generated token.");
        }
        try {
            URL url = new URL(entity + "/unregisterSlave?key=" + token);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            
            JsonUtils requestBody = new JsonUtils();
            Map<String, String> data = new TreeMap();
            data.put("link", link);
            data.put("token", token);
            data.put("key", QueryProtocol.userRegistrationKey);
            JSONMapAdapter dataWrapper = new JSONMapAdapter();
            dataWrapper.setData(data);
            requestBody.setJSONAdapter(dataWrapper);
            requestBody.setMessage("register");
            
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            requestBody.writeToOutput(wr);
            wr.flush();
            
            JSONObject responseJSON = JsonUtils.readBody(new InputStreamReader(conn.getInputStream()));
            String status = (String) responseJSON.get("status");
            if(status == null)
            {
                FileSlaveServlet.writeToLog("<STATUS> Failed to unregister Slave to Master: [Invalid response from Master]");
                return;
            }
            if(status.equals("success"))FileSlaveServlet.writeToLog("<STATUS> Successfully unregistered Slave to Master.");
            else FileSlaveServlet.writeToLog("<STATUS> Failed to unregistered Slave to Master.");
        } catch (MalformedURLException ex) {
            FileSlaveServlet.writeToLog("<ERROR> QueryProtocol.sendUnregisterRequestToMaster() : MalformedURLException(" + ex.getMessage() + ")");
        } catch (IOException ex) {
            FileSlaveServlet.writeToLog("<ERROR> QueryProtocol.sendUnregisterRequestToMaster() : IOException(" + ex.getMessage() + ")");
        }
    }
    
    
    public static String acceptQueriesKey;
    private static String userRegistrationKey = "vladdima";
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
            FileSlaveServlet.writeToLog("<WARNING> QueryProtocol.generateAdminKey() : IOException(Failed to write key to adminKey.txt)");
            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static String getUserRegistrationKey()
    {
        return userRegistrationKey;
    }
    public static void setUserRegistrationKey(String newUserKey)
    {
        userRegistrationKey = newUserKey;
    }
}
