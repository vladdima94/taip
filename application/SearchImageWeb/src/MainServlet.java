

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Controller.Controller;
import Controller.ControllerFactory;
import Utils.UriUtils;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/main/*")
@MultipartConfig
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public MainServlet() {
        // TODO Auto-generated constructor stub
    }

    
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
    {
    	UriUtils uri = new UriUtils(request.getRequestURI());
    	System.out.println(uri.getSubController());
    	Controller controller = ControllerFactory.getInstance().getController(uri.getSubController());
    	try {
			controller.processRequest(request, response, uri);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

}
