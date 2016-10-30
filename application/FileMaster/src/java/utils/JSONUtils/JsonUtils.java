/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.JSONUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Vlad
 */
public class JsonUtils {
    private String status;
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public void writeToOutput(PrintWriter out, JSONAdapter data)
    {
        JSONObject output = new JSONObject();
        output.put("status", status);
        if(data != null)
        {
            output.put("data", data.getJSONData());
        }
        else
        {
            output.put("data", "null");
        }
        out.append(output.toJSONString());
    }
    
    
    private static JSONParser parser = new JSONParser();
    public static JSONObject readBody(Reader input) throws ParseException, IOException
    {
        return (JSONObject) parser.parse(input);
    }
}

