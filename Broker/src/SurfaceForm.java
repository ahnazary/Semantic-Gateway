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

public class SurfaceForm {
	String inputAddress;
	String modelAddress;
	static Model model;
	static String input;
	
	public SurfaceForm(String inputAddress,String modelAddress) throws IOException {
		this.inputAddress = inputAddress;
		this.modelAddress = modelAddress;
		//FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
		model = FileManager.get().loadModel(modelAddress); // model that query request is sent to
		input = readFile("/home/amirhossein/Documents/GitHub/semantic-broker/Broker/input"); 
		
	}
	
	private static void sendQueryRequest(String inputQuery, Model model) throws FileNotFoundException {
		 
		
        Query query = QueryFactory.create(inputQuery); //inputString is the query above

        QueryExecution qExe = QueryExecutionFactory.create( query, model);
        ResultSet resultsOutput = qExe.execSelect();
        if (!resultsOutput.hasNext())	
        	System.out.println("Resultset is empty \n");
        
        if(resultsOutput.hasNext()) {
	        try {
	        	//FileOutputStream queryOutput = new FileOutputStream("Query Result.txt");
	            ResultSetFormatter.out(System.out, resultsOutput);
	            //System.out.println("\n");
	   			//ResultSetFormatter.out(queryOutput, resultsOutput);  
	            
	        }
	   
	        finally {
	            qExe.close();
	            
	        }
        }
	}
	
	private String readFile(String filePath) throws IOException {
	    BufferedReader reader = new BufferedReader(new FileReader (filePath));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    try {
	        while((line = reader.readLine()) != null) {
	            stringBuilder.append(line);
	            stringBuilder.append(ls);
	        }

	        return stringBuilder.toString();
	    } finally {
	        reader.close();
	    }
	}
	
	public void exactQuery() throws FileNotFoundException {
				
				String[] lines = input.split("\\r?\\n");
				 for (String line: lines) {
					 int i = 0;
					 String[] words = line.split("\\W+");
					 for (String word : words) {
						 if(i == 1) {
							 System. out. println(word);
							 String sarefQuery = 
									 "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
									+"PREFIX om: <http://www.wurvoc.org/vocabularies/om-1.8/> "
									+"PREFIX owl: <http://www.w3.org/2002/07/owl#> "
									+"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
									+"PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
									+"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
									+"PREFIX time: <http://www.w3.org/2006/time#> "
									+"PREFIX saref: <https://w3id.org/saref#>  "
									+"PREFIX schema: <http://schema.org/>  "
									+"PREFIX dcterms: <http://purl.org/dc/terms/>  "
			
									+"SELECT ?subject ?predicate ?object\n"
							 		+ "WHERE\n"
							 		+ "{\n"
							 		+"{?subject ?predicate ?object}"
							 		//+"filter (contains(str(?object), \""+word+"\") || contains(str(?subject), \""+word+"\") || contains(str(?predicate), \""+word+"\"))"
							 		//+"FILTER (regex(?object, \""+word+"\", \"i\" ) || regex(?predicate, \""+word+"\", \"i\" ) || regex(?subject, \""+word+"\", \"i\" )) "
							 		//+"filter (contains(str(?object), \""+word+"\"))"
							 		+"FILTER regex(?object, \""+word+"\", \"i\" ) "
							 		+""
							 		+ "}";
							 //System.out.println(word);
			 		 
							 sendQueryRequest(sarefQuery, model);				 
						 }
						 i++;
					 }
				 }	
			}	

