/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import SearchSystem.SearchingAlgorithms.SearchAlgorithm;
import SearchSystem.SearchingAlgorithms.SearchAlgorithmFactory;
import dao.SearchSystemDAO;
import dao.Factory.DAOFactory;
import servlet.FileSlaveServlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import ML.Classifier;
import ML.Instance;
import Models.ImageModel;
import utils.ImageUtils;
import utils.UriUtils;
import utils.JSONUtils.JSONListAdapter;
import utils.JSONUtils.JsonUtils;

/**
 *
 * @author Vlad
 */
public class SearchController extends Controller{
	
	public static SearchAlgorithm searchAlg;

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, UriUtils uri) {
    	if(searchAlg == null)
    	{
    		Controller.setQuickResponseMessage(500, "error", "Servlet not trained!", response);
            FileSlaveServlet.writeToLog("<ERROR> SearchController.processRequest() : searchAlg == null | FileSlave not trained!");
    		return;
    	}
    	
    	//TODO: read image from request and give it to search algorithm //////////////////////  
    	BufferedImage temp;
		try {
			temp = ImageIO.read(request.getInputStream());
			if(temp == null)
			{
				Controller.setQuickResponseMessage(400, "error", "Please send a request with a valid image/xxx Content-Type", response);
				return;
			}
//			ImageIO.write(temp, "jpg", new File("C:\\Users\\Vlad\\Desktop\\testoutput.jpg"));
		} catch (IOException e) {
			Controller.setQuickResponseMessage(400, "error", "Please send a request with a valid image/xxx Content-Type", response);
			e.printStackTrace();
			return;
		}
    	Mat inputImage = ImageUtils.Convert_BufferedImage2Mat_BGR(temp);
    	if(inputImage == null || inputImage.empty())
    	{
			Controller.setQuickResponseMessage(400, "error", "Please send a image in the request body!", response);
			return;
    	}
//    	Imgcodecs.imwrite("C:\\Users\\Vlad\\Desktop\\testoutput.jpg", inputImage);
        List<ImageModel> results = null;
        Classifier classifier = searchAlg.getClassifier();
        Instance inputInstance = classifier.getInstanceObject(searchAlg.extractFeatures(inputImage));
        SearchSystemDAO searchDAO = ((SearchSystemDAO)FileSlaveServlet.daoFactory.getDAOInstance(DAOFactory.SEARCH_SYSTEM_DAO));
		try {
//			results = searchDAO.searchFiles(inputImage, 10, searchAlg);
			results = searchDAO.getImagesFromCluster(classifier.classifie(inputInstance));
			searchAlg.sortData(results, inputInstance);
		} catch (ClassNotFoundException ex) {
			Controller.setQuickResponseMessage(500, "error", "Server error, working to fix it!", response);
            FileSlaveServlet.writeToLog("<ERROR> SearchController.processRequest() : ClassNotFoundException(" + ex.getMessage() + ")");
			ex.printStackTrace();
            return;
		} catch (SQLException ex) {
			Controller.setQuickResponseMessage(500, "error", "Server error, working to fix it!", response);
            FileSlaveServlet.writeToLog("<ERROR> SearchController.processRequest() : SQLException(" + ex.getMessage() + ")");
			ex.printStackTrace();
			return;
		}
        //////////////////////////////////////////////////////////////////////////////////////
        
        JSONListAdapter<ImageModel> resultsAdapter = new JSONListAdapter();
        resultsAdapter.setData(results);
        JsonUtils responseBody = new JsonUtils();
        responseBody.setStatus("success");
        responseBody.setJSONAdapter(resultsAdapter);
        try {
			responseBody.writeToOutput(response.getWriter());
		} catch (IOException ex) {
            FileSlaveServlet.writeToLog("<ERROR> SearchController.processRequest() : IOException(" + ex.getMessage() + ")");
            response.setStatus(500);
		}
        response.setStatus(200);
    }

}
