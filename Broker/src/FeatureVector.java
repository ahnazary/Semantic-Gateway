import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.FileManager;

public class FeatureVector {
	String inputAddress;
	String modelAddress;
	static String input;
	private final Model model;
	private static ArrayList<RDFNode> URIs = new ArrayList<RDFNode>();
	private static ArrayList<String> keyList = new ArrayList<String>();
	private static ArrayList<HashMap<String, Object>> JSONPairs = new ArrayList<HashMap<String,Object>>();
	
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


	@SuppressWarnings("deprecation")
	public FeatureVector(String inputAddress, String modelAddress) throws IOException {
		this.inputAddress = inputAddress;
		this.modelAddress = modelAddress;
		FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
		model = FileManager.get().loadModel(modelAddress); // model that query request is sent to
		input = readFile(inputAddress);
		ReadJSON rs = new ReadJSON(inputAddress);
		keyList = rs.getKeys();
		JSONPairs = rs.getJSONPairs();
		final SVM svm = new SVM(TRAINING_DATA);
		final WeightedSVM wsvm = new WeightedSVM(TRAINING_DATA);
		
		generateMorphemesResultsArr();
		generatedateTimeResultsArr();
	}
	
	public void dateTimeQuery(String method) throws FileNotFoundException {
	
	for (int i = 0; i < JSONPairs.size(); i++) {
		String value = (String) JSONPairs.get(i).values().toArray()[0];
		
		if(isValidDate(value)) {
			String[] words = {"date", "time"};
			System.out.println(JSONPairs.get(i));
			
			for(int j = 0 ; j < words.length ; j++) {
				String sarefQueryFileExact = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
						+ "PREFIX om: <http://www.wurvoc.org/vocabularies/om-1.8/> "
						+ "PREFIX owl: <http://www.w3.org/2002/07/owl#> "
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
						+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
						+ "PREFIX time: <http://www.w3.org/2006/time#> "
						+ "PREFIX saref: <https://w3id.org/saref#>  " + "PREFIX schema: <http://schema.org/>  "
						+ "PREFIX dcterms: <http://purl.org/dc/terms/>  "

						+ "SELECT ?subject \n" + "WHERE\n" + "{\n" + "{?subject ?predicate ?object}"
						// +"filter (contains(str(?object), \""+word+"\") || contains(str(?subject),
						// \""+word+"\") || contains(str(?predicate), \""+word+"\"))"
						// +"FILTER (regex(?object, \""+word+"\", \"i\" ) || regex(?predicate,
						// \""+word+"\", \"i\" ) || regex(?subject, \""+word+"\", \"i\" )) "
						// +"filter (contains(str(?object), \""+word+"\"))"
						+ "FILTER regex(?object, \"" + words[j] + "\", \"i\" ) " + "}";
					
					Map<RDFNode, float[]> map = new HashMap<RDFNode, float[]>();
					map = resultsMap(sarefQueryFileExact, model, 1f);
					
					for (Entry<RDFNode, float[]> pair : map.entrySet()) {
						
						if(method == "SVM" && SVM.classificationResult(MatrixUtils.createRealMatrix(new double[][] {{pair.getValue()[0] , pair.getValue()[1]}})) == 1) {
						System.out.println("    Approved by SVM");
						System.out.println(String.format("     Key (URI) is: %s     Value (features Vector) is : %s",
										pair.getKey() + "\n", Arrays.toString(pair.getValue()) + "\n"));					
						}
						
						else if (method == "SVM"){
							System.out.println("    Not Approved by SVM");
							System.out.println(String.format("     Key (URI) is: %s     Value (features Vector) is : %s",
											pair.getKey() + "\n", Arrays.toString(pair.getValue()) + "\n"));
						}
						else if(method == "WSVM" && WeightedSVM.classificationResult(MatrixUtils.createRealMatrix(new double[][] {{pair.getValue()[0] , pair.getValue()[1]*WeightedSVM.multiplier(TRAINING_DATA)}})) == 1) {
							System.out.println("    Approved by Weighted SVM");
							System.out.println(String.format("     Key (URI) is: %s     Value (features Vector) is : %s",
											pair.getKey() + "\n", Arrays.toString(pair.getValue()) + "\n"));					
						}
						
						else if (method == "WSVM"){
							System.out.println("    Not Approved by Weighted SVM");
							System.out.println(String.format("     Key (URI) is: %s     Value (features Vector) is : %s",
											pair.getKey() + "\n", Arrays.toString(pair.getValue()) + "\n"));
							
						}
					}
				}
			}
		}
	}
	
