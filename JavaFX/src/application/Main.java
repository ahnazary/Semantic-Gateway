package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.IntStream;

import org.apache.commons.math3.linear.MatrixUtils;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application {
	

	
	static final double [][][] TRAINING_DATA = {{{0.5555, 0.04175} , {+1}}, 							
												{{0.4165, 0.06217} , {+1}},
												{{0.4154, 0.05565} , {+1}},
												{{0.5239, 0.06456} , {+1}},
												{{0.4894, 0.04456} , {+1}},
												{{0.3495, 0.04456} , {+1}},
												{{0.4136, 0.03411} , {+1}},
												{{0.7794, 0.05411} , {+1}},
												{{0.4469, 0.07411} , {+1}},
												{{0.2269, 0.09411} , {+1}},
												{{0.2269, 0.05411} , {+1}},

												
												{{0.1465, 0.01789} , {-1}},
												{{0.1469, 0.00568} , {-1}},
												{{0.0462, 0.01567} , {-1}},
												{{0.0465, 0.00176} , {-1}},
												{{0.2654, 0.03519} , {-1}},
												{{0.2465, 0.02299} , {-1}},
												{{0.2796, 0.02274} , {-1}},
												{{0.3945, 0.01274} , {-1}},
												{{0.1954, 0.03238} , {-1}},
												{{0.6954, 0.00828} , {-1}},
												};
	static final double ZERO = 0.000000009;
	static SupportVec svm = null;
	static double x , y;
	
	public static double [][][] weightedTrainingData(double [][][] input){
		double maxx = 0;
		double maxy = 0;
		double[][][] result = new double[input.length][2][2];
		
		for(int i = 0; i < input.length; i++) {
				if(input[i][0][1] > maxy) {
					maxy = input[i][0][1];
				}
		}
		for(int i = 0; i < input.length; i++) {
			if(input[i][0][0] > maxx) {
				maxx = input[i][0][0];
			}	
		}
		double mult = maxx/maxy;
		
		for(int i = 0; i < input.length; i++) {
			result[i][0][0] = input[i][0][0];	
		}
		for(int i = 0; i < input.length; i++) {
			result[i][1][0] = input[i][1][0];	
		}
		for(int i = 0; i < input.length; i++) {
			result[i][0][1] = input[i][0][1] * mult;	
		}
		
		return result;
	}
	
	public static double  multiplier(double [][][] input){
		double maxx = 0;
		double maxy = 0;
		
		for(int i = 0; i < input.length; i++) {
				if(input[i][0][1] > maxy) {
					maxy = input[i][0][1];
				}
		}
		for(int i = 0; i < input.length; i++) {
			if(input[i][0][0] > maxx) {
				maxx = input[i][0][0];
			}	
		}
		double mult = maxx/maxy;
		return mult;
	}
	
	public void start(Stage stage) throws IOException {
		
		
		Platform.setImplicitExit(false);
		XYChart.Series<Number, Number> series01 = new XYChart.Series<Number,Number>();
		series01.setName("  -1   ");
		XYChart.Series<Number, Number> series02 = new XYChart.Series<Number,Number>();
		series02.setName("  +1   ");
		IntStream.range(0, Main.weightedTrainingData(TRAINING_DATA).length).forEach(i ->  {
			double x = Main.weightedTrainingData(TRAINING_DATA)[i][0][0] , y = Main.weightedTrainingData(TRAINING_DATA)[i][0][1];
			if(Main.weightedTrainingData(TRAINING_DATA)[i][1][0] == -1.0)
				series01.getData().add(new XYChart.Data<Number, Number>(x,y));
			else
				series02.getData().add(new XYChart.Data<Number, Number>(x,y));
		});
		//NumberAxis xAxis = new NumberAxis(0 , 10 , 1.0);
		NumberAxis xAxis = new NumberAxis(0 , 1 , 0.1);
		xAxis.setLabel(" Surface Similarity Feature");
		//NumberAxis yAxis = new NumberAxis(0 , 10 , 1.0);
		NumberAxis yAxis = new NumberAxis(0 , 1 , 0.1);
		yAxis.setLabel(" Popularity Feature");
		ScatterChart<Number, Number> scatterChart = new ScatterChart <Number, Number>(xAxis, yAxis);
		scatterChart.getData().add(series01);
		scatterChart.getData().add(series02);
		double m = -(svm.getW().getData()[0][0] / svm.getW().getData()[1][0]);
		double b = -(svm.getB()/ svm.getW().getData()[1][0]);
		double score1X = 0.00, score1Y = m*score1X+b, score2X = 10.00, score2Y = m*score2X+b;
		XYChart.Series<Number, Number> series03 = new XYChart.Series<Number, Number>();
		series03.getData().add(new XYChart.Data<Number,Number>(score1X,score1Y));
		series03.getData().add(new XYChart.Data<Number,Number>(score2X,score2Y));
		LineChart <Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
		lineChart.getData().add(series03);
		lineChart.setOpacity(0.4);
		Pane pane = new Pane();
		pane.getChildren().addAll(scatterChart,lineChart);
		stage.setScene(new Scene(pane, 550, 550	));
		stage.setOnHidden(e -> {try {handleCommandLine();} catch (Exception e1) {e1.printStackTrace();}});
		System.out.print("\nClose display window to proceed");
		stage.setTitle("Support Vector Machines ");
		stage.show();
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent e) {
		     Platform.exit();
		     System.exit(0);
		    }
		  });
	}
	
	public static void main(String[] args) throws IOException {
		double [][] xArray = new double [weightedTrainingData(TRAINING_DATA).length][2];
		double [][] yArray = new double [weightedTrainingData(TRAINING_DATA).length][1];
				
		for(int i = 0; i < TRAINING_DATA.length; i++ ) {
		
			xArray[i][0] = weightedTrainingData(TRAINING_DATA)[i][0][0];
			xArray[i][1] = weightedTrainingData(TRAINING_DATA)[i][0][1];
			yArray[i][0] = weightedTrainingData(TRAINING_DATA)[i][1][0];
		}
		
		svm = new SupportVec(MatrixUtils.createRealMatrix(xArray),MatrixUtils.createRealMatrix(yArray));
		displayInfoTables(xArray, yArray);
		//handleCommandLine();
		//Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Shutdown hook")));
		launch(args);
		
		
	}
	
	static void handleCommandLine() throws IOException{
		BufferedReader bufferedReader = new BufferedReader (new InputStreamReader(System.in));
		while(true) {
			System.out.println("\n> to classify new candidate enter scores 1 & 2 (or exit)");
			String[] values = (bufferedReader.readLine()).split(" ");
			if (values[0].equals("exit"))  
				System.exit(0);
			
			else {
				try {System.out.println(svm.classify(
						MatrixUtils.createRealMatrix(new double[][] {{Double.valueOf(values[0]) , Double.valueOf(values[1])*multiplier(TRAINING_DATA)}})));
				
						x = Double.valueOf(values[0]);
						y = Double.valueOf(values[1])*multiplier(TRAINING_DATA);
						System.out.println(" Distance to Decision Line is : " + distanceToLine(x , y));
				}
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
				System.out.println(Arrays.toString(xArray[i])+ " | " + ySB + " | " + new String(String.format("%.10f", svm.getAlpha().getData()[i][0])));
			}
		}
		System.out.println(" \n            wT            |   b  ");
		IntStream.range(0, 50).forEach(i -> System.out.print("-"));System.out.println();
		System.out.println("<" + (new String(String.format("%.9f", svm.getW().getData()[0][0])) + ", " +
								  new String(String.format("%.9f", svm.getW().getData()[1][0]))) + ">   | " + svm.getB());
		
		
	}
	public static double distanceToLine(double x, double y) {
		double result = 0.0;
		
		double a = (svm.getW().getData()[0][0]);
		double b = (svm.getW().getData()[1][0]);
		double c = (svm.getB());
		
		result = (Math.abs(a*x + b*y + c)) / (Math.sqrt(a*a+b*b));
		
		return result;
	}
	
}
