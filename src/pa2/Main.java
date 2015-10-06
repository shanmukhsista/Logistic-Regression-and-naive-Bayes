package pa2;

import utilities.*;

public class Main {

    public static void main(String[] args) {
	// write your code here
        String propertyFileName = "app";
        if ( args.length == 1 ){
            propertyFileName = args[0];
        }
        PropertyManager p = new PropertyManager(propertyFileName);
        //Read Configuraiton Values
        int classLabelIndex = Integer.parseInt(p.GetPropertyValue("ClassLabelIndex"));
        String trainFileName = p.GetPropertyValue("TrainFile");
        String categoricalDataString = p.GetPropertyValue("CategoricalFeatures");
        String testFileName = p.GetPropertyValue("TestFile");
        String depths = p.GetPropertyValue("TreeDepths");

        try{
            for (String dp : depths.split(",")){
                //Parse Tree Depths to run for each depth.
                //Read csv file and serialize input data.
                System.out.println("Reading Input Training Data from " + trainFileName);
                Data d = Utility.ParseCategoricalFeatureData(categoricalDataString, trainFileName);
                Data updatedData = Utility.readTrainingFile(classLabelIndex, trainFileName, d);   //read data
                System.out.println("Data load Complete.");
                //Apply Logistic Regression and Naive Bayes.

            }

        }
        catch(Exception e){
            System.out.println("Error Occoured");
        }

    }
}
