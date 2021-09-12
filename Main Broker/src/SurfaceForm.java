import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.VCARD;

public class SurfaceForm {
	String inputAddress;
	String modelAddress;
	static Model model;
	static ArrayList<RDFNode> URIs = new ArrayList<RDFNode>();

	static String input;
	
	@SuppressWarnings("deprecation")
	public SurfaceForm(String inputAddress,String modelAddress) throws IOException {
		this.inputAddress = inputAddress;
		this.modelAddress = modelAddress;
		FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
		model = FileManager.get().loadModel(modelAddress); // model that query request is sent to
		input = readFile("/home/amirhossein/Documents/GitHub/semantic-broker/Broker/input"); 
			
	}	
	
	private ArrayList<RDFNode> resultsArr(String inputQuery, Model model){
		Query query = QueryFactory.create(inputQuery); 
		
        QueryExecution qExe = QueryExecutionFactory.create( query, model);
        ResultSet resultsOutput = qExe.execSelect();
              
        for ( ; resultsOutput.hasNext() ; )
        {
  
         QuerySolution soln = resultsOutput.nextSolution() ;     
         RDFNode subject = soln.get("subject");
         URIs.add(subject);
         //System.out.println(URIs);
         //System.out.println(subject);
        }
        //System.out.println(map);
        return URIs;		
	}
	
	private float popularity(RDFNode URI, ArrayList<RDFNode> URIs) {
		
		int repetition = 0;
		for(int i = 0; i < URIs.size(); i++) {
			if(URI.equals(URIs.get(i))) {
				repetition++;
			}
		}	
		
		float result;
		result = ((float)repetition) / ((float)URIs.size());
		return result;
	}
	
	private Map<RDFNode , float[]> resultsMap(String inputQuery, Model model, float similarityFeature){
		Query query = QueryFactory.create(inputQuery); 
		
        QueryExecution qExe = QueryExecutionFactory.create( query, model);
        ResultSet resultsOutput = qExe.execSelect();
        
        Map<RDFNode, float[]> map = new HashMap<RDFNode, float[]>();
        
        for ( ; resultsOutput.hasNext() ; )
        {
  
         QuerySolution soln = resultsOutput.nextSolution() ;     
         RDFNode subject = soln.get("subject");     
         System.out.println(subject);
         System.out.println(" surface similarity Feature is : " + similarityFeature);
         System.out.println(" popularity is : "  + popularity(subject, resultsArr(inputQuery,model)) + " \n");
         
         float[] floatArray = {2.0f,1.5f,8.45f,116.77f};
         map.put(subject, floatArray);
        }
        //System.out.println(map);
        return map;		
	}
	
	
	private void sendQueryRequestConsole (String inputQuery, Model model) throws FileNotFoundException {
			 
			
	        Query query = QueryFactory.create(inputQuery); 
	
	        QueryExecution qExe = QueryExecutionFactory.create( query, model);
	        ResultSet resultsOutput = qExe.execSelect();
	        
	        if (!resultsOutput.hasNext())	
	        	System.out.println("Resultset is empty");
	        
	        if(resultsOutput.hasNext()) {
		        try {
		            ResultSetFormatter.out(System.out, resultsOutput);		            
		        }
		   
		        finally {
		            qExe.close();
		            
		        }
	        }       
	}
	
