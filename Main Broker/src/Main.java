import java.io.IOException;
import java.text.ParseException;

public class Main {

	public static void main(String[] args) throws IOException, ParseException {
	
		SurfaceForm Test = new SurfaceForm("input","saref.ttl");
		Test.exactQuery();
		Test.morphemesQuery();
		
		
		//WriteJSON Sensors = new WriteJSON();
		//Sensors.writeJSONFile();
		
		//ReadJSON rs = new ReadJSON("input_3");
		//rs.returnKeys();

	}	
	
	
}