	public void morphemesQuery(String method) throws FileNotFoundException {

		
		System.out.println("Total numebr of results is : " + URIs.size() + "\n");
		
		for (int i = 0; i < JSONPairs.size(); i++) {
			String word = (String) JSONPairs.get(i).keySet().toArray()[0];
			//String word = keyList.get(i);
			//System.out.println(word);
			System.out.println(JSONPairs.get(i));
			char wordArr[] = new char[word.length()];

			for (int j = 0; j < word.length(); j++) {
				wordArr[j] = word.charAt(j);
			}

			for (int j = 0; j < word.length(); j++) {
				for (int k = j + 3; k < word.length(); k++) {
					String morphemes = "";

					for (int z = j; z <= k; z++) {
						morphemes += wordArr[z];
					}

					String sarefQueryFileExact = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
							+ "PREFIX om: <http://www.wurvoc.org/vocabularies/om-1.8/> "
							+ "PREFIX owl: <http://www.w3.org/2002/07/owl#> "
							+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
							+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
							+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
							+ "PREFIX time: <http://www.w3.org/2006/time#> "
							+ "PREFIX saref: <https://w3id.org/saref#>  " + "PREFIX schema: <http://schema.org/>  "
							+ "PREFIX dcterms: <http://purl.org/dc/terms/>  "

							+ "SELECT ?subject \n" + "WHERE\n" + "{\n" + "{?subject ?predicate ?object}"
							// +"filter (contains(str(?object), \""+word+"\") || contains(str(?subject),
							// \""+word+"\") || contains(str(?predicate), \""+word+"\"))"
							// +"FILTER (regex(?object, \""+word+"\", \"i\" ) || regex(?predicate,
							// \""+word+"\", \"i\" ) || regex(?subject, \""+word+"\", \"i\" )) "
							// +"filter (contains(str(?object), \""+word+"\"))"
							+ "FILTER regex(?object, \"" + morphemes + "\", \"i\" ) " + "}";
					String sarefQueryFile = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
							+ "PREFIX om: <http://www.wurvoc.org/vocabularies/om-1.8/> "
							+ "PREFIX owl: <http://www.w3.org/2002/07/owl#> "
							+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
							+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
							+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
							+ "PREFIX time: <http://www.w3.org/2006/time#> "
							+ "PREFIX saref: <https://w3id.org/saref#>  " + "PREFIX schema: <http://schema.org/>  "
							+ "PREFIX dcterms: <http://purl.org/dc/terms/>  "

							+ "SELECT ?subject \n" + "WHERE\n" + "{\n" + "{?subject ?predicate ?object}"

							// +"filter (contains(str(?object), \""+word+"\") || contains(str(?subject),
							// \""+word+"\") || contains(str(?predicate), \""+word+"\"))"
							// +"FILTER (regex(?object, \""+word+"\", \"i\" ) || regex(?predicate,
							// \""+word+"\", \"i\" ) || regex(?subject, \""+word+"\", \"i\" )) "
							// +"filter (contains(str(?object), '"+morphemes+"'))"
							// +"FILTER regex(?object, \" "+morphemes+" \", \"i\" ) "
							+ "FILTER regex(?object, \" " + morphemes + " \", \"i\" ) "
							// +"FILTER regex(?object, \"\\b"+morphemes+"\\b\" ) "
							// +"FILTER regex(?object,'"+morphemes+"') "
							+ "}";

					Map<RDFNode, float[]> map = new HashMap<RDFNode, float[]>();
					if (j == 0 && k == word.length() - 1) {

						map = resultsMap(sarefQueryFileExact, model, 1f);
						System.out.println(morphemes);

						for (Entry<RDFNode, float[]> pair : map.entrySet()) {
							
							if(method == "SVM" && SVM.classificationResult(MatrixUtils.createRealMatrix(new double[][] {{pair.getValue()[0] , pair.getValue()[1]}})) == 1) {
							System.out.println("    Approved by SVM");
							System.out.println(String.format("     Key (URI) is: %s     Value (features Vector) is : %s",
											pair.getKey() + "\n", Arrays.toString(pair.getValue()) + "\n"));					
							}
							
							else if (method == "SVM"){
								System.out.println("    Not Approved by SVM");
								System.out.println(String.format("     Key (URI) is: %s     Value (features Vector) is : %s",
												pair.getKey() + "\n", Arrays.toString(pair.getValue()) + "\n"));
							}
							else if(method == "WSVM" && WeightedSVM.classificationResult(MatrixUtils.createRealMatrix(new double[][] {{pair.getValue()[0] , pair.getValue()[1]*WeightedSVM.multiplier(TRAINING_DATA)}})) == 1) {
								System.out.println("    Approved by Weighted SVM");
								System.out.println(String.format("     Key (URI) is: %s     Value (features Vector) is : %s",
												pair.getKey() + "\n", Arrays.toString(pair.getValue()) + "\n"));					
							}
							
							else if (method == "WSVM"){
								System.out.println("    Not Approved by Weighted SVM");
								System.out.println(String.format("     Key (URI) is: %s     Value (features Vector) is : %s",
												pair.getKey() + "\n", Arrays.toString(pair.getValue()) + "\n"));
							}
						}
						
					} 
					else {

						map = resultsMap(sarefQueryFile, model, surfaceSimilarity(word, morphemes));
						System.out.println(morphemes);
						for (Entry<RDFNode, float[]> pair : map.entrySet()) {
							
							if(method == "SVM" && SVM.classificationResult(MatrixUtils.createRealMatrix(new double[][] {{pair.getValue()[0] , pair.getValue()[1]}})) == 1) {
							System.out.println("    Approved by SVM");
							System.out.println(String.format("     Key (URI) is: %s     Value (features Vector) is : %s",
											pair.getKey() + "\n", Arrays.toString(pair.getValue()) + "\n"));
							}
							else if(method == "SVM"){
								System.out.println("    Not Approved by SVM");
								System.out.println(String.format("     Key (URI) is: %s     Value (features Vector) is : %s",
												pair.getKey() + "\n", Arrays.toString(pair.getValue()) + "\n"));
							}
							else if(method == "WSVM" && WeightedSVM.classificationResult(MatrixUtils.createRealMatrix(new double[][] {{pair.getValue()[0] , pair.getValue()[1]*WeightedSVM.multiplier(TRAINING_DATA)}})) == 1) {
								System.out.println("    Approved by Weighted SVM" );
								System.out.println(String.format("     Key (URI) is: %s     Value (features Vector) is : %s",
												pair.getKey() + "\n", Arrays.toString(pair.getValue()) + "\n"));
							}
							else if(method == "WSVM"){
								System.out.println("    Not Approved by weighted SVM");
								System.out.println(String.format("     Key (URI) is: %s     Value (features Vector) is : %s",
												pair.getKey() + "\n", Arrays.toString(pair.getValue()) + "\n"));
							}
						}
					}
				}
			}
		}
	}

