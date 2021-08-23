import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class WriteJSON {
	@SuppressWarnings("unchecked")
	public void writeJSONFile() {
		
		//First Sensor
        JSONObject sensor1Details = new JSONObject();
        sensor1Details.put("Platform", "ArduinoBase1");
        sensor1Details.put("Observation", "TemperatureObservation");
        sensor1Details.put("Sensor", "TMP102");
        sensor1Details.put("ObservationValue", "22");
        sensor1Details.put("ObservationUnit", "Celsius");
         
        JSONObject sensor1 = new JSONObject(); 
        sensor1.put("Sensor 1", sensor1Details);
         
        //Second Sensor
        JSONObject sensor2Details = new JSONObject();
        sensor2Details.put("Platform", "ArduinoBase1");
        sensor2Details.put("Observation", "LightObservation");
        sensor2Details.put("Sensor", "TMP1345");
        sensor2Details.put("ObservationValue", "10");
        sensor2Details.put("ObservationUnit", "Kilograms");
         
        JSONObject sensor2 = new JSONObject(); 
        sensor2.put("Sensor 2", sensor2Details);
         
        //Add sensors to list
        JSONArray sensors = new JSONArray();
        sensors.add(sensor1);
        sensors.add(sensor2);
         
        //Write JSON file
        try (FileWriter file = new FileWriter("Sensors.json")) {
          
            file.write(sensors.toJSONString()); 
            file.flush();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
}
