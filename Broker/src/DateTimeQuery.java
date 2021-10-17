import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.jena.rdf.model.RDFNode;

public class DateTimeQuery extends FeatureVector{

	public DateTimeQuery(String inputAddress, String modelAddress, String SVMMethod) throws IOException {
		super(inputAddress, modelAddress, SVMMethod);
		
	}
	protected void dateTimeQuery(String method) throws FileNotFoundException {
		
		for (int i = 0; i < JSONPairs.size(); i++) {
			String value = String.valueOf(JSONPairs.get(i).values().toArray()[0]);
			
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
						
						Map<RDFNode, float[]> singleWordMap = new HashMap<RDFNode, float[]>();
						singleWordMap = resultsMap(sarefQueryFileExact, model, 1f);
						
						for (Entry<RDFNode, float[]> pair : singleWordMap.entrySet()) {
							
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
						
						double highestDistance = 0.0;
						Map<RDFNode, float[]> tempApprovedURIs = new HashMap<RDFNode, float[]>();
						
						for (Entry<RDFNode, float[]> pair : singleWordMap.entrySet()) {
							if(isValidURI(pair.getKey())) {
								if(method == "WSVM" && WeightedSVM.distanceToLine(pair.getValue()[0],pair.getValue()[1]*WeightedSVM.multiplier(TRAINING_DATA)) > highestDistance && 
										WeightedSVM.classificationResult(MatrixUtils.createRealMatrix(new double[][] {{pair.getValue()[0] , pair.getValue()[1]*WeightedSVM.multiplier(TRAINING_DATA)}})) == 1)
								{
									highestDistance = WeightedSVM.distanceToLine(pair.getValue()[0],pair.getValue()[1]*WeightedSVM.multiplier(TRAINING_DATA));
									tempApprovedURIs.clear();
									tempApprovedURIs.put(pair.getKey(), pair.getValue());
									
								}	
								else if(method == "WSVM" && WeightedSVM.distanceToLine(pair.getValue()[0],pair.getValue()[1]*WeightedSVM.multiplier(TRAINING_DATA)) == highestDistance && 
										WeightedSVM.classificationResult(MatrixUtils.createRealMatrix(new double[][] {{pair.getValue()[0] , pair.getValue()[1]*WeightedSVM.multiplier(TRAINING_DATA)}})) == 1)
								{
									highestDistance = WeightedSVM.distanceToLine(pair.getValue()[0],pair.getValue()[1]*WeightedSVM.multiplier(TRAINING_DATA));
									tempApprovedURIs.put(pair.getKey(), pair.getValue());
								}	
								else if(method == "SVM" && SVM.distanceToLine(pair.getValue()[0],pair.getValue()[1]) > highestDistance && 
										SVM.classificationResult(MatrixUtils.createRealMatrix(new double[][] {{pair.getValue()[0] , pair.getValue()[1]}})) == 1)
								{
									highestDistance = WeightedSVM.distanceToLine(pair.getValue()[0],pair.getValue()[1]*WeightedSVM.multiplier(TRAINING_DATA));
									tempApprovedURIs.clear();
									tempApprovedURIs.put(pair.getKey(), pair.getValue());
									
								}	
								else if(method == "SVM" && SVM.distanceToLine(pair.getValue()[0],pair.getValue()[1]*WeightedSVM.multiplier(TRAINING_DATA)) == highestDistance && 
										SVM.classificationResult(MatrixUtils.createRealMatrix(new double[][] {{pair.getValue()[0] , pair.getValue()[1]}})) == 1)
								{
									highestDistance = WeightedSVM.distanceToLine(pair.getValue()[0],pair.getValue()[1]*WeightedSVM.multiplier(TRAINING_DATA));
									tempApprovedURIs.put(pair.getKey(), pair.getValue());
								}	
							}
						}
						
						for (Entry<RDFNode, float[]> pair : tempApprovedURIs.entrySet()) {
							approvedURIs.put(pair.getKey(), pair.getValue());
							if(!isClassNode(pair.getKey())) {
								ArrayList <RDFNode> temp = getClassNode(pair.getKey());
								for(int p = 0;p < temp.size(); p++) {
									approvedURIs.put(temp.get(p), pair.getValue());
								}							
							}					
						}
					}
				}
			}
		}

	//this method queries over the model and generates the result array which stores all the URIs and is used to calculate popularity feature
	protected void generatedateTimeResultsArr() throws FileNotFoundException {
		
		for (int i = 0; i < JSONPairs.size(); i++) {
			String value = String.valueOf(JSONPairs.get(i).values().toArray()[0]);
			
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
	
}
