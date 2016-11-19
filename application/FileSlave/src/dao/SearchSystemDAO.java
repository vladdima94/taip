/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import SearchSystem.SearchingAlgorithms.SearchAlgorithm;
import servlet.FileSlaveServlet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.ImageModel;

/**
 *
 * @author Vlad
 */
public class SearchSystemDAO extends DAO{
    
	public int getDBSize()
	{
		Connection conn = null;
		try {
			conn = super.connectToDatabase();
			String sqlStmt = "SELECT COUNT(*) AS NR_ROWS FROM FEATURE_VECTOR_SLAVE";
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
    
    public List<ImageModel> searchFiles(double similarityThreshold, SearchAlgorithm testingAlgorithm)
    {
//        List<Map<String, String>> results = new ArrayList();
//        //TODO: get through all instances in db and use testingAlgorithm to test similarities;
//        
//        
//        Map<String,String> map1 = new HashMap();
//        Map<String,String> map2 = new HashMap();
//        Map<String,String> map3 = new HashMap();
//        Map<String,String> map4 = new HashMap();
//        
//        map1.put("link" ,"https://s-media-cache-ak0.pinimg.com/originals/4b/b2/85/4bb2852cb354b0ad89dfa1a97ef2f6fa.jpg");
//        map1.put("similarity" ,"null");
//
//        map2.put("link", "http://pop.h-cdn.co/assets/cm/15/05/54cb1d27a519c_-_analog-sports-cars-01-1013-de.jpg");
//        map2.put("similarity" ,"null");
//
//        map3.put("link", "http://pop.h-cdn.co/assets/cm/15/05/54cb1d27a519c_-_analog-sports-cars-01-1013-de.jpg");
//        map3.put("similarity" ,"null");
//        
//        map4.put("link", "https://s-media-cache-ak0.pinimg.com/originals/4b/b2/85/4bb2852cb354b0ad89dfa1a97ef2f6fa.jpg");
//        map4.put("similarity" ,"null");
//        
//        results.add(map1);
//        results.add(map2);
//        results.add(map3);
//        results.add(map4);
    	List<ImageModel> results = new ArrayList();
    	
    	ImageModel img1 = new ImageModel();
    	ImageModel img2 = new ImageModel();
    	ImageModel img3 = new ImageModel();
    	ImageModel img4 = new ImageModel();
    	
    	img1.link = "https://s-media-cache-ak0.pinimg.com/originals/4b/b2/85/4bb2852cb354b0ad89dfa1a97ef2f6fa.jpg";
    	img1.similarity = "null";
    	img2.link = "http://pop.h-cdn.co/assets/cm/15/05/54cb1d27a519c_-_analog-sports-cars-01-1013-de.jpg";
    	img2.similarity = "null";
    	img3.link = "http://pop.h-cdn.co/assets/cm/15/05/54cb1d27a519c_-_analog-sports-cars-01-1013-de.jpg";
    	img3.similarity = "null";
    	img4.link = "https://s-media-cache-ak0.pinimg.com/originals/4b/b2/85/4bb2852cb354b0ad89dfa1a97ef2f6fa.jpg";
    	img4.similarity = "null";
		  results.add(img1);
		  results.add(img2);
		  results.add(img3);
		  results.add(img4);
        return results;
    }
}
