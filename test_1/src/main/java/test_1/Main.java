package test_1;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		SparkConf conf = new SparkConf().setAppName("Line_Count");
		
		JavaSparkContext ctx = new JavaSparkContext(conf);
		
		JavaRDD<String> textLoadRDD = ctx.textFile("/home/amirhossein/Documents/GitHub/Semantic-Gateway/Broker/input_3");
		
		System.out.println(textLoadRDD.count());
	}

}
;