/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QueryProtocol;

import Exceptions.EntityAlreadyRegisteredException;
import dao.QueryProtocolDAO;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import servlet.FileMasterServlet;
import utils.JSONUtils.JsonUtils;

/**
 *
 * @author Vlad
 */
public class QueryProtocol {
    private QueryProtocolDAO dataAccess = new QueryProtocolDAO();
    
    public  List<Map<String, String>> sendRequestsToSlaves(HttpServletRequest req, HttpServletResponse resp, double [] queryData)
    {
    	List<Map<String, String>> output = new LinkedList();
    	String contentType = resp.getContentType();
        try {
            Map<String, String> slavesTokensPair = dataAccess.getSlavesTokensPair();
            int numberOfSlaves = slavesTokensPair.size();
            if(numberOfSlaves == 0)
            {
            	FileMasterServlet.writeToLog("<WARNING> QueryProtocol.sendRequestsToSlaves() : No Slaves registered!");
            	return output;
            }
            Thread [] sendRequestsThreads = new Thread[numberOfSlaves];
            int i = 0;
            for(Map.Entry<String, String> row : slavesTokensPair.entrySet())
            {
            	sendRequestsThreads[i] = new Thread(new SendRequestToSlaveRunnable(row.getKey(), row.getValue(), contentType, output));
            	sendRequestsThreads[i++].start();
            }
            for(Thread th : sendRequestsThreads)
            {
            	try {
					th.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        } catch (ClassNotFoundException ex) {
            FileMasterServlet.writeToLog("<ERROR> QueryProtocol.sendRequestsToSlaves.getSlavesTokensPair() : ClassNotFoundException(" + ex.getMessage() + ")");
            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            FileMasterServlet.writeToLog("<ERROR> QueryProtocol.sendRequestsToSlaves.getSlavesTokensPair() : SQLException(" + ex.getMessage() + ")");
            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return output;
    }
    
    
    
    public boolean validateRequest(HttpServletRequest request, String controller)
    {
        if(controller == null) return false;
        String token = request.getParameter("key");
        if(controller.equals("registerSlave"))
        {
            return token != null && token.equals(userKey);
        }
        else if(controller.equals("unregisterSlave"))
        {
            try {
                return this.dataAccess.validateSlaveToken(token);
            } catch (ClassNotFoundException ex) {
                FileMasterServlet.writeToLog("<ERROR> QueryProtocol.validateRequest() : ClassNotFoundException(" + ex.getMessage() + ")");
                return false;
            } catch (SQLException ex) {
                FileMasterServlet.writeToLog("<ERROR> QueryProtocol.validateRequest() : SQLException(" + ex.getMessage() + ")");
                return false;
            }
        }
        else
        {
            
        }
        //TODO: add support for users query
        return true;

    }
    
    
    public static String generateEntityToken()
    {
        return new BigInteger(130, securedRandom).toString();
    }
    
    
    public void registerEntity(String entity, String token, String maxDBSize, String currentDBSize) throws EntityAlreadyRegisteredException, ClassNotFoundException, SQLException
    {
            dataAccess.addTokenToDB(entity, token, maxDBSize, currentDBSize);
    }
    public void unregisterEntity(String token) throws EntityAlreadyRegisteredException
    {
        try {
            dataAccess.removeTokenToDB(token);
        } catch (ClassNotFoundException ex) {
            FileMasterServlet.writeToLog("<ERROR> QueryProtocol.unregisterEntity.addTokenToDB() : ClassNotFoundException(" + ex.getMessage() + ")");
            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            FileMasterServlet.writeToLog("<ERROR> QueryProtocol.unregisterEntity.addTokenToDB() : SQLException(" + ex.getMessage() + ")");
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
            System.out.printf("Admin Key[%s]\r\n", adminKey);
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




class SendRequestToSlaveRunnable implements Runnable
{
	public String link;
	public String token;
	public String contentType;
	public List<Map<String, String>> imageResults;
	
	public SendRequestToSlaveRunnable(String link, String token, String contentType, List<Map<String, String>> imageResults)
	{
		this.link = link;
		this.token = token;
		this.contentType = contentType;
		this.imageResults = imageResults;
	}

	@Override
	public void run() {
		try {
            URL url = new URL(this.link + "/search?key=" + this.token);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestProperty("Accept", "application/json");
            JSONObject responseJSON = JsonUtils.readBody(new InputStreamReader(conn.getInputStream()));
            if(responseJSON != null)
            {
            	JSONArray data = (JSONArray)responseJSON.get("data");
            	if(data == null)
            	{
            		FileMasterServlet.writeToLog("<WARNING> QueryProtocol.SendRequestToSlaveRunnable.run() : FileSlave [" + this.link + "] sent invalid response (no data found)!");
            	}
            	int size = data.size();
            	for(int i = 0; i < size; ++i)
            	{
            		JSONObject imageData = (JSONObject) data.get(i);
            		String link = (String) imageData.get("link");
            		String similarity = (String) imageData.get("similarity");
            		if(link == null)continue;
            		Map<String, String> temp = new HashMap();
            		temp.put("link", link);temp.put("similarity", similarity);
            		this.imageResults.add(temp);
            	}
            }
            else
            {
                FileMasterServlet.writeToLog("<WARNING> QueryProtocol.SendRequestToSlaveRunnable.run() : FileSlave [" + this.link + "] sent invalid response!");
            }
        } catch (MalformedURLException ex) {
            FileMasterServlet.writeToLog("<ERROR> QueryProtocol.SendRequestToSlaveRunnable.run() : MalformedURLException(" + ex.getMessage() + ")");
        } catch (IOException ex) {
        	FileMasterServlet.writeToLog("<ERROR> QueryProtocol.SendRequestToSlaveRunnable.run() : IOException(" + ex.getMessage() + ")");
        }
	}
}
