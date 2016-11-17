/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import Exceptions.EntityAlreadyRegisteredException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vlad
 */
public class QueryProtocolDAO extends DAO{
    
    public boolean validateToken(String entity, String token) throws SQLException, ClassNotFoundException
    {
//        Connection conn =super.connectToDatabase();
        
        //TODO: validate token with the one in DB;
        //////////////////////////////////////////
        
        return true;
    }
    
    
    /**
     
     */
    public Map<String, String> getSlavesTokensPair() throws ClassNotFoundException, SQLException
    {
        Connection conn = super.connectToDatabase();
        
        //TODO: return list of Slaves + their token
        Map<String, String> output = new LinkedHashMap();
        
        ///////////////////////////////////////////
        return output;
    }
    
    
    /**
     * Add a pair of entity - token to DB used to validate requests
     * @param entity - URI to Master or Slave
     * @param token - entity token used to validate it`s requests
     */
    public void addTokenToDB(String entity, String token) throws ClassNotFoundException, SQLException, EntityAlreadyRegisteredException
    {
        Connection conn = super.connectToDatabase();
        conn.setAutoCommit(true);
        String stmtSQL = "SELECT count(LINK) as COUNTER FROM SLAVES_KEY_MASTER WHERE LINK LIKE ?";
        PreparedStatement stmt = conn.prepareStatement(stmtSQL);
        stmt.setString(1, entity);
        ResultSet rows = stmt.executeQuery();
        while(rows.next())
        {
            if(rows.getInt("COUNTER") > 0) throw new EntityAlreadyRegisteredException("Slave Already registered!");
        }
        String registerSQL = "INSERT INTO SLAVES_KEY_MASTER (LINK, KEY) VALUES(?, ?)";
        stmt = conn.prepareStatement(registerSQL);
        stmt.setString(1, entity);
        stmt.setString(2, token);
        stmt.execute();
    }
    public void removeTokenToDB(String token) throws ClassNotFoundException, SQLException, EntityAlreadyRegisteredException
    {
        Connection conn = super.connectToDatabase();
        conn.setAutoCommit(true);
        String stmtSQL = "DELETE FROM SLAVES_KEY_MASTER WHERE key LIKE ?";
        PreparedStatement stmt = conn.prepareStatement(stmtSQL);
        stmt.setString(1, token);
        stmt.execute();
    }
    
    public boolean validateSlaveToken(String token) throws ClassNotFoundException, SQLException
    {
        Connection conn = super.connectToDatabase();
        String stmtSQL = "SELECT LINK FROM SLAVES_KEY_MASTER WHERE KEY LIKE ?";
        PreparedStatement stmt = conn.prepareStatement(stmtSQL);
        stmt.setString(1, token);
        ResultSet rows = stmt.executeQuery();
        if(rows.getFetchSize() > 0)return true;
        return false;
    }
}