	// all of the URIs generated are stored in this ArrayList
	private ArrayList<RDFNode> resultsArr(String inputQuery, Model model) {
		Query query = QueryFactory.create(inputQuery);

		QueryExecution qExe = QueryExecutionFactory.create(query, model);
		ResultSet resultsOutput = qExe.execSelect();

		for (; resultsOutput.hasNext();) {

			QuerySolution soln = resultsOutput.nextSolution();
			RDFNode subject = soln.get("subject");
			URIs.add(subject);
		}
		return URIs;
	}

	// this Map saves a specific URI with its corresponding FeatureVector
	private Map<RDFNode, float[]> resultsMap(String inputQuery, Model model, float similarityFeature) {
		Query query = QueryFactory.create(inputQuery);

		QueryExecution qExe = QueryExecutionFactory.create(query, model);
		ResultSet resultsOutput = qExe.execSelect();

		Map<RDFNode, float[]> map = new HashMap<RDFNode, float[]>();

		for (; resultsOutput.hasNext();) {

			QuerySolution soln = resultsOutput.nextSolution();
			RDFNode subject = soln.get("subject");

			float popularity = popularity(subject, URIs);

			float[] floatArray = { similarityFeature, popularity };
			map.put(subject, floatArray);
		}
		return map;
	}

