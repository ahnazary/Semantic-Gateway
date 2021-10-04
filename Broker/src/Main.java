import java.io.IOException;
import java.text.ParseException;

import org.apache.commons.math3.linear.MatrixUtils;

public class Main {
	
	public static final String TEXT_RESET = "\u001B[0m";
	public static final String TEXT_BLACK = "\u001B[30m";
	public static final String TEXT_RED = "\u001B[31m";
	public static final String TEXT_GREEN = "\u001B[32m";
	public static final String TEXT_YELLOW = "\u001B[33m";
	public static final String TEXT_BLUE = "\u001B[34m";
	public static final String TEXT_PURPLE = "\u001B[35m";
	public static final String TEXT_CYAN = "\u001B[36m";
	public static final String TEXT_WHITE = "\u001B[37m";
	
	static final double [][][] TRAINING_DATA = {{{1.0, 0.10} , {+1}}, 							
			{{1.0, 0.09} , {+1}},
			{{0.9, 0.09} , {+1}},
			{{0.9, 0.10} , {+1}},
			{{0.1, 0.01} , {-1}},
			{{0.1, 0.00} , {-1}},
			{{0.0, 0.01} , {-1}},
			{{0.0, 0.00} , {-1}},
			};

	public static void main(String[] args) throws IOException, ParseException {
	
		System.out.println(TEXT_RED + "This text is red!" + TEXT_RESET);
		FeatureVector Test = new FeatureVector("input","saref.ttl");
		Test.morphemesQuery("WSVM");  //use "WSVM" or "SVM" as the method for SVM	
		
		
		System.out.println((char)27 + "[31m" + "ERROR MESSAGE IN RED");
//		ReadJSON rs = new ReadJSON("input_3");
//		rs.printOutKeys();
//		
//		WriteJSON Sensors = new WriteJSON();
//		Sensors.writeJSONFile();
		
	}			
}
