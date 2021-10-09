import java.io.IOException;
import java.text.ParseException;

public class Main {
	
	static final double [][][] TRAINING_DATA = {{{1,1} , {-1}}, 							
												{{1, 5} , {+1}},
												{{5, 1} , {-1}},
												{{5, 5} , {+1}},
												};
	
	public static void main(String[] args) throws IOException, ParseException {
	
		FeatureVector Test = new FeatureVector("input","saref.ttl", "WSVM"); // 3rd field should be either "WSVM" or "SVM"
		Test.start();
			
//		WriteJSON Sensors = new WriteJSON();
//		Sensors.writeJSONFile();
		
//		WeightedSVM wsvm = new WeightedSVM(TRAINING_DATA);
//		wsvm.displayInfoTables();
//		wsvm.handleCommandLine();


	}			
}
