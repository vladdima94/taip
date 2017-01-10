package controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import Exceptions.FileMasterNotConfiguredException;
import QueryProtocol.QueryProtocol;
import utils.UriUtils;
import utils.JSONUtils.JsonUtils;

public class UploadImageController extends Controller{

	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response, UriUtils uri) {
		
		JSONObject data = null;
		try {
			data = JsonUtils.readBody(request.getReader());
		} catch (IOException e) {
			UploadImageController.setQuickResponseMessage(400, "error", "IOException", response);
			e.printStackTrace();
		}
		
		if(data == null)
		{
			//TODO: invalid body
			UploadImageController.setQuickResponseMessage(400, "error", "Invalid Body", response);
			return;
		}
		
		//Select fileSlave with smallest DB
		QueryProtocol sender = new QueryProtocol();

		try {
			String respCode = sender.uploadImagesToSlaves(data);
			if(respCode == null)
			{
				Controller.setQuickResponseMessage(400, "error", "Failed to upload images to Slaves!", response);
				return;
			}
			Controller.setQuickResponseMessage(200, "success", respCode, response);
		} catch (ClassNotFoundException e) {
			Controller.setQuickResponseMessage(500, "error", "ClassNotFoundException", response);
//			e.printStackTrace();
		} catch (SQLException e) {
			Controller.setQuickResponseMessage(500, "error", "SQLException", response);
//			e.printStackTrace();
		} catch (FileMasterNotConfiguredException e) {
			Controller.setQuickResponseMessage(500, "error", "File Master not configured yet", response);
//			e.printStackTrace();
		} catch (Exception e) {
			Controller.setQuickResponseMessage(500, "error", "Exception", response);
//			e.printStackTrace();
		}
	}

}
