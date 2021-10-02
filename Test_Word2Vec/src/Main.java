
import scala.Tuple2;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) {

		    SparkSession spark = SparkSession
		      .builder()
		      .appName("JavaWordCount")
		      .getOrCreate();

		    JavaRDD<String> lines = spark.read().textFile(args[0]).javaRDD();
		    JavaRDD<String> textFile = sc.textFile("hdfs://...");
		    JavaPairRDD<String, Integer> counts = textFile
		    	    .flatMap(s -> Arrays.asList(s.split(" ")).iterator())
		    	    .mapToPair(word -> new Tuple2<>(word, 1))
		    	    .reduceByKey((a, b) -> a + b);
		    	counts.saveAsTextFile("hdfs://...");

	}
	
	
}
