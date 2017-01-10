package tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONObject;
import org.junit.Test;

import QueryProtocol.QueryProtocol;
import utils.JSONUtils.JSONMapAdapter;
import utils.JSONUtils.JsonUtils;
import static tests.TestsConfig.*;

public class WorkflowTest4UnregisterSlave {

	@Test
	public void test5UnregisterSlaveSuccesfully() {
		try {
            URL url = new URL(FILE_MASTER_URI + "/unregisterSlave?key=" + FILE_SLAVE_TOKEN_TEST);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            
            JsonUtils requestBody = new JsonUtils();
            Map<String, String> data = new TreeMap();
            data.put("link", FILE_SLAVE_LINK_TEST);
            data.put("token", FILE_SLAVE_TOKEN_TEST);
            data.put("key", "vladdima");
            JSONMapAdapter dataWrapper = new JSONMapAdapter();
            dataWrapper.setData(data);
            requestBody.setJSONAdapter(dataWrapper);
            requestBody.setMessage("unregister");
            
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            requestBody.writeToOutput(wr);
            wr.flush();
            
            JSONObject responseJSON = JsonUtils.readBody(new InputStreamReader(conn.getInputStream()));
            String status = (String) responseJSON.get("status");
            if(status == null)
            {
	        	fail("Failed to unregister Slave to Master");
            }
            if(!status.equals("success"))
            {
	        	fail("Failed to unregister Slave to Master");
            }
        } catch (MalformedURLException ex) {
        	fail("Failed to unregister Slave to Master");
        } catch (IOException ex) {
        	fail("Failed to unregister Slave to Master");
        }
	}
	@Test
	public void test6UnregisterSlaveUnsuccesfully() {
		try {
	        URL url = new URL(FILE_MASTER_URI + "/unregisterlave?key=" + FILE_SLAVE_TOKEN_TEST);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setDoInput(true);
	        conn.setDoOutput(true);
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Accept", "application/json");
	        
	        JsonUtils requestBody = new JsonUtils();
	        Map<String, String> data = new TreeMap();
	        data.put("link", FILE_SLAVE_LINK_TEST);
	        data.put("token", FILE_SLAVE_TOKEN_TEST);
	        data.put("key", "vladdima");
	        JSONMapAdapter dataWrapper = new JSONMapAdapter();
	        dataWrapper.setData(data);
	        requestBody.setJSONAdapter(dataWrapper);
	        requestBody.setMessage("register");
	        
	        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	        requestBody.writeToOutput(wr);
	        wr.flush();
	        
	        JSONObject responseJSON = JsonUtils.readBody(new InputStreamReader(conn.getInputStream()));
	        System.out.println(responseJSON.toJSONString());
	        String status = (String) responseJSON.get("status");
	        if(status == null)
	        {
	        	fail("Failed to unregister Slave to Master");
	        }
	        if(!status.equals("success"))
	        {
	        	fail("Failed to unregister Slave to Master");
	        }
	    } catch (MalformedURLException ex) {
	    	fail("Failed to unregister Slave to Master");
	    } catch (IOException ex) {
	    	// HERE IS SUCCESS
	    }
	}
	

}
