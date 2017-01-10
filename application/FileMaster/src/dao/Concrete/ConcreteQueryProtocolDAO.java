/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.Concrete;

import Exceptions.EntityAlreadyRegisteredException;
import Exceptions.FileMasterNotConfiguredException;
import dao.DAO;
import dao.QueryProtocolDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Vlad
 */
public class ConcreteQueryProtocolDAO extends ConcreteJDBCDAO implements QueryProtocolDAO{
    
    public boolean validateToken(String entity, String token) throws SQLException, ClassNotFoundException
    {
//        Connection conn =super.connectToDatabase();
        
        //TODO: validate token with the one in DB;
        //////////////////////////////////////////
        
        return true;
    }
    
    
    public String checkIsUploading(String slaveLink) throws ClassNotFoundException, SQLException, FileMasterNotConfiguredException
    {
    	Connection conn = super.connectToDatabase();
    
	    String sqlStmt = "SELECT uploading FROM SLAVES_KEY_MASTER WHERE LINK LIKE ?";
	    PreparedStatement stmt = conn.prepareStatement(sqlStmt);
	    stmt.setString(1, slaveLink);
	    ResultSet rows = stmt.executeQuery();
	    String output = "";
	    while(rows.next())
	    {
	    	output = rows.getString("uploading");
	    }
	    return output;
    }
    
    
    /**
     * Returns a Map of pairs <Slave Link, Token> from DB
     * @throws FileMasterNotConfiguredException 
     */
    public Map<String, String> getSlavesTokensPair() throws ClassNotFoundException, SQLException, FileMasterNotConfiguredException
    {
        Connection conn = super.connectToDatabase();
        
        Map<String, String> output = new LinkedHashMap();
        Statement stmt = conn.createStatement();
        String sqlStmt = "SELECT LINK, TOKEN FROM SLAVES_KEY_MASTER";
        ResultSet rows = stmt.executeQuery(sqlStmt);
        while(rows.next())
        {
        	output.put(rows.getString("LINK"), rows.getString("TOKEN"));
        }
        return output;
    }
    
    
    /**
     * Add a pair of entity - token to DB used to validate requests
     * @param entity - URI to Master or Slave
     * @param token - entity token used to validate it`s requests
     */
    public void addTokenToDB(String entity, String token, String maxDBSize, String currentSize) throws FileMasterNotConfiguredException, ClassNotFoundException, SQLException, EntityAlreadyRegisteredException
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
        String registerSQL = "INSERT INTO SLAVES_KEY_MASTER (LINK, TOKEN, MAX_DB_SIZE, CURRET_DB_SIZE) VALUES(?, ?, ?, ?)";
        stmt = conn.prepareStatement(registerSQL);
        stmt.setString(1, entity);
        stmt.setString(2, token);
        stmt.setString(3, maxDBSize);
        stmt.setString(4, currentSize);
        stmt.execute();
        if(conn != null)
        {
        	conn.close();
        }
    }
    public void removeTokenToDB(String token) throws FileMasterNotConfiguredException, ClassNotFoundException, SQLException
    {
        Connection conn = super.connectToDatabase();
        conn.setAutoCommit(true);
        String stmtSQL = "DELETE FROM SLAVES_KEY_MASTER WHERE TOKEN LIKE ?";
        PreparedStatement stmt = conn.prepareStatement(stmtSQL);
        stmt.setString(1, token);
        stmt.execute();
        if(conn != null)
        {
        	conn.close();
        }
    }

    public synchronized List<String []> getSlaveWithSmallestDB() throws FileMasterNotConfiguredException, SQLException, ClassNotFoundException
    {
        Connection conn = super.connectToDatabase();
        conn.setAutoCommit(true);
    	String query = "SELECT link, token, CURRET_DB_SIZE FROM SLAVES_KEY_MASTER ORDER BY MAX_DB_SIZE ASC";
    	Statement stmt = conn.createStatement();
    	ResultSet results = stmt.executeQuery(query);
    	List<String[]> output = new LinkedList<String[]>();
    	while(results.next())
    	{
    		String [] slaveTokenPair = new String[3];
    		slaveTokenPair[0] = results.getString("LINK");
    		slaveTokenPair[1] = results.getString("TOKEN");
    		slaveTokenPair[2] = results.getString("CURRET_DB_SIZE");
    		output.add(slaveTokenPair);
    	}
    	if(output.size() == 0) return null;
    	return output;
    }
    
    public boolean validateSlaveToken(String token) throws FileMasterNotConfiguredException, ClassNotFoundException, SQLException
    {
    	Connection conn = null;
    	try{
	        conn = super.connectToDatabase();
	        String stmtSQL = "SELECT LINK FROM SLAVES_KEY_MASTER WHERE TOKEN LIKE ?";
	        PreparedStatement stmt = conn.prepareStatement(stmtSQL);
	        stmt.setString(1, token);
	        ResultSet rows = stmt.executeQuery();
	        if(rows.getFetchSize() > 0)return true;
	        return false;
    	}finally{
    		if(conn != null) conn.close();
    	}
    }
    public boolean validateUserToken(String token) throws FileMasterNotConfiguredException, ClassNotFoundException, SQLException
    {
    	Connection conn = null;
    	try{
	        conn = super.connectToDatabase();
	        String stmtSQL = "SELECT ID FROM USERS_KEY_MASTER WHERE TOKEN LIKE ?";
	        PreparedStatement stmt = conn.prepareStatement(stmtSQL);
	        stmt.setString(1, token);
	        ResultSet rows = stmt.executeQuery();
	        if(conn != null)
	        {
	        	conn.close();
	        }
	        if(rows.getFetchSize() > 0)return true;
	        return false;
    	}finally{
    		if(conn != null) conn.close();
    	}
    }


    public void updateSlaveDBSize(String slaveLink, int addedImages, String uploading) throws FileMasterNotConfiguredException, ClassNotFoundException, SQLException
    {
    	System.out.println("[FILEMASTER] Updating " + addedImages + " images in DB");
    	Connection conn = null;
    	try{
	        conn = super.connectToDatabase();
	        String stmtSQL = "UPDATE SLAVES_KEY_MASTER SET CURRET_DB_SIZE = ?, UPLOADING = ? WHERE LINK LIKE ?";
	        PreparedStatement stmt = conn.prepareStatement(stmtSQL);
	        stmt.setInt(1, addedImages);
	        stmt.setString(2, uploading);
	        stmt.setString(3, slaveLink);
	        ResultSet rows = stmt.executeQuery();
    	}finally{
    		if(conn != null) conn.close();
    	}
    }
}

