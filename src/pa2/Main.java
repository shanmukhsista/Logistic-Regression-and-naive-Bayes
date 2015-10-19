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
            int[] classesToTest = { 1};
            for (int dp:classesToTest){
               //Parse Tree Depths to run for each depth.
                //Read csv file and serialize input data.
                System.out.println("Reading Input Training Data from " + trainFileName);
                Data d = Utility.ParseCategoricalFeatureData(categoricalDataString, trainFileName);
                Data updatedData = Utility.readTrainingFile(classLabelIndex, trainFileName, d , dp);
                //Use this updatedData Object to train and test Logistic Regression.
                updatedData = Utility.readTestData(classLabelIndex, testFileName , d, dp);
                //updatedData.PrintData("test");
                //Apply Logistic Regression and Naive Bayes.
               //Naive bayes has to be run first because we are transforming the train adn test data for logistic regression.
                NaiveBayes nb = new NaiveBayes(updatedData);
                //train naive bayes classifier .
                nb.trainClassifier(updatedData);
                //Read test data.
                updatedData = Utility.readTestData(classLabelIndex, testFileName , d, dp);
                //test naive bayes classifier.
                nb.testClassifier(updatedData);
                //Create feature vectors for logistic regression training data.
                updatedData.CreateBinarySplitsForColumnsInData();
                //train logistic regression.
                LogisticRegression lr = new LogisticRegression(updatedData);
                lr.TrainClassifier();
                //test logistic regression classifier.
                lr.TestClassifier(updatedData);
            }

        }
        catch(Exception e){
            System.out.println("Error Occoured");
            e.printStackTrace();
        }

    }
}
