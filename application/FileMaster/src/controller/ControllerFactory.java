/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import utils.UriUtils;

/**
 *
 * @author Vlad
 */
public class ControllerFactory {
    private static ControllerFactory instance;
    
    private ControllerFactory(){};
    static
    {
        instance = new ControllerFactory();
    }
    public static ControllerFactory getInstance()
    {
        return instance;
    }
    
    public Controller getController(String type)
    {
        if(type == null) return new ErrorController();
        switch(type)
        {
	        case "search":
	        {
	            return new SearchController();
	        }
            case "registerSlave":
            {
                return new RegisterSlaveController();
            }
            case "unregisterSlave":
            {
                return new UnregisterSlaveController();
            }
            case "prepareServlet":
            {
                return new PrepareServletController();
            }
            case "upload":
            {
                return new UploadImageController();
            }
            default: return new ErrorController();
        }
    }
}

