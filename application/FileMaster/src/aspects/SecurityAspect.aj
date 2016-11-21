package aspects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import QueryProtocol.QueryProtocol;
import controller.Controller;
import utils.UriUtils;

public aspect SecurityAspect {

	pointcut adminSecurityCheck() : call (public void controller.Controller.processRequest(HttpServletRequest,HttpServletResponse,UriUtils)) && target(controller.PrepareServletController);
	pointcut userSecurityCheck() :  call (public void controller.Controller.processRequest(HttpServletRequest,HttpServletResponse,UriUtils)) && target(controller.SearchController);
	pointcut registerSlaveSecurityCheck() : call(public void controller.Controller.processRequest(HttpServletRequest,HttpServletResponse,UriUtils)) && target(controller.RegisterSlaveController);
	
	void around() : adminSecurityCheck()
	{
		System.out.println("adminSecurityCheck Security Check");
		Object [] args = thisJoinPoint.getArgs();
		String token = ((HttpServletRequest)args[0]).getParameter("token");
		System.out.printf("[FILEMASTER] Register Slave Key [%s]\r\n", token);
		
		if(QueryProtocol.checkAdminKey(token)) proceed();
		else
		{
			Controller.setQuickResponseMessage(404, "error", "Invalid token.", (HttpServletResponse)args[1]);
		}
	}

	void around() : userSecurityCheck()
	{
		System.out.println("userSecurityCheck Security Check");
		Object [] args = thisJoinPoint.getArgs();
		String token = ((HttpServletRequest)args[0]).getParameter("token");
		if(token == null)return;
		QueryProtocol test = new QueryProtocol();
		byte testResult = test.checkUserKey(token);
		if(testResult == 1) proceed();
		else if(testResult == 0)
		{
			Controller.setQuickResponseMessage(404, "error", "Invalid token.", (HttpServletResponse)args[1]);
		}
		else if(testResult == -1)
		{
			Controller.setQuickResponseMessage(500 , "error", "Internal Server Error. We are trying to fix it!", (HttpServletResponse)args[1]);
		}
		
	}
	void around() : registerSlaveSecurityCheck()
	{
		System.out.println("registerSlaveSecurityCheck Security Check");
		Object [] args = thisJoinPoint.getArgs();
		String token = ((HttpServletRequest)args[0]).getParameter("key");
		System.out.printf("[FILEMASTER] Register Slave Key [%s]\r\n", token);
		if(QueryProtocol.getUserKey().equals(token)) proceed();
		else
		{
			Controller.setQuickResponseMessage(404, "error", "Invalid register token.", (HttpServletResponse)args[1]);
		}
	}

}
