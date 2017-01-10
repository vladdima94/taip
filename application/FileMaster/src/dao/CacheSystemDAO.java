package dao;

import java.util.LinkedList;
import java.util.List;

public interface CacheSystemDAO extends DAO{
    public void addCacheToDB(double [] data, List<String> results);
    public List<String> getCacheFromDB(int queryID);
}
