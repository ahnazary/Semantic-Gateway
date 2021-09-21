import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Arrays;
import java.util.stream.IntStream;


import org.apache.commons.math3.linear.MatrixUtils;

public class Main {
	
	static final double [][][] TRAINING_DATA = {{{1, 1} , {-1}}, 							
												{{1, 5} , {-1}},
												{{5, 1} , {+1}},
												{{5, 5} , {+1}}};
	static final double ZERO = 0.000000009;
	static SupportVec svm = null;

	public static void main(String[] args) throws IOException, ParseException {
	
		
		double [][] xArray = new double [TRAINING_DATA.length][2];
		double [][] yArray = new double [TRAINING_DATA.length][1];
		
		for(int i = 0; i < TRAINING_DATA.length; i++ ) {
		
			xArray[i][0] = TRAINING_DATA[i][0][0];
			xArray[i][1] = TRAINING_DATA[i][0][1];
			yArray[i][0] = TRAINING_DATA[i][1][0];
		}
		
		svm = new SupportVec(MatrixUtils.createRealMatrix(xArray),MatrixUtils.createRealMatrix(yArray));
		displayInfoTables(xArray, yArray);
		handleCommandLine();
		
		
//		SurfaceForm Test = new SurfaceForm("input","saref.ttl");
//		Test.exactQuery();
//		Test.morphemesQuery();
//		
//		
//		WriteJSON Sensors = new WriteJSON();
//		Sensors.writeJSONFile();
//		
//		ReadJSON rs = new ReadJSON("/home/amirhossein/Documents/GitHub/semantic-broker/Broker/input_3");
//		rs.returnKeys();
		
	}			
	
	static void handleCommandLine() throws IOException{
		BufferedReader bufferedReader = new BufferedReader (new InputStreamReader(System.in));
		while(true) {
			System.out.println("\n> to classify new candidate enter scores for intervies 1 & 2 (or exit)");
			String[] values = (bufferedReader.readLine()).split(" ");
			if (values[0].equals("exit"))  
				System.exit(0);
			
			else {
				try {System.out.println(svm.classify(
						MatrixUtils.createRealMatrix(new double[][] {{Double.valueOf(values[0]) , Double.valueOf(values[1])}})));}
				catch(Exception e) {System.out.println("invalid input"); }
			}
		}
	}
	
	static void displayInfoTables(double[][] xArray, double[][] yArray) {
		System.out.println("    Support vector    | label  | alpha");
		IntStream.range(0, 50).forEach(i -> System.out.print("-"));System.out.println();
		for(int i = 0 ; i < xArray.length ; i++) {
			if(svm.getAlpha().getData()[i][0] > ZERO && svm.getAlpha().getData()[i][0] != SupportVec.C) {
				StringBuffer ySB = new StringBuffer(String.valueOf(yArray[i][0]));
				ySB.setLength(5);
				System.out.println(Arrays.toString(xArray[i])+ " | " + ySB+ " | " + new String(String.format("%.10f", svm.getAlpha().getData()[i][0])));
			}
		}
		System.out.println(" \n            wT            |   b  ");
		IntStream.range(0, 50).forEach(i -> System.out.print("-"));System.out.println();
		System.out.println("<" + (new String(String.format("%.9f", svm.getW().getData()[0][0])) + ", " +
								  new String(String.format("%.9f", svm.getW().getData()[1][0]))) + ">   | " + svm.getB());
	}
}
