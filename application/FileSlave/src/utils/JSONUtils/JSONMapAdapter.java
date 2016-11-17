/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.JSONUtils;

import java.util.ArrayList;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Vlad
 */
public class JSONMapAdapter<T> extends JSONAdapter{

    @Override
    public Object getJSONData() {
        if(this.data == null) return new JSONArray();
        Map<String, T> data = (Map)this.data;
        JSONArray output = new JSONArray();
        JSONObject tempObj;
        for(Map.Entry<String, T> row : data.entrySet())
        {
            tempObj = new JSONObject();
            tempObj.put(row.getKey(), row.getValue());
            output.add(tempObj);
        }
        return output;
    }
    
    @Override
    public String toString() {
        if(this.data == null) return new JSONArray().toJSONString();
        Map<String, T> data = (Map)this.data;
        JSONArray output = new JSONArray();
        JSONObject tempObj;
        for(Map.Entry<String, T> row : data.entrySet())
        {
            tempObj = new JSONObject();
            tempObj.put(row.getKey(), row.getValue());
            output.add(tempObj);
        }
        return output.toJSONString();
    }
    
}
