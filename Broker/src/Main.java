import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;

//import org.json.simple.parser.ParseException;

public class Main {

	public static void main(String[] args) throws IOException, ParseException, java.text.ParseException{
	
		SurfaceForm Test = new SurfaceForm("input","saref.ttl");
		Test.exactQuery();
		//Test.morphemesQuery();
		
		
		//WriteJSON Sensors = new WriteJSON();
		//Sensors.writeJSONFile();
		
		//ReadJSON rs = new ReadJSON("/home/amirhossein/Documents/GitHub/semantic-broker/Broker/input_3");
		//rs.returnKeys();
		System.out.println(isValidDate("20-01-2014"));
        System.out.println(isValidDate("22:01:33"));
        System.out.println(isValidDate("32476347656435"));
		
	}	
	
	public static boolean isValidDate(String inDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
}
