import java.io.IOException;
import java.text.ParseException;

import org.apache.spark.sql.SparkSession;

public class Main {

	public static void main(String[] args) throws IOException, ParseException {
	
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
		
		
		SparkSession spark = SparkSession
			      .builder()
			      .appName("JavaWord2VecExample")
			      .getOrCreate();
			    // $example on$
			    // Input data: Each row is a bag of words from a sentence or document.
//			    List<Row> data = Arrays.asList(
//			      RowFactory.create(Arrays.asList("Hi I heard about Spark".split(" "))),
//			      RowFactory.create(Arrays.asList("I wish Java could use case classes".split(" "))),
//			      RowFactory.create(Arrays.asList("Logistic regression models are neat".split(" ")))
//			    );
//			    StructType schema = new StructType(new StructField[]{
//			      new StructField("text", new ArrayType(DataTypes.StringType, true), false, Metadata.empty())
//			    });
//			    
//			    Dataset<Row> documentDF = spark.createDataFrame(data, schema);
//			    
//			    // Learn a mapping from words to Vectors.
//			    Word2Vec word2Vec = new Word2Vec()
//			      .setInputCol("text")
//			      .setOutputCol("result")
//			      .setVectorSize(3)
//			      .setMinCount(0);
//			    Word2VecModel model = word2Vec.fit(documentDF);
//			    Dataset<Row> result = model.transform(documentDF);
//			    for (Row row : result.collectAsList()) {
//			      List<String> text = row.getList(0);
//			      Vector vector = (Vector) row.get(1);
//			      System.out.println("Text: " + text + " => \nVector: " + vector + "\n");
//			    }
//			    // $example off$
//			    spark.stop();
//		
	}	
	
	
	
}
