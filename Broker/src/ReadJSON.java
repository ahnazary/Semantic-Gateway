import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ReadJSON {
	

	String fileAddress;
	String input;
	private static ArrayList<String> keylist = new ArrayList<String>();

	private static ArrayList<HashMap<String, Object>> JSONPairs = new ArrayList<HashMap<String,Object>>();
	
	public ReadJSON(String fileAddress) throws IOException {
		
		this.fileAddress = fileAddress;
		input = readFile(fileAddress);
	}
	
	private static void myfunction(JSONObject x) throws JSONException
    {
  
		
		JSONArray keys =  x.names();
		
        for(int i=0;i<keys.length();i++)
        {
            String current_key = keys.get(i).toString();   
            if( x.get(current_key).getClass().getName().equals("org.json.JSONObject"))
            {
                keylist.add(current_key);
                myfunction((JSONObject) x.get(current_key));
               
            } 
            else if( x.get(current_key).getClass().getName().equals("org.json.JSONArray"))
            {
                for(int j=0;j<((JSONArray) x.get(current_key)).length();j++)
                {
                    if(((JSONArray) x.get(current_key)).get(j).getClass().getName().equals("org.json.JSONObject"))
                    {
                        keylist.add(current_key);
                        myfunction((JSONObject)((JSONArray) x.get(current_key)).get(j));
                        
                    }
                }
            }
            else 
            {     
            	
            	HashMap<String, Object> tempMap = new HashMap<String, Object>();
            	tempMap.put(current_key, x.get(current_key));
            	JSONPairs.add(tempMap);
            	
            	keylist.add(current_key);
            	
            	
            	//String s1=Boolean.toString(x.getBoolean(current_key));
//            	try {
//            		if(x.get(current_key) instanceof Boolean)
//            			System.out.println(x.getBoolean(current_key));
//            		
//            		if(x.get(current_key) instanceof String)
//            			System.out.println(x.getString(current_key));
//            		
//            		if(x.get(current_key) instanceof Integer)
//            			System.out.println(x.getInt(current_key));           		
//            	}
//            	
//            	catch (Exception e) {
//            		System.out.println( e );
//            	}               
            }
        }
    }

	public ArrayList<HashMap<String, Object>>getJSONPairs() {		
			return JSONPairs;        
	}
	
	public void printOutKeys() throws IOException, ParseException {
		
		JSONObject jsonObject = new JSONObject(input);
		myfunction(jsonObject);
		System.out.println(keylist);
        
	}
	
	public ArrayList<String> getKeys(){
		
		JSONObject jsonObject = new JSONObject(input);
		myfunction(jsonObject);
		return keylist;		
	}
	
	private String readFile(String filePath) throws IOException {
	    BufferedReader reader = new BufferedReader(new FileReader (filePath));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    try {
	        while((line = reader.readLine()) != null) {
	            stringBuilder.append(line);
	            stringBuilder.append(ls);
	        }

	        return stringBuilder.toString();
	    } finally {
	        reader.close();
	    }
	}
}


