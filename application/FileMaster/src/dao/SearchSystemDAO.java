package dao;

import java.util.ArrayList;
import java.util.List;

import SearchSystem.SearchingAlgorithms.SearchAlgorithm;

public interface SearchSystemDAO extends DAO{
	   public List<String> searchFiles(double similarityThreshold, SearchAlgorithm testingAlgorithm);
}
