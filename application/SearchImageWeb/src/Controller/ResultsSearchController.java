package Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import Utils.JsonUtils;
import Utils.UriUtils;

public class ResultsSearchController implements Controller{

	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response, UriUtils uri)
			throws ServletException, IOException {
		
		Part filePart = request.getPart("imageFile"); // Retrieves <input type="file" name="file">

		try {
            URL url = new URL("http://localhost:8080/FileMaster/search?token=0000");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "image/jpg");
            conn.setRequestProperty("Accept", "application/json");
            copyStream(filePart.getInputStream(), conn.getOutputStream());
            JSONObject responseJSON = JsonUtils.readBody(new InputStreamReader(conn.getInputStream()));
            JSONArray data = (JSONArray)responseJSON.get("data");
            if(data == null || data.size() < 1){
            	new ErrorController("Invalid response from FileMaster!").processRequest(request, response, uri);
            	return;
            }
            request.setAttribute("resultsList", data);
    		request.getRequestDispatcher("/WEB-INF/views/resultsPage.jsp").forward(request, response);
        } catch (MalformedURLException ex) {
        	new ErrorController("500 - Malformed Link").processRequest(request, response, uri);
        	ex.printStackTrace();
        } catch (IOException ex) {
        	new ErrorController("FileMaster not responding!").processRequest(request, response, uri);
        	ex.printStackTrace();
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
