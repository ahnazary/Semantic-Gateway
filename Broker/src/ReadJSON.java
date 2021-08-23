import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReadJSON {
	
	
	String fileAddress;
	String input;
	public static ArrayList<String> keylist = new ArrayList<String>();
	
	public ReadJSON(String fileAddress) throws IOException {
		
		this.fileAddress = fileAddress;
		input = readFile(fileAddress);
	}
	
	public static void myfunction(JSONObject x) throws JSONException
    {
        org.json.JSONArray keys =  x.names();
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
                for(int j=0;j<((JSONArray) x.get(current_key)).size();j++)
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
                keylist.add(current_key);
            }
        }
    }
	
	public void returnKeys() throws IOException, ParseException {
		
		JSONObject jsonObject = new JSONObject(input);
		myfunction(jsonObject);
        System.out.println(keylist);
        
//		JSONObject jsonObject = new JSONObject(input);
//        Iterator<String> keys = jsonObject.keys();
//        while(keys.hasNext()) {
//            String key = keys.next();
//            System.out.println(key);
//            if(jsonObject.get(key) instanceof JSONArray) {
//                JSONArray array = (JSONArray) jsonObject.get(key);
//                JSONObject object = (JSONObject) array.get(0);
//                Iterator<String> innerKeys = object.keys();
//                while(innerKeys.hasNext()) {
//                    String innerKey = innerKeys.next();
//                    System.out.println(innerKey);
//                }
//            }
//        }

//		JSONParser jsonParser = new JSONParser();
//		FileReader reader = new FileReader("/home/amirhossein/Documents/GitHub/semantic-broker/Broker/Sensors.json");
//		Object obj = jsonParser.parse(reader);
//		 
//        JSONArray employeeList = (JSONArray) obj;
//        System.out.println(employeeList);
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


