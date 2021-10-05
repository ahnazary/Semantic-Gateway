import java.io.IOException;
import java.text.ParseException;

public class Main {
	
	public static void main(String[] args) throws IOException, ParseException {
	
		FeatureVector Test = new FeatureVector("input","saref.ttl");
		Test.morphemesQuery("WSVM");  //use "WSVM" or "SVM" as the method for SVM	
		Test.dateTimeQuery("WSVM");
		
		System.out.println("Total number of URIs is : " + Test.getURIs().size());

//		ReadJSON rs = new ReadJSON("input_3");
//		rs.printOutKeys();
//		
//		WriteJSON Sensors = new WriteJSON();
//		Sensors.writeJSONFile();
		
	}			
}
