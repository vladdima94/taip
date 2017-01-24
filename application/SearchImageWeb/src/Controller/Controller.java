package Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Utils.UriUtils;

public interface Controller {
	public void processRequest(HttpServletRequest request, HttpServletResponse response, UriUtils uri) throws ServletException, IOException ;
}
