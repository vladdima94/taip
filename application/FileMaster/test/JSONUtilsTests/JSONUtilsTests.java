/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JSONUtilsTests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import junit.framework.TestCase;
import org.json.simple.JSONObject;
import utils.JSONUtils.JSONListAdapter;
import utils.JSONUtils.JSONMapAdapter;
import utils.JSONUtils.JsonUtils;

/**
 *
 * @author Vlad
 */
public class JSONUtilsTests extends TestCase{
    
    public JSONUtilsTests() {
        
    }
    
    public void testJSONMapAdapter_Use_JSONListAdapter_Value()
    {
        JsonUtils testJSONResponse = new JsonUtils();
        testJSONResponse.setMessage("Testing JSONListAdapter");
        testJSONResponse.setStatus("success");
        
        List<String> listData1 = new ArrayList();
        listData1.add("item1");
        listData1.add("item2");
        listData1.add("item3");
        List<String> listData2 = new ArrayList();
        listData2.add("item1");
        listData2.add("item2");
        List<String> listData3 = new ArrayList();
        listData3.add("item1");
        
        JSONListAdapter listAdapter1 = new JSONListAdapter();
        JSONListAdapter listAdapter2 = new JSONListAdapter();
        JSONListAdapter listAdapter3 = new JSONListAdapter();
        
        listAdapter1.setData(listData1);
        listAdapter2.setData(listData2);
        listAdapter3.setData(listData3);
        
        Map<String, JSONListAdapter> mapData = new TreeMap();
        mapData.put("mapItem1", listAdapter1);
        mapData.put("mapItem2", listAdapter2);
        mapData.put("mapItem3", listAdapter3);
        
        JSONMapAdapter testMapAdapter = new JSONMapAdapter();
        testMapAdapter.setData(mapData);
        testJSONResponse.setJSONAdapter(testMapAdapter);
        assertEquals(testJSONResponse.toString() , "{\"data\":[{\"mapItem1\":[\"item1\",\"item2\",\"item3\"]},{\"mapItem2\":[\"item1\",\"item2\"]},{\"mapItem3\":[\"item1\"]}],\"message\":\"Testing JSONListAdapter\",\"status\":\"success\"}");
    }
    public void testJSONMapAdapter_Base_Case()
    {
        JsonUtils testJSONResponse = new JsonUtils();
        testJSONResponse.setMessage("Testing JSONListAdapter");
        testJSONResponse.setStatus("success");
        
        Map<String, String> mapData = new HashMap();
        mapData.put("test1", "val1");
        mapData.put("test2", "val2");
        mapData.put("test3", "val3");
        JSONMapAdapter mapAdapter = new JSONMapAdapter();
        mapAdapter.setData(mapData);
        testJSONResponse.setJSONAdapter(mapAdapter);
        assertEquals(testJSONResponse.toString(), "{\"data\":[{\"test2\":\"val2\"},{\"test3\":\"val3\"},{\"test1\":\"val1\"}],\"message\":\"Testing JSONListAdapter\",\"status\":\"success\"}");
    }
    
    
    public void testJSONListAdapter_JSONAdapter_Value()
    {
        JsonUtils testJSONResponse = new JsonUtils();
        testJSONResponse.setMessage("Testing JSONListAdapter");
        testJSONResponse.setStatus("success");
        
        List<String> listData1 = new ArrayList();
        listData1.add("item1");
        listData1.add("item2");
        listData1.add("item3");
        List<String> listData2 = new ArrayList();
        listData2.add("item1");
        listData2.add("item2");
        List<String> listData3 = new ArrayList();
        listData3.add("item1");
        
        JSONListAdapter listAdapter1 = new JSONListAdapter();
        JSONListAdapter listAdapter2 = new JSONListAdapter();
        JSONListAdapter listAdapter3 = new JSONListAdapter();
        
        listAdapter1.setData(listData1);
        listAdapter2.setData(listData2);
        listAdapter3.setData(listData3);
        
        List<JSONListAdapter> listData = new ArrayList();
        listData.add(listAdapter1);
        listData.add(listAdapter2);
        listData.add(listAdapter3);
        
        JSONListAdapter testMapAdapter = new JSONListAdapter();
        testMapAdapter.setData(listData);
        testJSONResponse.setJSONAdapter(testMapAdapter);
        assertEquals(testJSONResponse.toString(), "{\"data\":[[\"item1\",\"item2\",\"item3\"],[\"item1\",\"item2\"],[\"item1\"]],\"message\":\"Testing JSONListAdapter\",\"status\":\"success\"}");
    }
    public void testJSONListAdapter_Null_Data_In_Adapter()
    {
        JsonUtils testJSONResponse = new JsonUtils();
        testJSONResponse.setMessage("Testing JSONListAdapter");
        testJSONResponse.setStatus("success");
        JSONListAdapter listAdapter = new JSONListAdapter();
        listAdapter.setData(null);
        testJSONResponse.setJSONAdapter(listAdapter);
        assertEquals(testJSONResponse.toString(), "{\"data\":[],\"message\":\"Testing JSONListAdapter\",\"status\":\"success\"}");
    }
    public void testJSONListAdapter_Null_Adapter_Case()
    {
        JsonUtils testJSONResponse = new JsonUtils();
        testJSONResponse.setMessage("Testing JSONListAdapter");
        testJSONResponse.setStatus("success");
        testJSONResponse.setJSONAdapter(null);
        assertEquals(testJSONResponse.toString(), "{\"data\":\"null\",\"message\":\"Testing JSONListAdapter\",\"status\":\"success\"}");
    }
    public void testJSONListAdapterTest_Base_Case()
    {
        JsonUtils testJSONResponse = new JsonUtils();
        testJSONResponse.setMessage("Testing JSONListAdapter");
        testJSONResponse.setStatus("success");
        
        List<String> listData = new ArrayList();
        listData.add("item1");
        listData.add("item2");
        listData.add("item3");
        JSONListAdapter listAdapter = new JSONListAdapter();
        listAdapter.setData(listData);
        
        //BASE TEST CASE
        testJSONResponse.setJSONAdapter(listAdapter);
        assertEquals(testJSONResponse.toString(), "{\"data\":[\"item1\",\"item2\",\"item3\"],\"message\":\"Testing JSONListAdapter\",\"status\":\"success\"}");
    }
    

    public void testJSONReadBody()
    {
        String goodJSON = "{\"data\": [\"Test1\",\"Test2\"],\"status\": \"Error, unknown Action!\"}";
        String badJSON  = "{\"data\": [\"Test1,\"Test2\"],\"status\": \"Error, unknown Action!\"}";
        JSONObject goodJSONObject = JsonUtils.readBody(goodJSON);
        JSONObject badJSONObject = JsonUtils.readBody(badJSON);
        if(goodJSONObject == null)
        {
            fail("Failed to load JSONObject from good json String");
        }
        else if(badJSONObject != null)
        {
            fail("Loaded JSONObject from bad json String.");
        }
    }
}
