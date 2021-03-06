package aspects;

import Exceptions.EntityAlreadyRegisteredException;
import QueryProtocol.QueryProtocol;
import controller.Controller;

import java.sql.SQLException;
import javax.servlet.http.HttpServletResponse;
import servlet.FileMasterServlet;


public aspect FileMasterLoggingAspect {
	

	declare soft : Exception : execution(public void QueryProtocol.QueryProtocol.registerEntity(..));
	pointcut FileMasterRegisterSlaveSQLExceptionPointcut() : execution(public void QueryProtocol.QueryProtocol.registerEntity(..));
	after () returning : FileMasterRegisterSlaveSQLExceptionPointcut()
	{
		 Object [] args = thisJoinPoint.getArgs();
		 FileMasterServlet.writeToLog("<STATUS> Succesully registered FileSlave [" + args[0] + "] with token [" + args[1] + "]");
		 Controller.setQuickResponseMessage(200 , "success", "Internal Server Error. We are trying to fix it!", (HttpServletResponse)args[4]);
	}
	after () throwing (SQLException ex) : FileMasterRegisterSlaveSQLExceptionPointcut()
	{
		HttpServletResponse response = (HttpServletResponse)thisJoinPoint.getArgs()[4];
		FileMasterServlet.writeToLog("<ERROR> QueryProtocol.registerEntity.addTokenToDB() : SQLException(" + ex.getMessage() + ")");
		Controller.setQuickResponseMessage(500 , "error", "Internal Server Error. We are trying to fix it!", response);
	}	
	after () throwing (EntityAlreadyRegisteredException ex) : FileMasterRegisterSlaveSQLExceptionPointcut()
	{
		HttpServletResponse response = (HttpServletResponse)thisJoinPoint.getArgs()[4];
		FileMasterServlet.writeToLog("<ERROR> QueryProtocol.registerEntity.addTokenToDB() : EntityAlreadyRegisteredException(" + ex.getMessage() + ")");
		Controller.setQuickResponseMessage(400 , "error", "link or token not found in data JSON", response);
	}	
	after () throwing (ClassNotFoundException ex) : FileMasterRegisterSlaveSQLExceptionPointcut()
	{
		HttpServletResponse response = (HttpServletResponse)thisJoinPoint.getArgs()[4];
		FileMasterServlet.writeToLog("<ERROR> QueryProtocol.registerEntity.addTokenToDB() : ClassNotFoundException(" + ex.getMessage() + ")");
		Controller.setQuickResponseMessage(500 , "error", "Internal Server Error. We are trying to fix it!", response);
	}	
	after () throwing (NullPointerException ex) : FileMasterRegisterSlaveSQLExceptionPointcut()
	{
		HttpServletResponse response = (HttpServletResponse)thisJoinPoint.getArgs()[4];
		Controller.setQuickResponseMessage(400 , "error", "Load Config First!", response);
	}
	

	declare soft : Exception : execution(public void dao.QueryProtocolDAO.validateUserToken(..));
	pointcut FileMasterValidateUserTokenPointcut() : execution(public boolean dao.QueryProtocolDAO.validateUserToken(..));
	after () throwing (ClassNotFoundException ex) : FileMasterValidateUserTokenPointcut()
	{
		HttpServletResponse response = (HttpServletResponse)thisJoinPoint.getArgs()[4];
		FileMasterServlet.writeToLog("<ERROR> QueryProtocol.checkUserKey.validateUserToken() : ClassNotFoundException(" + ex.getMessage() + ")");
		Controller.setQuickResponseMessage(500 , "error", "Internal Server Error. We are trying to fix it!", response);
	}	
	after () throwing (SQLException ex) : FileMasterValidateUserTokenPointcut()
	{
		HttpServletResponse response = (HttpServletResponse)thisJoinPoint.getArgs()[4];
		FileMasterServlet.writeToLog("<ERROR> QueryProtocol.checkUserKey.validateUserToken() : SQLException(" + ex.getMessage() + ")");
		Controller.setQuickResponseMessage(500 , "error", "Internal Server Error. We are trying to fix it!", response);
	}
	

	declare soft : Exception : execution(public void QueryProtocol.QueryProtocol.unregisterEntity(..));
	pointcut FileMasterUnregisterSlaveSQLExceptionPointcut() : execution(public void QueryProtocol.QueryProtocol.unregisterEntity(..));
	after () throwing (ClassNotFoundException ex) : FileMasterUnregisterSlaveSQLExceptionPointcut()
	{
		HttpServletResponse response = (HttpServletResponse)thisJoinPoint.getArgs()[4];
		FileMasterServlet.writeToLog("<ERROR> QueryProtocol.unregisterEntity.addTokenToDB() : ClassNotFoundException(" + ex.getMessage() + ")");
		Controller.setQuickResponseMessage(500 , "error", "Internal Server Error. We are trying to fix it!", response);
	}	
	after () throwing (SQLException ex) : FileMasterUnregisterSlaveSQLExceptionPointcut()
	{
		HttpServletResponse response = (HttpServletResponse)thisJoinPoint.getArgs()[4];
		FileMasterServlet.writeToLog("<ERROR> QueryProtocol.unregisterEntity.addTokenToDB() : SQLException(" + ex.getMessage() + ")");
		Controller.setQuickResponseMessage(500 , "error", "Internal Server Error. We are trying to fix it!", response);
	}	
	after () throwing (EntityAlreadyRegisteredException ex) : FileMasterUnregisterSlaveSQLExceptionPointcut()
	{
		HttpServletResponse response = (HttpServletResponse)thisJoinPoint.getArgs()[4];
		FileMasterServlet.writeToLog("<ERROR> QueryProtocol.unregisterEntity.addTokenToDB() : EntityAlreadyRegisteredException(" + ex.getMessage() + ")");
		Controller.setQuickResponseMessage(400 , "error", "FileSlave already registered!", response);
	}
	
}
