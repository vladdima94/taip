package Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Utils.UriUtils;

public class ErrorController implements Controller{

	private String message;
	public ErrorController(String message)
	{
		this.message = message;
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response, UriUtils uri)  throws ServletException, IOException {
		request.setAttribute("message", this.message);
		request.getRequestDispatcher("/WEB-INF/views/errorPage.jsp").forward(request, response);
	}

}
