package dao.Factory;

import dao.DAO;
import dao.Concrete.ConcreteCacheSystemDAO;
import dao.Concrete.ConcreteConfigParamsDAO;
import dao.Concrete.ConcreteQueryProtocolDAO;
import dao.Concrete.ConcreteSearchSystemDAO;

public class JDBCFactory implements DAOFactory{

	
	@Override
	public DAO getDAOInstance(int type)
	{
		switch(type)
		{
			case DAOFactory.CACHE_SYSTEM_DAO:{
				return new ConcreteCacheSystemDAO();
			}
			case DAOFactory.CONFIG_PARAMS_DAO:{
				return new ConcreteConfigParamsDAO();
			}
			case DAOFactory.QUERY_PROTOCOL_DAO:{
				return new ConcreteQueryProtocolDAO();
			}
			case DAOFactory.SEARCH_SYSTEM_DAO:{
				return new ConcreteSearchSystemDAO();
			}
			
			default: return null;
		}
	}
}
