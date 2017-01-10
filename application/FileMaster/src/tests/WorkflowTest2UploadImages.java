package tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONObject;
import org.junit.Test;

import utils.JSONUtils.JsonUtils;
import static tests.TestsConfig.*;

public class WorkflowTest2UploadImages {

	@Test
	public void test3UploadImages() {
		try {
            URL url = new URL(FILE_MASTER_URI + "/upload?key=" + "0000");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(3);
            OutputStreamWriter write = new OutputStreamWriter(conn.getOutputStream());
            write.write(jsonDATA);
            write.flush();
            
            JSONObject responseJSON = JsonUtils.readBody(new InputStreamReader(conn.getInputStream()));
            if(responseJSON == null)
	        {
	        	fail("Failed to Upload Images");
	        }
	        String status = (String) responseJSON.get("status");
	        if(status == null)
	        {
	        	fail("Failed to Upload Images (invalid JSONResponse)");
	        }
	        if(!status.equals("success")){
	        	fail("Failed to Upload Images");
	        }
        } catch (MalformedURLException ex) {
        	fail("Failed to Upload Images");
        } catch (IOException ex) {
        	fail("Failed to Upload Images: " + ex.getMessage());
        }
	}

}
