package dao.Concrete;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import servlet.FileSlaveServlet;

public class ConcreteJDBCDAO {
    
    protected Connection connectToDatabase() throws ClassNotFoundException, SQLException
    {
        Class.forName(FileSlaveServlet.configParams.get("DBdriver"));
        Connection conn = DriverManager.getConnection(FileSlaveServlet.configParams.get("DBlocation"),
                                                      FileSlaveServlet.configParams.get("DBusername"), FileSlaveServlet.configParams.get("DBpassword"));
        return conn;
    }
}
