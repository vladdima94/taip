package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Exceptions.EntityAlreadyRegisteredException;
import Exceptions.FileMasterNotConfiguredException;

public interface QueryProtocolDAO extends DAO{
	public boolean validateToken(String entity, String token) throws SQLException, ClassNotFoundException;
    
    
    public String checkIsUploading(String slaveLink) throws ClassNotFoundException, SQLException, FileMasterNotConfiguredException;
    
    
    /**
     * Returns a Map of pairs <Slave Link, Token> from DB
     */
    public Map<String, String> getSlavesTokensPair() throws FileMasterNotConfiguredException, ClassNotFoundException, SQLException;
    
    /**
     * Add a pair of entity - token to DB used to validate requests
     * @param entity - URI to Master or Slave
     * @param token - entity token used to validate it`s requests
     */
    public void addTokenToDB(String entity, String token, String maxDBSize, String currentSize) throws FileMasterNotConfiguredException, ClassNotFoundException, SQLException, EntityAlreadyRegisteredException;
    
    public void removeTokenToDB(String token) throws FileMasterNotConfiguredException, ClassNotFoundException, SQLException;
    public List<String []> getSlaveWithSmallestDB() throws FileMasterNotConfiguredException, SQLException, ClassNotFoundException;
    public boolean validateSlaveToken(String token) throws FileMasterNotConfiguredException, ClassNotFoundException, SQLException;
    public boolean validateUserToken(String token) throws FileMasterNotConfiguredException, ClassNotFoundException, SQLException;


    public void updateSlaveDBSize(String slaveLink, int addedImages, String uploading) throws FileMasterNotConfiguredException, ClassNotFoundException, SQLException;
}
