/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.JSONUtils;

import java.util.List;
import org.json.simple.JSONArray;

/**
 *
 * @author Vlad
 */
public class JSONListAdapter<T> extends JSONAdapter{

    @Override
    public Object getJSONData()
    {
        if(this.data == null) return new JSONArray();
        List<String> data = (List)this.data;
        JSONArray output = new JSONArray();
        int size = data.size();
        for(int i = 0; i < size; ++i)
        {
            output.add(data.get(i));
        }
        return output;
    }
    
    @Override
    public String toString()
    {
        if(this.data == null) return new JSONArray().toJSONString();
        List<T> data = (List)this.data;
        JSONArray output = new JSONArray();
        int size = data.size();
        for(int i = 0; i < size; ++i)
        {
            output.add(data.get(i));
        }
        return output.toJSONString();
    }
}