package dao;

import java.sql.SQLException;
import java.util.Map;

public interface QueryProtocolDAO extends DAO{
    public boolean validateToken(String entity, String token) throws SQLException, ClassNotFoundException;
    
    
    /**
     
     */
    public Map<String, String> getSlavesTokensPair() throws ClassNotFoundException, SQLException;
    
    
    /**
     * Add a pair of entity - token to DB used to validate requests
     * @param entity - URI to Master or Slave
     * @param token - entity token used to validate it`s requests
     */
    public void addTokenToDB(String entity, String token) throws ClassNotFoundException, SQLException;
}
