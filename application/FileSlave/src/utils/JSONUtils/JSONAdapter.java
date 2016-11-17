/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.JSONUtils;

import org.json.simple.JSONObject;

/**
 *
 * @author Vlad
 */
public abstract class JSONAdapter {
    protected Object data;
    
    public abstract Object getJSONData();
    public void setData(Object data){this.data = data;}
}