	public void morphemesQuery() throws FileNotFoundException {


		
		String[] lines = input.split("\\r?\\n");
		 for (String line: lines) {
			 int i = 0;
			 String[] words = line.split("\\W+");
			 for (String word : words) {
				 if(i == 1) {
					 System. out. println(word);
					 char wordArr[] = new char[word.length()];
			
					 for(int j = 0; j < word.length(); j++) {
						 
						 wordArr[j] = word.charAt(j);
						 //System. out. println(wordArr[j]);
						 
					 }
					 
					 for(int j = 0; j < word.length(); j++) {
						 for(int k = j + 3; k < word.length();k++) {
							 if (j == 0 && k == word.length() -1)
								 continue;
							 
							 //StringBuilder sb = new StringBuilder(); 
							 String morphemes = "";
							 for(int z = j; z <= k ; z++) {
								 //sb.append(wordArr[k]);
								 morphemes += wordArr[z];
							 }
							 //String morphemes = sb.toString();
							 System. out. println(morphemes);
							 String sarefQuery = 
									 "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
									+"PREFIX om: <http://www.wurvoc.org/vocabularies/om-1.8/> "
									+"PREFIX owl: <http://www.w3.org/2002/07/owl#> "
									+"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
									+"PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
									+"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
									+"PREFIX time: <http://www.w3.org/2006/time#> "
									+"PREFIX saref: <https://w3id.org/saref#>  "
									+"PREFIX schema: <http://schema.org/>  "
									+"PREFIX dcterms: <http://purl.org/dc/terms/>  "
			
									+"SELECT ?subject ?predicate ?object\n"
							 		+ "WHERE\n"
							 		+ "{\n"
							 		+"{?subject ?predicate ?object}"
							 		//+"filter (contains(str(?object), \""+word+"\") || contains(str(?subject), \""+word+"\") || contains(str(?predicate), \""+word+"\"))"
							 		//+"FILTER (regex(?object, \""+word+"\", \"i\" ) || regex(?predicate, \""+word+"\", \"i\" ) || regex(?subject, \""+word+"\", \"i\" )) "
							 		//+"filter (contains(str(?object), \""+morphemes+"\"))"
							 		+"FILTER regex(?object, \""+morphemes+"\", \"i\" ) "
							 		+""
							 		+ "}";
							 //System.out.println(word);
							 
							 if(hasMeaning(morphemes)) {
								 System.out.println('"' + morphemes + '"' + " has meaning ");
								 sendQueryRequest(sarefQuery, model);
								 System.out.println("Surface Similarity Feature is : " + surfaceSimilarity(word,morphemes) + "\n\n");
							 }
							 
							 else
								 System.out.println('"' + morphemes + '"' + " does not mean anything \n");
						 }
					 }	
					
							 
						
				 }
				 i++;
			 }
		 }
		
	}
	
	private static boolean hasMeaning(String word) {
				
				String queryStr = 
						 "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
						+"PREFIX om: <http://www.wurvoc.org/vocabularies/om-1.8/> "
						+"PREFIX owl: <http://www.w3.org/2002/07/owl#> "
						+"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+"PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
						+"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
						+"PREFIX time: <http://www.w3.org/2006/time#> "
						+"PREFIX saref: <https://w3id.org/saref#>  "
						+"PREFIX schema: <http://schema.org/>  "
						+"PREFIX dcterms: <http://purl.org/dc/terms/>  "
		
						+"SELECT *\n"
						+ "WHERE\n"
						+ "     {\n"
						+ "        ?subject rdfs:label \""+word+"\"@en.\n"
						+ "     }";
				
						Query query = QueryFactory.create(queryStr); //inputString is the query above
				
				        QueryExecution qExe = QueryExecutionFactory.sparqlService( "https://dbpedia.org/sparql/" , query);
				        ResultSet resultsOutput = qExe.execSelect();
				        if (resultsOutput.hasNext())
				        	return true;
				        
				        else 
				        	return false;
				        	
				
				
			} 

	private static double surfaceSimilarity(String word, String morphemes) {
			
			float z = ((float)morphemes.length())/((float)word.length());
			return z;
					
		}

	private static void generateTriples() {
		String s = "Sensor";
		Model m = ModelFactory.createDefaultModel();
		Resource animal1 = m.createResource(s).addProperty(VCARD.FN, "LION");
		//m.write(System.out,"Turtle");
		
	}
}

	
