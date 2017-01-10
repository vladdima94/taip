package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import servlet.FileSlaveServlet;

public interface ConfigParamsDAO extends DAO{
	public static void loadConfigParams(String configFilePath, HashMap<String, String> configParamsMap) throws FileNotFoundException, IOException
    {
        try(FileReader input = new FileReader(configFilePath);
                BufferedReader in = new BufferedReader(input)){
            String line;
            while( (line = in.readLine()) != null)
            {
                String [] param = line.split("=");
                if(param.length < 2)
                {
                    FileSlaveServlet.writeToLog("<ERROR>Invalid param in config File!");
                }
                else
                {
                    configParamsMap.put(param[0], param[1]);
                }
            }
        }
        if(configParamsMap.get("LogFile") == null)
        {
            FileSlaveServlet.writeToLog("<ERROR>Missing Mandatory Param [LogFile] in config file!");
            return;
        }
        FileSlaveServlet.logFile = new File(configParamsMap.get("LogFile"));
        if(configParamsMap.get("DBdriver") == null)FileSlaveServlet.writeToLog("<ERROR>Missing Mandatory Param [driver] in config file!");
        if(configParamsMap.get("DBlocation") == null)FileSlaveServlet.writeToLog("<ERROR>Missing Mandatory Param [location] in config file!");
        if(configParamsMap.get("DBusername") == null)FileSlaveServlet.writeToLog("<ERROR>Missing Mandatory Param [DBusername] in config file!");
        if(configParamsMap.get("DBpassword") == null)FileSlaveServlet.writeToLog("<ERROR>Missing Mandatory Param [DBpassword] in config file!");
        if(configParamsMap.get("PathToDiskDB") == null)FileSlaveServlet.writeToLog("<ERROR>Missing Param [PathToDiskDB] in config file!");
        if(configParamsMap.get("MasterURI") == null)FileSlaveServlet.writeToLog("<WARNING>Missing Param [MasterURI] in config file! Mandatory if you are in FileSlave.");
        if(configParamsMap.get("slaveRegisterToken") == null)FileSlaveServlet.writeToLog("<WARNING>Missing Param [slaveRegisterToken] in config file! Using default key [vladdima]");
        if(configParamsMap.get("maxDBSize") == null)FileSlaveServlet.writeToLog("<WARNING>Missing Param [slaveRegisterToken] in config file! Using default key [vladdima]");
        if(configParamsMap.get("algorithm") == null)FileSlaveServlet.writeToLog("<WARNING>Missing Param [algorithm] in config file! Using default algorithm [CBIR]");
        if(configParamsMap.get("numberBlocksWidth") == null)
    	{
        	configParamsMap.put("numberBlocksWidth", "8");
    		FileSlaveServlet.writeToLog("<WARNING>Missing Param [numberBlocksWidth] in config file! Using default value [8]");
    	}
        if(configParamsMap.get("numberBlocksHeight") == null)
    	{
        	configParamsMap.put("numberBlocksHeight", "8");
    		FileSlaveServlet.writeToLog("<WARNING>Missing Param [numberBlocksHeight] in config file! Using default value [8]");
    	}
        if(configParamsMap.get("numberOfClusters") == null)
    	{
        	configParamsMap.put("numberOfClusters", "10");
    		FileSlaveServlet.writeToLog("<ERROR>Missing Param [numberOfClusters] in config file! Using default value [10]");
    	}
        else
        {
        	try
        	{
    			Integer.parseInt(FileSlaveServlet.configParams.get("numberOfClusters"));
    		}catch(NumberFormatException ex){
            	configParamsMap.put("numberOfClusters", "10");
        		FileSlaveServlet.writeToLog("<ERROR>Invalid Param [numberOfClusters] in config file! Using default value [10]");
        	}
        }
    }
}
