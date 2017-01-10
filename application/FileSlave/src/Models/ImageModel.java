package Models;

public class ImageModel implements Comparable<ImageModel>{
	public String link;
	public String diskPath;
	public double similarity;
	
	
	@Override
	public String toString()
	{
		StringBuilder output = new StringBuilder();
		output.append("{\"link\":\"");
		if(link != null)output.append(link);
		else output.append(":\"null");
		output.append("\",\"similarity\":\"").append(String.valueOf(similarity));
		output.append("\"}");
		return output.toString();
	}


	@Override
	public int compareTo(ImageModel o) {
		if(this.similarity < o.similarity) return 1;
		return -1;
	}
}
