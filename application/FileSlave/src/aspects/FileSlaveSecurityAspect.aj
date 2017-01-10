package aspects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import QueryProtocol.QueryProtocol;
import controller.Controller;
import utils.UriUtils;

public aspect FileSlaveSecurityAspect {
/*
	pointcut FileSlaveAdminSecurityCheck() : call (public void controller.Controller.processRequest(HttpServletRequest,HttpServletResponse,UriUtils)) && 
								    target(controller.PrepareServletController);
	pointcut FileSlaveTokenSecurityCheck() : call (public void controller.Controller.processRequest(HttpServletRequest,HttpServletResponse,UriUtils)) && 
								    (target(controller.SearchController) || target(controller.UploadImageController));
	
	void around() : FileSlaveAdminSecurityCheck()
	{
		System.out.println("[FILESLAVE] adminSecurityCheck Security Check");
		Object [] args = thisJoinPoint.getArgs();
		String token = ((HttpServletRequest)args[0]).getParameter("token");
		
		if(QueryProtocol.checkAdminKey(token)) proceed();
		else
		{
			Controller.setQuickResponseMessage(404, "error", "Invalid token.", (HttpServletResponse)args[1]);
		}
	}
	
	void around() : FileSlaveTokenSecurityCheck()
	{
		System.out.println("[FILESLAVE] tokenSecurityCheck Security Check");
		Object [] args = thisJoinPoint.getArgs();
		String token = ((HttpServletRequest)args[0]).getParameter("token");
		
		if(QueryProtocol.checkAcceptQueriesKey(token)) proceed();
		else
		{
			Controller.setQuickResponseMessage(404, "error", "Invalid token.", (HttpServletResponse)args[1]);
		}
	}*/
}
