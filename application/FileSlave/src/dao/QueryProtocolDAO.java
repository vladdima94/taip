/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import servlet.FileSlaveServlet;

/**
 *
 * @author Vlad
 */
public class QueryProtocolDAO extends DAO{
    
    public boolean validateToken(String entity, String token) throws SQLException, ClassNotFoundException
    {
        Connection conn =super.connectToDatabase();
        
        //TODO: validate token with the one in DB;
        //////////////////////////////////////////
        
        return false;
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
    public void addTokenToDB(String entity, String token) throws ClassNotFoundException, SQLException
    {
        Connection conn = super.connectToDatabase();
        //TODO: insert <slave, token> pair in DB
    }
    
    
}
