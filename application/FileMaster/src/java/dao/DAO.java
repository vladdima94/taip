/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import servlet.FileMasterServlet;

/**
 *
 * @author Vlad
 */
public abstract class DAO {
    
    protected Connection connectToDatabase() throws ClassNotFoundException, SQLException
    {
        Class.forName(FileMasterServlet.configParams.get("driver"));
        Connection conn = DriverManager.getConnection(FileMasterServlet.configParams.get("location"),
                                                      FileMasterServlet.configParams.get("DBusername"), FileMasterServlet.configParams.get("DBpassword"));
        return conn;
    }
}
