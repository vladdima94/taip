package Controller;

public class ControllerFactory {
	private static ControllerFactory instance;
	private ControllerFactory(){};
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException("Clone not available!");
	}
	
	public static ControllerFactory getInstance()
	{
		if(instance == null) instance = new ControllerFactory();
		return instance;
	}
	
	
	public Controller getController(String type)
	{
		if(type == null) return new InputSearchController();
		switch(type)
		{
			case "":{
				return new InputSearchController();
			}
			case "search":{
				return new ResultsSearchController();
			}
			default: return new ErrorController("Invalid Action");
		}
	}
}
