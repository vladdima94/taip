package dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.List;
import org.json.simple.JSONArray;
import org.opencv.core.Mat;

import ML.Instance;
import ML.kmeas.KMeans;
import Models.ImageModel;
import SearchSystem.SearchingAlgorithms.SearchAlgorithm;

public interface SearchSystemDAO extends DAO{
	public int getDBSize();
	public int uploadImageToDB(JSONArray jsonImages) throws ClassNotFoundException, SQLException;
    public List<ImageModel> searchFiles(Mat inputImage, double similarityThreshold, SearchAlgorithm testingAlgorithm) throws ClassNotFoundException, SQLException;
    public List<Instance> getInstancesFromDB(SearchAlgorithm searchAlg) throws SQLException, ClassNotFoundException;
    public void updateImageToClusters(KMeans algorithm) throws ClassNotFoundException, SQLException;
    public List<ImageModel> getImagesFromCluster(int clusterID) throws ClassNotFoundException, SQLException;
    public List<ImageModel> getImagesFromClusters(int ... clusters) throws ClassNotFoundException, SQLException;
    
    public static void readFeatureVector(double [] vector, String outputFile) throws FileNotFoundException, IOException
    {
    	try(FileInputStream output = new FileInputStream(outputFile);
    		FileChannel out = output.getChannel())
    	{
    			ByteBuffer buf = ByteBuffer.allocate(8 * vector.length);
    			buf.clear();
    			out.read(buf);
    			buf.flip();
    			buf.asDoubleBuffer().get(vector);
    	}
    }
}
