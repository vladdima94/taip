package aspects;

import Exceptions.InvalidJSONException;
import controller.Controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import javax.servlet.http.HttpServletResponse;
import servlet.FileSlaveServlet;


public aspect FileSlaveLoggingAspect {
	/*
	pointcut FileSlaveSearchFilesPointcut() : call(public void QueryProtocol.QueryProtocol.sendRegisterRequestToMaster(..));
	after () throwing (MalformedURLException ex) : FileSlaveSearchFilesPointcut()
	{
		Object [] args = thisJoinPoint.getArgs();
		FileSlaveServlet.writeToLog("<ERROR> QueryProtocol.sendRegisterRequestToMaster() : MalformedURLException(" + ex.getMessage() + ")");
		Controller.setQuickResponseMessage(500, "error", "Invalid MasterURI link", (HttpServletResponse)args[0]);
	}
	after () throwing (ProtocolException ex) : FileSlaveSearchFilesPointcut()
	{
		Object [] args = thisJoinPoint.getArgs();
        FileSlaveServlet.writeToLog("<ERROR> QueryProtocol.sendRegisterRequestToMaster() : ProtocolException(" + ex.getMessage() + ")");
		Controller.setQuickResponseMessage(500, "error", "Invalid http verb!", (HttpServletResponse)args[0]);
	}
	after () throwing (IOException ex) : FileSlaveSearchFilesPointcut()
	{
		Object [] args = thisJoinPoint.getArgs();
        FileSlaveServlet.writeToLog("<ERROR> QueryProtocol.sendRegisterRequestToMaster() : IOException(" + ex.getMessage() + ")");
		Controller.setQuickResponseMessage(500, "error", "Invalid Master Link!", (HttpServletResponse)args[0]);
	}
	after () throwing (InvalidJSONException ex) : FileSlaveSearchFilesPointcut()
	{
		Object [] args = thisJoinPoint.getArgs();
        FileSlaveServlet.writeToLog("<ERROR> QueryProtocol.sendRegisterRequestToMaster() : InvalidJSONException(" + ex.getMessage() + ")");
		Controller.setQuickResponseMessage(400, "error", "Invalid Response from Master!", (HttpServletResponse)args[0]);
	}
	
/*
	declare soft : Exception : execution(public void dao.QueryProtocolDAO.validateUserToken(..));
	pointcut validateUserTokenPointcut() : execution(public boolean dao.QueryProtocolDAO.validateUserToken(..));
	after () throwing (ClassNotFoundException ex) : validateUserTokenPointcut()
	{
		HttpServletResponse response = (HttpServletResponse)thisJoinPoint.getArgs()[4];
		FileSlaveServlet.writeToLog("<ERROR> QueryProtocol.checkUserKey.validateUserToken() : ClassNotFoundException(" + ex.getMessage() + ")");
		Controller.setQuickResponseMessage(500 , "error", "Internal Server Error. We are trying to fix it!", response);
	}	
	after () throwing (SQLException ex) : validateUserTokenPointcut()
	{
		HttpServletResponse response = (HttpServletResponse)thisJoinPoint.getArgs()[4];
		FileSlaveServlet.writeToLog("<ERROR> QueryProtocol.checkUserKey.validateUserToken() : SQLException(" + ex.getMessage() + ")");
		Controller.setQuickResponseMessage(500 , "error", "Internal Server Error. We are trying to fix it!", response);
	}
	

	declare soft : Exception : execution(public void QueryProtocol.QueryProtocol.unregisterEntity(..));
	pointcut unRegisterSlaveSQLExceptionPointcut() : execution(public void QueryProtocol.QueryProtocol.unregisterEntity(..));
	after () throwing (ClassNotFoundException ex) : unRegisterSlaveSQLExceptionPointcut()
	{
		HttpServletResponse response = (HttpServletResponse)thisJoinPoint.getArgs()[4];
		FileSlaveServlet.writeToLog("<ERROR> QueryProtocol.unregisterEntity.addTokenToDB() : ClassNotFoundException(" + ex.getMessage() + ")");
		Controller.setQuickResponseMessage(500 , "error", "Internal Server Error. We are trying to fix it!", response);
	}	
	after () throwing (SQLException ex) : unRegisterSlaveSQLExceptionPointcut()
	{
		HttpServletResponse response = (HttpServletResponse)thisJoinPoint.getArgs()[4];
		FileSlaveServlet.writeToLog("<ERROR> QueryProtocol.unregisterEntity.addTokenToDB() : SQLException(" + ex.getMessage() + ")");
		Controller.setQuickResponseMessage(500 , "error", "Internal Server Error. We are trying to fix it!", response);
	}*/
}
