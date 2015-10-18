package utilities;
import java.util.HashSet;
import java.util.List;
import java.util.HashMap;
import java.util.Set;


public class Data {
	//This hashmap will contain the row id and the corresponding features for the row 
	private HashMap<Integer,FeatureRow> trainingRows ;
	private int lastInsertedTestRow ;
	public int getLastInsertedTestRow() {
		return lastInsertedTestRow;
	}
	public HashMap<Integer, FeatureRow> getTrainingRows() {
		return trainingRows;
	}
	public void setTrainingRows(HashMap<Integer, FeatureRow> trainingRows) {
		this.trainingRows = trainingRows;
	}
	public HashMap<Integer, FeatureRow> getTestRows() {
		return testRows;
	}
	public void setTestRows(HashMap<Integer, FeatureRow> testRows) {
		this.testRows = testRows;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getLastInsertedRow() {
		return lastInsertedRow;
	}
	public void setLastInsertedRow(int lastInsertedRow) {
		this.lastInsertedRow = lastInsertedRow;
	}
	public HashMap<Integer, HashMap<String, Integer>> getCategoricalValues() {
		return categoricalValues;
	}
	public void setCategoricalValues(
			HashMap<Integer, HashMap<String, Integer>> categoricalValues) {
		this.categoricalValues = categoricalValues;
	}

	public HashMap<String, List<Integer>> getClasswisePredictions() {
		return classwisePredictions;
	}
	public void setClasswisePredictions(HashMap<String, List<Integer>> classwisePredictions) {
		this.classwisePredictions = classwisePredictions;
	}

	private HashMap<String, List<Integer>> classwisePredictions ; 
	private HashMap<Integer,FeatureRow> testRows ;
	//type of data - train / test 
	private String type ;
	private int lastInsertedRow ; 
	public int getAttributeCount() {
		return attributeCount;
	}
	public void setAttributeCount(int attributeCount) {
		this.attributeCount = attributeCount;
	}
	private int attributeCount; 
	//Categorical data information for the dataset. 
	/*
	 * This hashmap will hold the column index and a list of values for this categorical variable. 
	 */
	public Set<String> classLabelSet = new HashSet<String>();
	
	public Set<String> getClassLabelSet() {
		return classLabelSet;
	}
	public void setClassLabelSet(Set<String> classLabelSet) {
		this.classLabelSet = classLabelSet;
	}

	public HashMap<Integer , HashMap<String, Integer> > categoricalValues; 
	public void AddCategoricalValuesForColumn(int colIndex, HashMap<String, Integer> values){
		//Adds categorical data information for this data. 
		this.categoricalValues.put(colIndex, values);
	}
	public void PrintSummary(){
		System.out.println("Data Summary => " );
		System.out.println("Total training instances "+ trainingRows.size());
		System.out.println("Categorical Attributes Count : " + categoricalValues.size());
	}
	public Data(String type){
		this.type = type ;
		this.trainingRows = new HashMap<Integer, FeatureRow>(); 
		this.categoricalValues = new HashMap<Integer, HashMap<String,Integer>>(); 
		this.testRows = new HashMap<>(); 
		lastInsertedRow = 0; 
		lastInsertedTestRow = 0 ;  
	}
	public void AddRow(FeatureRow r){
		if ( r != null){
			this.trainingRows.put(lastInsertedRow++, r); 
		}
		this.setAttributeCount(r.getFeatures().size());
	}
	public void AddTestRow(FeatureRow r ){
		if ( r != null){
			this.testRows.put(lastInsertedTestRow++, r); 
		}
	}
	
	public void PrintData(String type){
		if (type == "train"){
			for( int key : trainingRows.keySet()){
				//Fetch and print each row.
				FeatureRow fr;
				System.out.print("Row-" + (key) + " | "); 
				if ( (fr = trainingRows.get(key)) != null){
					//Print each row
					fr.PrintRow("train");
				}
			}
		}
		else{
			for( int key : testRows.keySet()){
				//Fetch and print each row.
				FeatureRow fr;
				System.out.print("Row-" + (key) + " | "); 
				if ( (fr = testRows.get(key)) != null){
					//Print each row
					fr.PrintRow("test");
				}
			}
		}
	}
	public void PrintClassWisePredictions(){
		System.out.println(this.classwisePredictions);
	}
}