	//this method queries over the model and generates the result array which stores all the URIs and is used to calculate popularity feature
	private void generateMorphemesResultsArr() throws FileNotFoundException {

		for (int i = 0; i < keyList.size(); i++) {
			String word = keyList.get(i);
			char wordArr[] = new char[word.length()];

			for (int j = 0; j < word.length(); j++) {
				wordArr[j] = word.charAt(j);
			}

			for (int j = 0; j < word.length(); j++) {
				for (int k = j + 3; k < word.length(); k++) {
					String morphemes = "";

					for (int z = j; z <= k; z++) {
						morphemes += wordArr[z];
					}

					String sarefQueryFileExact = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
							+ "PREFIX om: <http://www.wurvoc.org/vocabularies/om-1.8/> "
							+ "PREFIX owl: <http://www.w3.org/2002/07/owl#> "
							+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
							+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
							+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
							+ "PREFIX time: <http://www.w3.org/2006/time#> "
							+ "PREFIX saref: <https://w3id.org/saref#>  " + "PREFIX schema: <http://schema.org/>  "
							+ "PREFIX dcterms: <http://purl.org/dc/terms/>  "

							+ "SELECT ?subject \n" + "WHERE\n" + "{\n" + "{?subject ?predicate ?object}"
							// +"filter (contains(str(?object), \""+word+"\") || contains(str(?subject),
							// \""+word+"\") || contains(str(?predicate), \""+word+"\"))"
							// +"FILTER (regex(?object, \""+word+"\", \"i\" ) || regex(?predicate,
							// \""+word+"\", \"i\" ) || regex(?subject, \""+word+"\", \"i\" )) "
							// +"filter (contains(str(?object), \""+word+"\"))"
							+ "FILTER regex(?object, \"" + morphemes + "\", \"i\" ) " + "}";
					String sarefQueryFile = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
							+ "PREFIX om: <http://www.wurvoc.org/vocabularies/om-1.8/> "
							+ "PREFIX owl: <http://www.w3.org/2002/07/owl#> "
							+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
							+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
							+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
							+ "PREFIX time: <http://www.w3.org/2006/time#> "
							+ "PREFIX saref: <https://w3id.org/saref#>  " + "PREFIX schema: <http://schema.org/>  "
							+ "PREFIX dcterms: <http://purl.org/dc/terms/>  "

							+ "SELECT ?subject \n" + "WHERE\n" + "{\n" + "{?subject ?predicate ?object}"

							// +"filter (contains(str(?object), \""+word+"\") || contains(str(?subject),
							// \""+word+"\") || contains(str(?predicate), \""+word+"\"))"
							// +"FILTER (regex(?object, \""+word+"\", \"i\" ) || regex(?predicate,
							// \""+word+"\", \"i\" ) || regex(?subject, \""+word+"\", \"i\" )) "
							// +"filter (contains(str(?object), '"+morphemes+"'))"
							+ "FILTER regex(?object, \" " + morphemes + " \", \"i\" ) "
							// +"FILTER regex(?object, \"\\b"+morphemes+"\\b\" ) "
							// +"FILTER regex(?object,'"+morphemes+"') "
							+ "}";

					if (j == 0 && k == word.length() - 1) {
						URIs = resultsArr(sarefQueryFileExact, model);
					} else {
						URIs = resultsArr(sarefQueryFile, model);
					}
				}
			}
		}
	}

