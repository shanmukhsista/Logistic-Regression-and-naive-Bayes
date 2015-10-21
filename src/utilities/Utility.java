package utilities;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utility {
	public static HashMap<Integer, Integer> lastValues = new HashMap<>(); 
	 public static Data readTrainingFile(int classLabelColumn, String inputFileName , Data trainingData, int currentClass) throws Exception{
		try{
			
			 FileReader fr = new FileReader(inputFileName);
				BufferedReader br = new BufferedReader(fr);
				String line;
				Set<String> classLabelSet = new HashSet<String>();
				HashMap<Integer, HashMap<String, Integer>> categoricalInfo = trainingData.categoricalValues; 
				//create a map to store all the rows and their indices. 
				int rowIndex = 0 ; 
				 while((line = br.readLine()) != null){
					 List<Attribute> features = new ArrayList<Attribute>();
					 String row[] = line.split(",");
					 FeatureRow newRow = new FeatureRow();
					 int columnIndex = 0 ; 
					 for(String t: row){
						 
						 Attribute a = new Attribute();
						 a.setValue(Integer.parseInt(t));
						 if ( columnIndex== classLabelColumn){
							 //This is the class label attribute.
							 if ( a.getValue() != currentClass){
								 a.setValue(0);
							 }
							 else{
								 a.setValue(1);
							 }
							 newRow.setClassLabel(a);
							 classLabelSet.add(Integer.toString(a.getValue()));
						 }
						 else{				 
							 if (categoricalInfo!= null){	 
								 if ( categoricalInfo.containsKey(columnIndex)){
									 HashMap<String, Integer> featureValues = categoricalInfo.get(columnIndex); 
									 if ( featureValues != null){
										 if ( !featureValues.containsKey(Integer.toString(a.getValue()))){
											 if ( lastValues.containsKey(columnIndex)){
												 featureValues.put(Integer.toString(a.getValue()),lastValues.get(columnIndex)  + 1 );
												 lastValues.put(columnIndex, lastValues.get(columnIndex)  + 1);
											 }
										 }
									 }
								 }else{
									 HashMap<String, Integer> featureValues = new HashMap<>();
									 lastValues.put(columnIndex, 0);
									 featureValues.put(Integer.toString(a.getValue()), 0); 
									 categoricalInfo.put(columnIndex, featureValues); 
								 }
							 }
							 features.add(a);
							 
						 }
						 columnIndex++; 
					 }
					 newRow.setFeatures(features);
					 trainingData.AddRow(newRow);
			
					 rowIndex++; 
				 }
				 br.close();
				 trainingData.setClassLabelSet(classLabelSet);
				 return trainingData ;
		}
		catch (FileNotFoundException fe){
			System.out.println("Training File Not found.Please ensure that the configuration specifies the correct file. "); 
		}
		catch(IOException ioe){
			System.out.println("An error Occoured while reading the file. Please try again. "); 
		}
		return null; 
	}
	 //Read testing data. Testing data is present inside the data object. TrainingData. WE have already
	 //trained our data. This testing 
	 public static Data readTestData(int classLabelColumn, String inputFileName , Data trainingData, int currentClass){
		 try{
			
			 	FileReader fr = new FileReader(inputFileName);
			 System.out.println("Reading file " +  inputFileName);
				BufferedReader br = new BufferedReader(fr);
				String line;
				int rowIndex = 0 ; 
				 while((line = br.readLine()) != null){
					 List<Attribute> features = new ArrayList<Attribute>();
					 String row[] = line.split(",");
					 FeatureRow newRow = new FeatureRow();
					 int columnIndex = 0 ; 
					 for(String t: row){
						 
						 Attribute a = new Attribute();
						 a.setValue(Integer.parseInt(t));
						 if ( columnIndex== classLabelColumn){
							 //This is the class label attribute.
							 if ( a.getValue() != currentClass){
								 a.setValue(0);
							 }
							 else{
								 a.setValue(1);
							 }
							 newRow.setExpectedClassLabel(Integer.toString(a.getValue()));
						 }
						 else{				 
							 /*Handle categorical variables later*/
							 HashMap<Integer, HashMap<String, Integer>> categoricalInfo =  trainingData.categoricalValues;
							 if (categoricalInfo.containsKey(columnIndex)){
								 //this means that this attribute is a categorical variable. 
								 
							 }
							 features.add(a);
						 }
						 columnIndex++; 
					 }
					 newRow.setFeatures(features);
					 trainingData.AddTestRow(newRow);
					 rowIndex++; 
				 }
				 br.close();
				 return trainingData ;
		}
		catch (FileNotFoundException fe){
			System.out.println("Training File Not found.Please ensure that the configuration specifies the correct file. "); 
		}
		catch(IOException ioe){
			System.out.println("An error Occoured while reading the file. Please try again. "); 
		}
		 return null;
	 }
	 public static Data ParseCategoricalFeatureData(String categoricalDataString , String inputFileName){
		 //Parse the categorical Feature Info from the config file. 
		 Data trainingData = new Data("train");
		 String[] parts = categoricalDataString.split(",");
		 for ( String part : parts){ 
			 part = part.replace("{", ""); 
			 part = part.replace("}", ""); 
			 String pattern[] = part.split("#"); 
			 HashMap<String, Integer> valuesMap = new HashMap<String, Integer>(); 
			 if ( pattern.length == 2){
				 int columnIndex = Integer.parseInt(pattern[0]); 
				 String categoricalValues[] = pattern[1].split("\\|");
				 System.out.println(categoricalValues.length);
				 int vCount =0 ;
				 for( String variable : categoricalValues){
					 	valuesMap.put(variable, vCount++); 
				 }
				 lastValues.put(columnIndex, vCount--);
				 trainingData.AddCategoricalValuesForColumn(columnIndex, valuesMap );
			 }
			
		 }
		
		 return trainingData; 
	 }
	public static void WriteDataSetToFile(String outFileName, Data d) throws Exception {
        String trainOutFileName = outFileName.replace("*", "train");
        WriteRowsObject(d.getTrainingRows(), trainOutFileName, "train");
        String testOutFileName = outFileName.replace("*", "test");
        WriteRowsObject(d.getTestRows(), testOutFileName, "test");
    }

    private static void WriteRowsObject( HashMap<Integer, FeatureRow> rows, String outFileName,  String type) throws Exception{
        int ch = 0 ;
        PrintWriter pw=  new PrintWriter(outFileName);
        //Write training file.
        //We need column headers for weka.
        for ( int rowId : rows.keySet()){
            FeatureRow row = rows.get(rowId);
            String[] rowValues = new String[row.getFeatures().size() +  1];
            String[] headers = new String[row.getFeatures().size() +  1];
            if ( ch == 0 ){
                for ( int i = 0 ; i < row.getFeatures().size() + 1 ; i++){
                    headers[i] ="A-" + (i+1);
                }
                pw.println(StringUtils.join(headers, ","));
                ch++;
            }
            for ( int i = 0 ; i < row.getFeatures().size() ; i++){
                rowValues[i] = Integer.toString(row.getFeatures().get(i).getValue());
            }
            //Add the class label.
            if ( type.equals("train")){

                rowValues[rowValues.length - 1 ] = Integer.toString(row.getClassLabel().getValue());

            }
            else{
                rowValues[rowValues.length - 1 ] = row.getExpectedClassLabel();

            }
            pw.println(StringUtils.join(rowValues, ","));
        }
        pw.close();
    }
	public Data CreateBinarySplits(Data inputData){
		return inputData;
	}
	 public void CreateNewFile(String fileName){
			try {
	    		 
			      File file = new File("fileName");
			      
			      if (file.createNewFile()){
			        System.out.println(fileName + " created successfully. ");
			      }
			      
		    	} catch (IOException e) {
			      e.printStackTrace();
			}
	 }
}
