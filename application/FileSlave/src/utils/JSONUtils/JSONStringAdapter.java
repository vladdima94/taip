/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.JSONUtils;

/**
 *
 * @author Vlad
 */
public class JSONStringAdapter extends JSONAdapter{

    @Override
    public Object getJSONData() {
        return data;
    }
    
    

    @Override
    public String toString() {
    	return data.toString();
    }
}
