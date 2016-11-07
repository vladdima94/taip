/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import servlet.FileMasterServlet;

/**
 *
 * @author Vlad
 */
public class ConfigParamsDAO extends DAO{
    
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
                    FileMasterServlet.writeToLog("<ERROR>Invalid param in config File!");
                }
                else
                {
                    configParamsMap.put(param[0], param[1]);
                }
            }
        }
        if(configParamsMap.get("LogFile") == null)
        {
            FileMasterServlet.writeToLog("<WARNING>Missing Mandatory Param [LogFile] in config file!");
            return;
        }
        FileMasterServlet.logFile = new File(configParamsMap.get("LogFile"));
        if(configParamsMap.get("DBdriver") == null)FileMasterServlet.writeToLog("<WARNING>Missing Mandatory Param [driver] in config file!");
        if(configParamsMap.get("DBlocation") == null)FileMasterServlet.writeToLog("<WARNING>Missing Mandatory Param [location] in config file!");
        if(configParamsMap.get("DBusername") == null)FileMasterServlet.writeToLog("<WARNING>Missing Mandatory Param [DBusername] in config file!");
        if(configParamsMap.get("DBpassword") == null)FileMasterServlet.writeToLog("<WARNING>Missing Mandatory Param [DBpassword] in config file!");
        if(configParamsMap.get("MasterURI") == null)FileMasterServlet.writeToLog("<WARNING>Missing Mandatory Param [MasterURI] in config file!");

    }
}

