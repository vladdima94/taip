package dao.Factory;


import dao.DAO;

public interface DAOFactory {
	public static final int CACHE_SYSTEM_DAO = 0;
	public static final int CONFIG_PARAMS_DAO = 1;
	public static final int QUERY_PROTOCOL_DAO = 2;
	public static final int SEARCH_SYSTEM_DAO = 3;
	
	public DAO getDAOInstance(int type);
}

