package Utils;

import java.io.IOException;
import java.io.Reader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public interface JsonUtils {
    public static JSONObject readBody(Reader input)
    {
    	JSONParser parser = new JSONParser();
        try {
            return (JSONObject) parser.parse(input);
        } catch (IOException | ParseException ex) {
            return null;
        }
    }
    public static JSONObject readBody(String input)
    {
    	JSONParser parser = new JSONParser();
        try {
            return (JSONObject) parser.parse(input);
        } catch (ParseException ex) {
            return null;
        }
    }
}