	private void generatedateTimeResultsArr() throws FileNotFoundException {
		
		for (int i = 0; i < JSONPairs.size(); i++) {
			String value = (String) JSONPairs.get(i).values().toArray()[0];
			
			if(isValidDate(value)) {
				String[] words = {"date", "time"};
				
				for(int j = 0 ; j < words.length ; j++) {
					String sarefQueryFileExact = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
							+ "PREFIX om: <http://www.wurvoc.org/vocabularies/om-1.8/> "
							+ "PREFIX owl: <http://www.w3.org/2002/07/owl#> "
							+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
							+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
							+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
							+ "PREFIX time: <http://www.w3.org/2006/time#> "
							+ "PREFIX saref: <https://w3id.org/saref#>  " + "PREFIX schema: <http://schema.org/>  "
							+ "PREFIX dcterms: <http://purl.org/dc/terms/>  "

							+ "SELECT ?subject \n" + "WHERE\n" + "{\n" + "{?subject ?predicate ?object}"
							// +"filter (contains(str(?object), \""+word+"\") || contains(str(?subject),
							// \""+word+"\") || contains(str(?predicate), \""+word+"\"))"
							// +"FILTER (regex(?object, \""+word+"\", \"i\" ) || regex(?predicate,
							// \""+word+"\", \"i\" ) || regex(?subject, \""+word+"\", \"i\" )) "
							// +"filter (contains(str(?object), \""+word+"\"))"
							+ "FILTER regex(?object, \"" + words[j] + "\", \"i\" ) " + "}";
					
					URIs = resultsArr(sarefQueryFileExact, model);
					}
				}
			}
		}
		
	private float surfaceSimilarity(String word, String morphemes) {

		float z = ((float) morphemes.length()) / ((float) word.length());
		return z;

	}

	private float popularity(RDFNode URI, ArrayList<RDFNode> URIs) {

		int repetition = 0;
		for (int i = 0; i < URIs.size(); i++) {
			if (URI.equals(URIs.get(i))) {
				repetition++;
			}
		}

		float result;
		result = ((float) repetition) / ((float) URIs.size());
		return result;
	}

	private static void appendStrToFile(String filePath, String str) {
		try (FileWriter fw = new FileWriter("Output", true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println(str);

		} catch (IOException e) {
			System.out.println("Exception Occurred" + e);
		}
	}

	private static boolean isValidDate(String inDate) {

		boolean result = false;
		;
		ArrayList<String> validFormats = new ArrayList<String>();
		validFormats.add("HH:mm:ss");
		validFormats.add("dd-MM-yyyy HH:mm:ss:ms");
		validFormats.add("dd-MM-yyyy HH:mm:ss");
		validFormats.add("dd-MM-yyyy HH:mm");
		validFormats.add("dd-MM-yyyy");

		for (int i = 0; i < validFormats.size(); i++) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(validFormats.get(i));
			dateFormat.setLenient(false);
			try {
				dateFormat.parse(inDate.trim());
			} catch (ParseException pe) {

				if (i == validFormats.size())
					result = false;

				else
					continue;
			}
			result = true;
			break;
		}
		return result;
	}
	
	private String readFile(String filePath) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		try {
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}

			return stringBuilder.toString();
		} finally {
			reader.close();
		}
	}

	public ArrayList<RDFNode> getURIs() {
		return URIs;	
	}
}

