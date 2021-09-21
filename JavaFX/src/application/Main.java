package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.IntStream;

import org.apache.commons.math3.linear.MatrixUtils;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Main extends Application {
	
	static final double [][][] TRAINING_DATA = {{{9.123456, 3.123456} , {+1}}, 
												{{9.123456, 5.123456} , {+1}},
												{{5.123456, 5.123456} , {-1}},
												{{8.123456, 6.123456} , {+1}},
												{{4.123456, 4.123456} , {-1}},
												{{2.123456, 4.123456} , {-1}},
												{{9.123456, 7.123456} , {+1}},
												{{4.123456, 4.123456} , {-1}},
												{{8.123456, 2.123456} , {+1}},
												{{2.123456, 2.123456} , {-1}},
												{{3.123456, 3.123456} , {-1}},
												{{8.123456, 4.123456} , {+1}},
												{{7.123456, 6.123456} , {+1}},
												{{4.123456, 7.123456} , {-1}},
												{{6.123456, 4.123456} , {-1}},
												{{8.123456, 5.123456} , {+1}},
												{{3.123456, 4.123456} , {-1}}};
	
	
//	static final double [][][] TRAINING_DATA = {{{1, 1} , {+1}}, 							
//			{{1, 5} , {+1}},
//			{{5, 1} , {-1}},
//			{{5, 5} , {-1}}};
	static final double ZERO = 0.000000009;
	static SupportVec svm = null;
	static double x , y;
	
	public void start(Stage stage) throws IOException {
		Platform.setImplicitExit(false);
		XYChart.Series<Number, Number> series01 = new XYChart.Series<Number,Number>();
		series01.setName("  -1   ");
		XYChart.Series<Number, Number> series02 = new XYChart.Series<Number,Number>();
		series02.setName("  +1   ");
		IntStream.range(0, Main.TRAINING_DATA.length).forEach(i ->  {
			double x = Main.TRAINING_DATA[i][0][0] , y = Main.TRAINING_DATA[i][0][1];
			if(Main.TRAINING_DATA[i][1][0] == -1.0)
				series01.getData().add(new XYChart.Data<Number, Number>(x,y));
			else
				series02.getData().add(new XYChart.Data<Number, Number>(x,y));
		});
		NumberAxis xAxis = new NumberAxis(0 , 10 , 1.0);
		xAxis.setLabel(" ");
		NumberAxis yAxis = new NumberAxis(0 , 10 , 1.0);
		xAxis.setLabel(" ");
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
	}
	
	public static void main(String[] args) throws IOException {
		double [][] xArray = new double [TRAINING_DATA.length][2];
		double [][] yArray = new double [TRAINING_DATA.length][1];
		
		for(int i = 0; i < TRAINING_DATA.length; i++ ) {
		
			xArray[i][0] = TRAINING_DATA[i][0][0];
			xArray[i][1] = TRAINING_DATA[i][0][1];
			yArray[i][0] = TRAINING_DATA[i][1][0];
		}
		
		svm = new SupportVec(MatrixUtils.createRealMatrix(xArray),MatrixUtils.createRealMatrix(yArray));
		displayInfoTables(xArray, yArray);
		//handleCommandLine();
		
		launch();
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
						MatrixUtils.createRealMatrix(new double[][] {{Double.valueOf(values[0]) , Double.valueOf(values[1])}})));
						x = Double.valueOf(values[0]);
						y = Double.valueOf(values[1]);
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
		
		double a = -(svm.getW().getData()[0][0]);
		double b = svm.getW().getData()[1][0];
		double c = -(svm.getB());
		
		result = (Math.abs(a*x + b*y + c)) / (Math.sqrt(a*a+b*b));
		
		return result;
	}
	
}
