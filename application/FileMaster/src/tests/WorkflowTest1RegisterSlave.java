package tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import static tests.TestsConfig.*;

import org.json.simple.JSONObject;
import org.junit.Test;

import utils.JSONUtils.JSONMapAdapter;
import utils.JSONUtils.JsonUtils;

public class WorkflowTest1RegisterSlave {

	@Test
	public void test1RegisterSlaveSuccessfully() {
		try{
			URL url = new URL(FILE_MASTER_URI + "/registerSlave?key=" + "vladdima");
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setDoInput(true);
	        conn.setDoOutput(true);
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Accept", "application/json");
	        
	        JsonUtils requestBody = new JsonUtils();
	        Map<String, String> data = new TreeMap();
	        data.put("link", FILE_SLAVE_LINK_TEST);
	        data.put("token", FILE_SLAVE_TOKEN_TEST);
	        data.put("maxDBSize", "1000");
	        data.put("currentDBSize", "0");
	        JSONMapAdapter dataWrapper = new JSONMapAdapter();
	        dataWrapper.setData(data);
	        requestBody.setJSONAdapter(dataWrapper);
	        requestBody.setMessage("register");
	        
	        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	        requestBody.writeToOutput(wr);
	        wr.flush();
	        
	        JSONObject responseJSON = JsonUtils.readBody(new InputStreamReader(conn.getInputStream()));
	        if(responseJSON == null)
	        {
	        	fail("Failed to register Slave to Master");
	        }
	        String status = (String) responseJSON.get("status");
	        if(status == null)
	        {
	        	fail("Failed to register Slave to Master");
	        }
	        if(!status.equals("success")){
	            fail("Failed to register Slave to Master");
	        }
        } catch (IOException e) {
            fail("Failed to register Slave to Master");
		}
	}
	@Test
	public void test2RegisterSlaveUnsuccessfully() {
		try{
			URL url = new URL(FILE_MASTER_URI + "/registerSe?key=" + FILE_SLAVE_TOKEN_TEST);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setDoInput(true);
	        conn.setDoOutput(true);
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Accept", "application/json");
	        
	        JsonUtils requestBody = new JsonUtils();
	        Map<String, String> data = new TreeMap();
	        data.put("link", FILE_SLAVE_LINK_TEST);
	        data.put("token", FILE_SLAVE_TOKEN_TEST);
	        data.put("maxDBSize", "1000");
	        data.put("currentDBSize", "0");
	        JSONMapAdapter dataWrapper = new JSONMapAdapter();
	        dataWrapper.setData(data);
	        requestBody.setJSONAdapter(dataWrapper);
	        requestBody.setMessage("register");
	        
	        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	        requestBody.writeToOutput(wr);
	        wr.flush();
	        
	        JSONObject responseJSON = JsonUtils.readBody(new InputStreamReader(conn.getInputStream()));
	        if(responseJSON == null)
	        {
	        	fail("Failed to register Slave to Master");
	        }
	        String status = (String) responseJSON.get("status");
	        if(status == null)
	        {
	        	fail("Failed to register Slave to Master");
	        }
	        else if(status.equals("success")){
	            fail("Failed to register Slave to Master");
	        }
        } catch (IOException e) {
            // HERE MUST ENTER BECAUSE INVALID LINK
		}
	}

}
