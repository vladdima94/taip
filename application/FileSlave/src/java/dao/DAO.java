/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import servlet.FileSlaveServlet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Vlad
 */
public abstract class DAO {
    
    protected Connection connectToDatabase() throws ClassNotFoundException, SQLException
    {
        Class.forName(FileSlaveServlet.configParams.get("driver"));
        Connection conn = DriverManager.getConnection(FileSlaveServlet.configParams.get("location"),
                                                      FileSlaveServlet.configParams.get("DBusername"), FileSlaveServlet.configParams.get("DBpassword"));
        return conn;
    }
}
