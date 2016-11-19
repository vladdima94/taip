package utils.JSONUtils;

import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONMapObjectAdapter<T> extends JSONAdapter{

    @Override
    public Object getJSONData() {
        if(this.data == null) return new JSONArray();
        Map<String, T> data = (Map)this.data;
        JSONObject output = new JSONObject();
        for(Map.Entry<String, T> row : data.entrySet())
        {
            output.put(row.getKey(), row.getValue());
        }
        return output;
    }
    
    @Override
    public String toString() {
        if(this.data == null) return new JSONArray().toJSONString();
        Map<String, T> data = (Map)this.data;
        JSONObject output = new JSONObject();
        for(Map.Entry<String, T> row : data.entrySet())
        {
        	output.put(row.getKey(), row.getValue());
        }
        return output.toJSONString();
    }
    
}
