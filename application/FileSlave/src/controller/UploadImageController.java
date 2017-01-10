package controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import dao.SearchSystemDAO;
import dao.Factory.DAOFactory;
import servlet.FileSlaveServlet;
import utils.UriUtils;
import utils.JSONUtils.JsonUtils;

public class UploadImageController extends Controller{

	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response, UriUtils uri)
	{
		JSONObject jsonBody;
		try {
			jsonBody = JsonUtils.readBody(request.getReader());
			if(jsonBody == null)
			{
				Controller.setQuickResponseMessage(400, "error", "Invalid Request", response);
				System.out.println("[FILESLAVE] INVALID REQUEST");
				return;
			}
			
			JSONArray jsonImages = (JSONArray)jsonBody.get("data");
			if(jsonImages == null)
			{
				Controller.setQuickResponseMessage(400, "error", "Invalid Request, no data found", response);
				System.out.println("[FILESLAVE] INVALID REQUEST, no data found");
				return;
			}
			int size = jsonImages.size();
			SearchSystemDAO saveInData = (SearchSystemDAO)FileSlaveServlet.daoFactory.getDAOInstance(DAOFactory.SEARCH_SYSTEM_DAO);
			int successfullyUploaded = 0;
			try {
				successfullyUploaded = saveInData.uploadImageToDB(jsonImages);
			} catch (ClassNotFoundException ex) {
				Controller.setQuickResponseMessage(500, "error", "Server error, working to fix it!", response);
	            FileSlaveServlet.writeToLog("<ERROR> UploadImageController.processRequest() : ClassNotFoundException(" + ex.getMessage() + ")");
				ex.printStackTrace();
	            return;
			} catch (SQLException ex) {
				Controller.setQuickResponseMessage(500, "error", "Server error, working to fix it!", response);
	            FileSlaveServlet.writeToLog("<ERROR> UploadImageController.processRequest() : SQLException(" + ex.getMessage() + ")");
				ex.printStackTrace();
				return;
			}
			Controller.setQuickResponseMessage(200, "success", "successfully uploaded" + successfullyUploaded + " images", String.valueOf(successfullyUploaded), response);
		} catch (IOException e) {
			Controller.setQuickResponseMessage(500, "error", "IOException", response);
			e.printStackTrace();
		}
	}

}
