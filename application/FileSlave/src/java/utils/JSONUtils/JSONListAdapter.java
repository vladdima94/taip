/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.JSONUtils;

import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Vlad
 */
public class JSONListAdapter extends JSONAdapter{

    @Override
    public Object getJSONData()
    {
        ArrayList<String> data = (ArrayList)this.data;
        JSONArray output = new JSONArray();
        int size = data.size();
        for(int i = 0; i < size; ++i)
        {
            output.add(data.get(i));
        }
        return output;
    }
}
