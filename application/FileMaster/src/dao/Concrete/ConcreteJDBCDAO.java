package dao.Concrete;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import Exceptions.FileMasterNotConfiguredException;
import dao.DAO;
import servlet.FileMasterServlet;

public abstract class ConcreteJDBCDAO implements DAO{
	  protected Connection connectToDatabase() throws ClassNotFoundException, SQLException, FileMasterNotConfiguredException
	  {
		  try{
		      Class.forName(FileMasterServlet.configParams.get("DBdriver"));
		      Connection conn = DriverManager.getConnection(FileMasterServlet.configParams.get("DBlocation"),
		                                                FileMasterServlet.configParams.get("DBusername"), FileMasterServlet.configParams.get("DBpassword"));
		      return conn;
		  }catch(NullPointerException ex){
			  throw new FileMasterNotConfiguredException("File Master not configured!");
		  }
	  }
}
