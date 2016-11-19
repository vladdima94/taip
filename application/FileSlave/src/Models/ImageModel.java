package Models;

public class ImageModel {
	public String link;
	public String similarity;
	
	
	@Override
	public String toString()
	{
		StringBuilder output = new StringBuilder();
		output.append("{\"link\":\"");
		if(link != null)output.append(link);
		else output.append(":\"null");
		output.append("\",\"similarity\":\"");
		if(similarity!= null) output.append(similarity);
		else output.append(":\"null");
		output.append("\"}");
		return output.toString();
	}
}
