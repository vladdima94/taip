/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.JSONUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Vlad
 */
public class JsonUtils {
    private String status;
    private String message;
    private JSONAdapter adapter;
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    public void setMessage(String message)
    {
        this.message = message;
    }
    public void setJSONAdapter(JSONAdapter adapter)
    {
        this.adapter = adapter;
    }
    
    public void writeToOutput(PrintWriter out)
    {
        JSONObject output = new JSONObject();
        output.put("status", status);
        output.put("message", message);
        if(adapter != null)
        {
            output.put("data", adapter.getJSONData());
        }
        else
        {
            output.put("data", "null");
        }
        out.append(output.toJSONString());
    }
    
    @Override
    public String toString()
    {
        JSONObject output = new JSONObject();
        output.put("status", status);
        output.put("message", message);
        if(adapter != null)
        {
            output.put("data", adapter.getJSONData());
        }
        else
        {
            output.put("data", "null");
        }
        return output.toJSONString();
    }

    
    private static JSONParser parser = new JSONParser();
    public static JSONObject readBody(Reader input)
    {
        try {
            return (JSONObject) parser.parse(input);
        } catch (IOException | ParseException ex) {
            return null;
        }
    }
    public static JSONObject readBody(String input)
    {
        try {
            return (JSONObject) parser.parse(input);
        } catch (ParseException ex) {
            return null;
        }
    }
}

