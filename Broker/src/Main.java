
import java.io.IOException;

import java.text.ParseException;



public class Main {
	
	static final double [][][] TRAINING_DATA = {{{1, 1} , {-1}}, 							
												{{1, 5} , {-1}},
												{{5, 1} , {+1}},
												{{5, 5} , {+1}}};

	public static void main(String[] args) throws IOException, ParseException {
	
		
//		final SupportVec svm = new SupportVec(TRAINING_DATA);
//		svm.displayInfoTables();
//		svm.handleCommandLine();
		
		
		SurfaceForm Test = new SurfaceForm("input","saref.ttl");
		//Test.exactQuery();
		Test.morphemesQuery();
//		
//		
//		WriteJSON Sensors = new WriteJSON();
//		Sensors.writeJSONFile();
//		
//		ReadJSON rs = new ReadJSON("input_3");
//		rs.printOutKeys();
		
	}			
}
