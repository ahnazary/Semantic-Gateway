import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class SupportVec {
	private RealMatrix x, y;
	static final double MIN_ALPHA_OPTIMIZATION = 0.00001;
	static final int MAX_NUMB_OF_ITERATION = 50;
	private RealMatrix alpha;
	static final double EPSILON = 0.001;
	static final double C = 1.0;
	private RealMatrix w;
	private double b = 0;
	
	public SupportVec (RealMatrix x, RealMatrix y){
		this.x = x; 
		this.y = y;
		double[] alphaArray = new double[x.getData().length];
		IntStream.range(0, alphaArray.length).forEach(i -> alphaArray[i] = 0);
		alpha = MatrixUtils.createColumnRealMatrix(alphaArray);
		int i = 0;
		while (i < MAX_NUMB_OF_ITERATION) {
			if(performSMO() == 0)	
				i += 1;
			else 
				i = 0;
		}
		w = calcW();
	}
	
	private int performSMO() {
		int numberOfAlphaPairsOptimized = 0;
		
		for	(int i = 0 ; i < x.getData().length ; i++) {
			RealMatrix Ei = mult(y,alpha).transpose()
					.multiply(x.multiply(x.getRowMatrix(i).transpose()))
					.scalarAdd(b)
					.subtract(y.getRowMatrix(i));
			if(checkIfAlphaViolatesKKT(alpha.getEntry(i, 0), Ei.getEntry(0, 0))) {
				int j = selectIndexOf2ndAlphaToOptimize(i,x.getData().length);
				RealMatrix Ej = mult(y,alpha).transpose()
						.multiply(x.multiply(x.getRowMatrix(j).transpose()))
						.scalarAdd(b)
						.subtract(y.getRowMatrix(j));
				
				double alphaIold = alpha.getRowMatrix(i).getEntry(0, 0);
				double alphaJold = alpha.getRowMatrix(j).getEntry(0, 0);
				double[] bounds = boundAlpha(alpha.getEntry(i, 0), alpha.getEntry(j, 0),y.getEntry(i, 0), y.getEntry(j, 0));
				double ETA = x.getRowMatrix(i).multiply(x.getRowMatrix(j).transpose()).scalarMultiply(2.0).getEntry(0, 0)
						-x.getRowMatrix(i).multiply(x.getRowMatrix(i).transpose()).getEntry(0, 0)
						-x.getRowMatrix(j).multiply(x.getRowMatrix(j).transpose()).getEntry(0, 0);
				
				if(bounds[0] != bounds[1] && ETA < 0) {
					if(optimize1AlphaPair(i,j,Ei.getEntry(0, 0), Ej.getEntry(0, 0),  ETA,  bounds, alphaIold,  alphaJold)) {
						optimizeB(Ei.getEntry(0, 0), Ej.getEntry(0, 0), alphaIold, alphaJold, i,j);
						numberOfAlphaPairsOptimized += 1;
					}	
				}	
			}
		}
		return numberOfAlphaPairsOptimized;
	}
	
	private boolean optimize1AlphaPair(int i, int j, double Ei, double Ej, double ETA, double[] bounds, double alphaIold, double alphaJold) {
		boolean flag = false;
		alpha.setEntry(j, 0, alpha.getEntry(j, 0)-y.getEntry(j, 0)*(Ei-Ej)/ETA);
		clipAlphaJ(j,bounds[1],bounds[0]);
		if(Math.abs(alpha.getEntry(j, 0) - alphaJold) >= MIN_ALPHA_OPTIMIZATION) {
			optimizeAlphaISameAsAlphaJOppositeDirection(i,j,alphaJold);
			flag = true;
		}
		return flag;
	}
	
	private void optimizeAlphaISameAsAlphaJOppositeDirection(int i, int j, double alphaJold) {
		
		alpha.setEntry(i, 0, alpha.getEntry(i, 0) + y.getEntry(j, 0)*y.getEntry(i, 0)*(alphaJold - alpha.getEntry(j, 0)));
	}
	
	private void optimizeB(double Ei, double Ej, double alphaIold, double alphaJold, int i, int j) {
		double b1 = b - Ei - mult(y.getRowMatrix(i),alpha.getRowMatrix(i).scalarAdd(-alphaIold))
							.multiply(x.getRowMatrix(i).multiply(x.getRowMatrix(i).transpose())).getEntry(0, 0)
							-mult(y.getRowMatrix(j),alpha.getRowMatrix(j).scalarAdd(-alphaJold))
							.multiply(x.getRowMatrix(i).multiply(x.getRowMatrix(j).transpose())).getEntry(0, 0);
		double b2 = b - Ej - mult(y.getRowMatrix(i),alpha.getRowMatrix(i).scalarAdd(-alphaIold))
							.multiply(x.getRowMatrix(i).multiply(x.getRowMatrix(j).transpose())).getEntry(0, 0)
							-mult(y.getRowMatrix(j),alpha.getRowMatrix(j).scalarAdd(-alphaJold))
							.multiply(x.getRowMatrix(j).multiply(x.getRowMatrix(j).transpose())).getEntry(0, 0);
		
		if(0 < alpha.getRowMatrix(i).getEntry(0, 0) && C > alpha.getRowMatrix(i).getEntry(0, 0))
			b = b1;
		else if(0 < alpha.getRowMatrix(j).getEntry(0, 0) && C > alpha.getRowMatrix(j).getEntry(0, 0))	
			b = b2;
		else
			b = (b1 + b2) / 2.0;
		
	}
	
	private void clipAlphaJ(int j, double highBound, double lowBound) {
		if(alpha.getEntry(j, 0) < lowBound)
			alpha.setEntry(j, 0, lowBound);
		if(alpha.getEntry(j, 0) > highBound)
			alpha.setEntry(j, 0, highBound);
	}
	
	private boolean checkIfAlphaViolatesKKT(double alpha, double e) {
		return (alpha > 0 && Math.abs(e) < EPSILON) || (alpha < C && Math.abs(e) > EPSILON);
	}
	
	private double[] boundAlpha(double alphaI, double alphaJ, double yI, double yJ) {
		
		double[] bounds = new double[2];
		if(yI == yJ) {
			bounds[0] = Math.max(0 , alphaJ + alphaI - C);
			bounds[1] = Math.min(C, alphaJ + alphaI);
		}
		else {
			bounds[0] = Math.max(0 , alphaJ - alphaI );
			bounds[1] = Math.min(C, alphaJ - alphaI + C);
			
		}
		return bounds;
	}
	
	private int selectIndexOf2ndAlphaToOptimize(int indexOf1stAlpha , int numbOfRows) {
		
		int indexOf2ndAlpha = indexOf1stAlpha;
		while(indexOf1stAlpha == indexOf2ndAlpha )
			indexOf2ndAlpha = ThreadLocalRandom.current().nextInt(0,numbOfRows-1);
		return indexOf2ndAlpha;
	}
	
	private RealMatrix calcW() {
		
		double[][] wArray = new double [x.getData()[0].length][1];
		IntStream.range(0, wArray.length).forEach(i ->wArray[i][0] = 0.0);
		RealMatrix w = MatrixUtils.createRealMatrix(wArray);
		for(int i = 0; i < x.getData().length ; i++) 		
			w = w.add(x.getRowMatrix(i).transpose()
					.scalarMultiply(y.getRowMatrix(i).multiply(alpha.getRowMatrix(i)).getEntry(0, 0)));	
		
		return w;
	}
	
	public String classify(RealMatrix entry) {
		String classification = "classified as -1 (will not be hired prediction)";
		if(Math.signum(entry.multiply(w).getEntry(0, 0)+b) == 1)
			classification = "classified as 1 (will be hired prediction)";
		
		return classification;
		
	}
	
	
	static RealMatrix mult(RealMatrix matrix1, RealMatrix matrix2) {
		double[][] returnData = new double [matrix1.getData().length][matrix1.getData()[0].length];
		IntStream.range(0, matrix1.getData().length).forEach(r ->
			IntStream.range(0, matrix1.getData()[0].length).forEach(c ->
					returnData[r][c] = matrix1.getEntry(r, c)*matrix2.getEntry(r, c)));
		
		return MatrixUtils.createRealMatrix(returnData);
		
	}
	public RealMatrix getAlpha() {
		return alpha;
	}
	public RealMatrix getW(){
		return w;
	}
	public double getB() {
		return b;
	}
}
