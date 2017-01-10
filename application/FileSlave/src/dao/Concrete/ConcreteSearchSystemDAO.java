package dao.Concrete;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.json.simple.JSONArray;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import ML.Classifier;
import ML.Instance;
import ML.kmeas.Cluster;
import ML.kmeas.KMeans;
import Models.ImageModel;
import SearchSystem.SearchingAlgorithms.CBIRSearchAlgorithm;
import SearchSystem.SearchingAlgorithms.KMeansImageInstance;
import SearchSystem.SearchingAlgorithms.SearchAlgorithm;
import dao.DAO;
import dao.SearchSystemDAO;
import servlet.FileSlaveServlet;
import utils.ImageUtils;

public class ConcreteSearchSystemDAO extends ConcreteJDBCDAO implements SearchSystemDAO{
    
	public int getDBSize()
	{
		Connection conn = null;
		try {
			conn = super.connectToDatabase();
			String sqlStmt = "SELECT count(ID) AS NR_ROWS FROM IMAGE_FEATURES_SLAVE";
			Statement stmt = conn.createStatement();
			ResultSet rows = stmt.executeQuery(sqlStmt);
			while(rows.next())
			{
				return rows.getInt("NR_ROWS");
			}
		} catch (ClassNotFoundException ex) {
            FileSlaveServlet.writeToLog("<ERROR> SearchSystemDAO.getDBSize() : ClassNotFoundException(" + ex.getMessage() + ")");
		} catch (SQLException ex) {
            FileSlaveServlet.writeToLog("<ERROR> SearchSystemDAO.getDBSize() : SQLException(" + ex.getMessage() + ")");
		} finally{
	        if(conn != null)
	        {
	        	try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
		}
		return -1;
	}
    
	
	public synchronized int uploadImageToDB(JSONArray jsonImages) throws ClassNotFoundException, SQLException
	{
		Connection conn = super.connectToDatabase();
		CBIRSearchAlgorithm test = new CBIRSearchAlgorithm();
		test.numberBlocksHeigth = 8;
		test.numberBlocksWidth = 8;
		conn.setAutoCommit(true);
		String sqlInsert = "INSERT INTO IMAGE_FEATURES_SLAVE(IMAGE_LINK, FEATURE_PATH, ID_CLUSTER) VALUES(?, ?, 0)";
		
		int size = jsonImages.size();
		int successfullyUploaded = 0;
		for(int i = 0; i < size; ++i)
		{
			try{
				String imgURI = (String)jsonImages.get(i);
				
				
				//TODO: extact features
//				System.out.println("--------------------------------");
//				System.out.println("[FILESLAVE] DOWNLOADING IMG: " + imgURI);
				BufferedImage image = getImageFromLink(imgURI);
				if(image == null || image.getType() > 7) continue;
//				System.out.println("[FILESLAVE] FINISHED DOWNLOADING IMG");
				String imgPathOnDisk = FileSlaveServlet.configParams.get("PathToDiskDB") + "\\" + System.currentTimeMillis() + ".jpg";
				
				///////////
				
				//TODO: add to db img URI and path to feature vector
//				System.out.println("[FILESLAVE] SAVING IMG IN DB");
				PreparedStatement stmt = conn.prepareStatement(sqlInsert);
				stmt.setString(1, imgURI);
				stmt.setString(2, imgPathOnDisk + ".cbir");
				stmt.execute();
				stmt.close();
//				System.out.println("[FILESLAVE] SAVED IMG IN DB");
//				System.out.println("[FILESLAVE] SAVING IMG ON DISK");
//				ImageIO.write(image, "jpg", new File(imgPathOnDisk));
//				System.out.println("[FILESLAVE] SAVED ON DISK");
//				System.out.println("[FILESLAVE] EXTACTING IMG FEATURES");
				test.extractFeatures(ImageUtils.Convert_BufferedImage2Mat_BGR(image), imgPathOnDisk + ".cbir");
//				System.out.println("[FILESLAVE] FINISHED EXTACTING IMG FEATURES");
				successfullyUploaded++;
//				System.out.println("--------------------------------");
			}
			catch(SQLException ex){
				ex.printStackTrace();
			}
//			catch(IOException ex){
//				//FIXME: Error saving image feature vector localy, remove it from DB
//				ex.printStackTrace();
//				break;
//			}
		}
		
		conn.close();
		return successfullyUploaded;
	}
	
	private BufferedImage getImageFromLink(String link)
	{
        URL url;
		try {
			url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setDoOutput(true);
	        conn.setRequestMethod("GET");
	        conn.setConnectTimeout(15000);
	        return ImageIO.read(conn.getInputStream());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		return null;
	}
	
    public List<ImageModel> searchFiles(Mat inputImage, double similarityThreshold, SearchAlgorithm testingAlgorithm) throws ClassNotFoundException, SQLException
    {
    	Connection conn = super.connectToDatabase();
    	
    	String sqlSelect = "SELECT * FROM IMAGE_FEATURES_SLAVE";
    	Statement stmt = conn.createStatement();
    	ResultSet queryResults = stmt.executeQuery(sqlSelect);
    	List<ImageModel> results = new ArrayList<ImageModel>();
    	while(queryResults.next())
    	{
    		ImageModel temp = new ImageModel();
    		temp.link = queryResults.getString("IMAGE_LINK");
    		temp.diskPath = queryResults.getString("FEATURE_PATH");
    		results.add(temp);
    	}
    	stmt.close();
    	conn.close();
    	int featureVectorSize = testingAlgorithm.getFeatureSize();
    	double [] instanceFeatureVector = new double[featureVectorSize];
    	double [] inputImageFeatureVector = testingAlgorithm.extractFeatures(inputImage);
    	for(ImageModel image : results)
    	{
    		try
    		{
    			SearchSystemDAO.readFeatureVector(instanceFeatureVector, image.diskPath);
    			image.similarity = testingAlgorithm.getSimilarityLevel(inputImageFeatureVector, instanceFeatureVector);
//    			System.out.printf("Reading imageVector [%s] vector size [%d] with similarity [%f]\r\n", image.diskPath, instanceFeatureVector.length, image.similarity);
			}catch(IOException ex){
				
			}
    	}
    	
    	
    	Collections.sort(results);

    	String outputPath = "C:\\Users\\Vlad\\Desktop\\results\\";
    	int size = results.size();
    	for(int i = 0; i < size; ++i)
    	{
    		ImageModel temp = results.get(i);
			BufferedImage image = getImageFromLink(temp.link);
			if(image == null || image.getType() > 7) continue;
			Mat img = ImageUtils.Convert_BufferedImage2Mat_BGR(image);
			Imgcodecs.imwrite(outputPath + i + ".jpg", img);
			System.out.printf("Image [%d] similarity [%f]", i , temp.similarity);
    	}
    	
        return results;
    }
    
    @Override
    public List<Instance> getInstancesFromDB(SearchAlgorithm searchAlg) throws SQLException, ClassNotFoundException
    {
    	Connection conn = super.connectToDatabase();
    	
    	String sqlSelect = "SELECT * FROM IMAGE_FEATURES_SLAVE";
    	Statement stmt = conn.createStatement();
    	ResultSet queryResults = stmt.executeQuery(sqlSelect);
    	List<Instance> results = new ArrayList<Instance>();

    	
    	int featureVectorSize = searchAlg.getFeatureSize();
    	Classifier classifier = searchAlg.getClassifier();
    	while(queryResults.next())
    	{
    		String diskPath = queryResults.getString("FEATURE_PATH");
    		int id = queryResults.getInt("ID");
    		try {
//	    		Instance newInstance = classifier.getInstanceObject(readFeatureVector(diskPath, featureVectorSize));
    			KMeansImageInstance newInstance = new KMeansImageInstance(readFeatureVector(diskPath, featureVectorSize), id);
	    		results.add(newInstance);
			} catch (IOException e) {
				// Ivalid feature path => ignore it
//				e.printStackTrace();
			}
    	}
    	stmt.close();
    	conn.close();
        return results;
    }
    

    
    private double [] readFeatureVector(String outputFile, int featureSize) throws FileNotFoundException, IOException
    {
    	try(FileInputStream output = new FileInputStream(outputFile);
    		FileChannel out = output.getChannel())
    	{
        		double [] vector = new double[featureSize];
    			ByteBuffer buf = ByteBuffer.allocate(8 * vector.length);
    			buf.clear();
    			out.read(buf);
    			buf.flip();
    			buf.asDoubleBuffer().get(vector);
    			return vector;
    	}
    }


	@Override
	public void updateImageToClusters(KMeans algorithm) throws ClassNotFoundException, SQLException {
		Connection conn = super.connectToDatabase();
    	String updateSQL = "UPDATE IMAGE_FEATURES_SLAVE SET ID_CLUSTER = ? WHERE ID = ?";
		PreparedStatement stmt;
		
		Cluster [] clusters = algorithm.getClusters();
		
		
		for(Cluster cluster : clusters)
		{
			for(Instance inst : cluster.instances)
			{
				KMeansImageInstance instance = (KMeansImageInstance)inst;
				stmt = conn.prepareStatement(updateSQL);
				stmt.setInt(1, cluster.clusterID);
				stmt.setInt(2, instance.imageID);
				stmt.executeQuery();
				stmt.close();
			}
		}
		
    	conn.close();
	}


	@Override
	public List<ImageModel> getImagesFromCluster(int clusterID) throws ClassNotFoundException, SQLException {
		List<ImageModel> output = new ArrayList();
		
		Connection conn = super.connectToDatabase();
		String getImagesSQL = "SELECT IMAGE_LINK, FEATURE_PATH FROM IMAGE_FEATURES_SLAVE WHERE ID_CLUSTER = ?";
		PreparedStatement stmt = conn.prepareStatement(getImagesSQL);
		stmt.setInt(1, clusterID);
		ResultSet rows = stmt.executeQuery();
		
		while(rows.next())
		{
			ImageModel newImg = new ImageModel();
			newImg.link = rows.getString("IMAGE_LINK");
			newImg.diskPath = rows.getString("FEATURE_PATH");
			output.add(newImg);
		}
		
		conn.close();
		return output;
	}
}

