package dao.Factory;

public class AbstractFactory {
	public static final int JDBC_DAO_FACTORY = 0;
	
	
	private static AbstractFactory instance;
	private AbstractFactory(){};
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	
	public static synchronized AbstractFactory getInstance()
	{
		if(instance == null) instance = new AbstractFactory();
		return instance;
	}
	
	public DAOFactory getDAOFactoryInstance(int type)
	{
		switch(type)
		{
			case AbstractFactory.JDBC_DAO_FACTORY:{
				return new JDBCFactory();
			}
			default: return null;
		}
	}
}