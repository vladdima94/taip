/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QueryProtocol;

import Exceptions.EntityAlreadyRegisteredException;
import Exceptions.FileMasterNotConfiguredException;
import controller.Controller;
import dao.QueryProtocolDAO;
import dao.Concrete.ConcreteQueryProtocolDAO;
import dao.Factory.DAOFactory;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
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
import servlet.FileMasterServlet;

/**
 *
 * @author Vlad
 */
public class QueryProtocol {
    private QueryProtocolDAO dataAccess = (QueryProtocolDAO) FileMasterServlet.daoFactory.getDAOInstance(DAOFactory.QUERY_PROTOCOL_DAO);
    
    public  List<Map<String, String>> sendRequestsToSlaves(HttpServletRequest req, HttpServletResponse resp, double [] queryData) throws FileMasterNotConfiguredException
    {
    	List<Map<String, String>> output = new LinkedList();
    	String contentType = req.getContentType();
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
            	sendRequestsThreads[i] = new Thread(new SendRequestToSlaveRunnable(req, row.getKey(), row.getValue(), contentType, output));
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
//            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (SQLException ex) {
            FileMasterServlet.writeToLog("<ERROR> QueryProtocol.sendRequestsToSlaves.getSlavesTokensPair() : SQLException(" + ex.getMessage() + ")");
//            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (NullPointerException ex) {
            FileMasterServlet.writeToLog("<ERROR> QueryProtocol.sendRequestsToSlaves.getSlavesTokensPair() : NullPointerException(" + ex.getMessage() + ")");
            Controller.setQuickResponseMessage(400, "error", "Load Master Config First!", resp);
//            Logger.getLogger(QueryProtocol.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        return output;
    }

    
    
    public static String generateEntityToken()
    {
        return new BigInteger(130, securedRandom).toString();
    }
    
    
    public void registerEntity(String entity, String token, String maxDBSize, String currentDBSize, HttpServletResponse response) throws EntityAlreadyRegisteredException, ClassNotFoundException, SQLException
    {
            dataAccess.addTokenToDB(entity, token, maxDBSize, currentDBSize);
    }
    public void unregisterEntity(String token) throws ClassNotFoundException, SQLException
    {
            dataAccess.removeTokenToDB(token);
    }

    
    public String uploadImagesToSlaves(JSONObject data) throws ClassNotFoundException, SQLException, FileMasterNotConfiguredException, Exception
    {
    	List<String []> slaves = dataAccess.getSlaveWithSmallestDB();
    	if(slaves == null) throw new SQLException("No Slave prezent in DB!");
    	JSONArray images = (JSONArray) data.get("data");
    	if(images == null) throw new Exception("Invalid JSON body!");
    	int numberOfAddedImages = images.size();
    	for(String [] slave : slaves)
    	{
    		while(dataAccess.checkIsUploading(slave[0]).equals("y"))
    		{
    			Thread.sleep(1000);
    		}
			int newSlaveDBSizeTemp = Integer.parseInt(slave[2]) + numberOfAddedImages;
			dataAccess.updateSlaveDBSize(slave[0], newSlaveDBSizeTemp, "y");
    		try{
				System.out.println("[FILEMASTER]Locked FileSlave for uploading");
				
				URL url = new URL(slave[0] + "/upload/?token=" + slave[1]);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				conn.setRequestProperty("Content-Type", "application/json");
				try(OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream()))
				{
					out.write(data.toString());
					out.flush();
					out.close();
				}
				
				JSONObject json = JsonUtils.readBody(new InputStreamReader(conn.getInputStream()));
				System.out.println(json.toJSONString());
				String status = (String)json.get("status");
				String succesfullyUploadedImages = (String)json.get("data");
				if(status != null && status.equals("success"))
				{
					// Can be invalid images that Slave ignors them, so we must check if the number of images sent are the same with the images added by the Slave
					if(succesfullyUploadedImages != null)
					{
						int newSlaveDBSizeCurret = Integer.parseInt(succesfullyUploadedImages);
						if(newSlaveDBSizeCurret != newSlaveDBSizeTemp)
						{
							// If not, update Master DB
							dataAccess.updateSlaveDBSize(slave[0], newSlaveDBSizeCurret, "n");
							System.out.println("[FILEMASTER]Unlocked FileSlave for uploading");
						}
					}
					return (String)json.get("message");
				}
				else
				{
					// Error, restore Slave DB size to old value;
					dataAccess.updateSlaveDBSize(slave[0], Integer.parseInt(slave[2]), "n");
					System.out.println("[FILEMASTER]Unlocked FileSlave for uploading");
					return (String)json.get("message");
				}
    		}catch(IOException ex){
    	    	return null;
	    	}
    	}
    	return null;
	}
    
    
    public static boolean checkAdminKey(String key)
    {
    	return key != null && adminKey != null && key.equals(adminKey);
    }
    public byte checkUserKey(String key)
    {
    	try{
    		if(dataAccess.validateUserToken(key)) return 1;
    		else return 0;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		return -1;
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
            System.out.printf("[FILEMASTER] Admin Key[%s]\r\n", adminKey);
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
	public HttpServletRequest req;
	
	public SendRequestToSlaveRunnable(HttpServletRequest req, String link, String token, String contentType, List<Map<String, String>> imageResults)
	{
		this.link = link;
		this.token = token;
		this.contentType = contentType;
		this.imageResults = imageResults;
		this.req = req;
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
            copyStream(req.getInputStream(), conn.getOutputStream());
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
	
	private void copyStream(InputStream input, OutputStream output)
		    throws IOException
		{
		    byte[] buffer = new byte[1024];
		    int bytesRead;
		    while ((bytesRead = input.read(buffer)) != -1)
		    {
		        output.write(buffer, 0, bytesRead);
		    }
		}
}
