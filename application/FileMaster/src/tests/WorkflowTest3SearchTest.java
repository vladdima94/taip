package tests;

import static org.junit.Assert.*;
import static tests.TestsConfig.FILE_MASTER_URI;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.json.simple.JSONObject;
import org.junit.Test;

import utils.JSONUtils.JsonUtils;

public class WorkflowTest3SearchTest {

	@Test
	public void test4SearchTest() {
		try {
            URL url = new URL(FILE_MASTER_URI + "/search?token=" + "0000");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "image/jpeg");
            OutputStream write = conn.getOutputStream();
            BufferedImage testImage = ImageIO.read(new File("C:\\Users\\Vlad\\Desktop\\test.jpg"));
            write.write(((DataBufferByte) testImage.getData().getDataBuffer()).getData());
            write.flush();
            write.close();
            
            JSONObject responseJSON = JsonUtils.readBody(new InputStreamReader(conn.getInputStream()));
            System.out.println(responseJSON.toJSONString());
            if(responseJSON == null)
	        {
	        	fail("Failed to Search for Images");
	        }
	        String status = (String) responseJSON.get("status");
	        if(status == null)
	        {
	        	fail("Failed to Search for Images (invalid JSONResponse)");
	        }
	        if(!status.equals("success")){
	        	fail("Failed to Search for Images");
	        }
        } catch (MalformedURLException ex) {
        	fail("Failed to Search for Images");
        } catch (IOException ex) {
        	fail("Failed to Search for Images: " + ex.getMessage());
        }
	}
	
}