	private String sendQueryRequestFile(String inputQuery, Model model) throws FileNotFoundException {
		 
		
        Query query = QueryFactory.create(inputQuery); 

        QueryExecution qExe = QueryExecutionFactory.create( query, model);
        
        ResultSet resultsOutput = qExe.execSelect();
        String resultsOutputStr = ResultSetFormatter.asText(resultsOutput);
        
        return resultsOutputStr;
        
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
							 String sarefQueryConsole = 
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
							 
							 String sarefQueryFile = 
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
			
									+"SELECT ?subject \n"
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
							 
							 appendStrToFile("Output",word);
							 appendStrToFile("Output",sendQueryRequestFile(sarefQueryFile, model));		
							 //sendQueryRequestConsole(sarefQueryConsole, model);
							 //System.out.println("Surface Similarity Feature is : 1" + "\n\n");
							 
							 //System.out.println(resultsArr(sarefQueryFile, model));
							 System.out.println(resultsMap(sarefQueryFile, model, (float)1));
						
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
	 
							 String morphemes = "";
							 for(int z = j; z <= k ; z++) {
								 //sb.append(wordArr[k]);
								 morphemes += wordArr[z];
							 }
							 String sarefQueryFile = 
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
			
									+"SELECT ?subject \n"
							 		+ "WHERE\n"
							 		+ "{\n"
							 		+"{?subject ?predicate ?object}"
							 		
							 		//+"filter (contains(str(?object), \""+word+"\") || contains(str(?subject), \""+word+"\") || contains(str(?predicate), \""+word+"\"))"
							 		//+"FILTER (regex(?object, \""+word+"\", \"i\" ) || regex(?predicate, \""+word+"\", \"i\" ) || regex(?subject, \""+word+"\", \"i\" )) "
							 		//+"filter (contains(str(?object), '"+morphemes+"'))"
							 		+"FILTER regex(?object, \" "+morphemes+" \", \"i\" ) "
							 		//+"FILTER regex(?object, \"\\b"+morphemes+"\\b\" ) "
							 		//+"FILTER regex(?object,'"+morphemes+"') "
							 		+ "}";
							
							 String sarefQueryConsole = 
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
							 		//+"filter (contains(str(?object), '"+morphemes+"'))"
							 		+"FILTER regex(?object, \" "+morphemes+" \", \"i\" ) "
							 		//+"FILTER regex(?object, \"\\b"+morphemes+"\\b\" ) "
							 		//+"FILTER regex(?object,'"+morphemes+"') "
							 		+ "}";
							 
							 appendStrToFile("Output",morphemes);
							 if(sendQueryRequestFile(sarefQueryFile, model) != "")
								 appendStrToFile("Output",sendQueryRequestFile(sarefQueryFile, model));					
							 
							 System.out.println('"' + morphemes + '"' );
							 //sendQueryRequestConsole(sarefQueryConsole, model);
							 //System.out.println("Surface Similarity Feature is : " + surfaceSimilarity(word,morphemes) + "\n\n");
							 
							 System.out.println(resultsMap(sarefQueryFile, model, (float)surfaceSimilarity(word,morphemes)) + "\n");
						 }
					 }	
	
				 }
				 i++;
			 }
		 }
		
	}	
	
	private boolean hasMeaning(String word) {
				
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

	private double surfaceSimilarity(String word, String morphemes) {
			
			float z = ((float)morphemes.length())/((float)word.length());
			return z;
					
		}

	private static void appendStrToFile(String filePath, String str)
		{
			try(FileWriter fw = new FileWriter("Output", true);
    	    BufferedWriter bw = new BufferedWriter(fw);
    	    PrintWriter out = new PrintWriter(bw))
    	{
    	    out.println(str);
    	   
    	} catch (IOException e) {
    		System.out.println("Exception Occurred" + e);
    		}
		}
	
	public static boolean isValidDate(String inDate){
		
			boolean result = false;;
			ArrayList <String>validFormats = new ArrayList <String>();
			validFormats.add("HH:mm:ss");
			validFormats.add("dd-MM-yyyy HH:mm:ss:ms"); 
			validFormats.add("dd-MM-yyyy HH:mm:ss"); 
			validFormats.add("dd-MM-yyyy HH:mm"); 
			validFormats.add("dd-MM-yyyy"); 
			
			for(int i = 0 ; i < validFormats.size() ; i++) {
				SimpleDateFormat dateFormat = new SimpleDateFormat(validFormats.get(i));
				dateFormat.setLenient(false);
		        try {
		            dateFormat.parse(inDate.trim());
		        } 
		        catch (ParseException pe) {
		           
		            if(i == validFormats.size())
		            	 result = false;
		            
		            else
		            	continue;
		        }
		        result = true;
		        break;
			}    
			return result;
	    }
	
	private void generateTriples() {
		String s = "Sensor";
		Model m = ModelFactory.createDefaultModel();
		Resource animal1 = m.createResource(s).addProperty(VCARD.FN, "LION");
		m.write(System.out,"Turtle");
		
	}
}

	
