import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.VCARD;


public class Main {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
		
		SurfaceForm Test = new SurfaceForm("/home/amirhossein/Documents/GitHub/semantic-broker/Broker/input","/home/amirhossein/Documents/GitHub/semantic-broker/Broker/saref.ttl");
		
		Test.exactQuery();
		Test.morphemesQuery();
		
       
	}

}