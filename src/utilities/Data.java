package utilities;
import java.util.*;


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
	public void CreateBinarySplitsForColumnsInData(){
		//This method scans the train and test data for distinct values and builds a multi column binary split
		HashMap<Integer , HashSet<Integer>> values = new HashMap<>();
		HashSet<Integer> col13 = new HashSet<>();
        col13.add(0);
        col13.add(2);
        col13.add(4);
        col13.add(5);
        col13.add(6);
        col13.add(8);
        values.put(12,col13);
		int insertions = 0;
		//Add these columns to tthe train and test set.
		for ( int rowId  : trainingRows.keySet()){
			FeatureRow row = trainingRows.get(rowId) ;
			List<Attribute> cols = row.getFeatures();
			//Create a temp map storing values for splits as categorical values
			List<Attribute> newCols = new ArrayList<>();
			for ( int c = 0 ; c < cols.size(); c++){
				//Check if this col's value is = 0 or
				if ( values.containsKey(c)){
                    if ( values.get(c).contains(0) && values.get(c).contains(1) && values.get(c).size() == 2){
                        //We don't need to do anything here. The column is binary valued.
                        newCols.add(cols.get(c));
                    }
                    else{
                        TreeMap<Integer, Integer> aCol = new TreeMap<>();
                        int tempIndex = c ;
                        for ( int c1  : values.get(c)){
                            //aCol -- key - new column index.
                            //value - new column value.
                            aCol.put(c1, 0);
                        }
                        if( aCol.containsKey(cols.get(c).getValue())){
                            aCol.put(cols.get(c).getValue(), 1);

                        }
                        //Append this to the newCols

                        for ( int k : aCol.keySet()){
                            Attribute n = new Attribute();
                            n.setValue(aCol.get(k));
                            newCols.add(n);
                            insertions++;
                        }
                    }
                }
                else{
                    newCols.add(cols.get(c));
                }

			}
                row.setFeatures(newCols);
			this.setAttributeCount(newCols.size());
		}


		//Add these columns to tthe train and test set.
		for ( int rowId  : testRows.keySet()){
			FeatureRow row = testRows.get(rowId) ;
			List<Attribute> cols = row.getFeatures();
			//Create a temp map storing values for splits as categorical values
			List<Attribute> newCols = new ArrayList<>();
			for ( int c = 0 ; c < cols.size(); c++){
				if ( values.containsKey(c)){
                    //Check if this col's value is = 0 or
                    if ( values.get(c).contains(0) && values.get(c).contains(1) && values.get(c).size() == 2){
                        //We don't need to do anything here. The column is binary valued.
                        newCols.add(cols.get(c));
                    }
                    else{
                        TreeMap<Integer, Integer> aCol = new TreeMap<>();
                        int tempIndex = c ;
                        for ( int c1  : values.get(c)){
                            //aCol -- key - new column index.
                            //value - new column value.
                            aCol.put(c1, 0);
                        }
                        if( aCol.containsKey(cols.get(c).getValue())){
                            aCol.put(cols.get(c).getValue(), 1);

                        }
                        //Append this to the newCols

                        for ( int k : aCol.keySet()){
                            Attribute n = new Attribute();
                            n.setValue(aCol.get(k));
                            newCols.add(n);
                            insertions++;
                        }
                    }
                } else{
                    newCols.add(cols.get(c));
                }

			}
			row.setFeatures(newCols);
			this.setAttributeCount(newCols.size());
		}
	}
	public void PrintData(String type){
		int c = 0 ;

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
	public void PrintConfusionMatrix(){
		if ( trainingRows != null && testRows != null){
			//Print confusion matrix.
		}
	}

	public void PrintClassWisePredictions(){
		System.out.println(this.classwisePredictions);
	}
}
