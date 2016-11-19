/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlet.FileSlaveServlet;
import utils.UriUtils;

/**
 *
 * @author Vlad
 */
public class PrepareServletController implements Controller{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, UriUtils uri) {
        String configPath = request.getParameter("configFile");
        String unregister = request.getParameter("unregister");
        if(unregister != null && unregister.equals("true"))
        {
            QueryProtocol.QueryProtocol.sendUnregisterRequestToMaster(FileSlaveServlet.configParams.get("MasterURI"), 
                                                        FileSlaveServlet.configParams.get("requestsKey"), 
                                                        FileSlaveServlet.configParams.get("SlaveURI"));
            return;
        }
        FileSlaveServlet.loadConfigParams(configPath);
        QueryProtocol.QueryProtocol.acceptQueriesKey = QueryProtocol.QueryProtocol.generateEntityToken();
        QueryProtocol.QueryProtocol.sendRegisterRequestToMaster(response, FileSlaveServlet.configParams.get("MasterURI"), 
		                                                                QueryProtocol.QueryProtocol.acceptQueriesKey, 
		                                                                FileSlaveServlet.configParams.get("SlaveURI"),
		                                                                FileSlaveServlet.configParams.get("maxDBSize"));
    }
    
}
