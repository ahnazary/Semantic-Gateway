import java.io.File;
import java.io.IOException;
import java.text.ParseException;

public class Main {
	
	static final double [][][] TRAINING_DATA = {{{1, 1} , {-1}}, 							
												{{1, 5} , {-1}},
												{{5, 1} , {+1}},
												{{5, 5} , {+1}}};

	public static void main(String[] args) throws IOException, ParseException {
	
		
//		final SVM svm = new SVM(TRAINING_DATA);
//		svm.displayInfoTables();
//		svm.handleCommandLine();
		
		
		FeatureVector Test = new FeatureVector("input","saref.ttl");
		File file = new File("Output");
		file.delete();
		Test.exactQuery();
		Test.morphemesQuery();
		
//		ReadJSON rs = new ReadJSON("input_3");
//		rs.printOutKeys();
//		
//		WriteJSON Sensors = new WriteJSON();
//		Sensors.writeJSONFile();
		
	}			
}
