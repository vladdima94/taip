package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import SearchSystem.SearchingAlgorithms.SearchAlgorithmFactory;
import utils.UriUtils;

public class TrainController extends Controller{

	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response, UriUtils uri) {
		SearchController.searchAlg = SearchAlgorithmFactory.getInstance().getAlgorithm("CBIR");
		SearchController.searchAlg.train();
		if(SearchController.searchAlg.isTrained())
		{
			Controller.setQuickResponseMessage(200, "success", "Succesfully trained the Slave", response);
		}
		else
		{
			Controller.setQuickResponseMessage(200, "error", "Error training the slave. See logs for info!", response);
		}
	}

}
