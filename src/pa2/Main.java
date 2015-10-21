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
        String outFileNamePrefix = p.GetPropertyValue("OutFileNamePrefix");

        try{
            //It is given that we have 7 class labels in the dataset. Adding them in the array below to test for
            //all values.
            int[] classesToTest = { 1,2,3,4,5,6,7};
            for (int dp:classesToTest){
               //Parse Tree Depths to run for each depth.
                //Read csv file and serialize input data.
                System.out.println("Reading Input Training Data from " + trainFileName + " For original class label " + dp);
                Data d = Utility.ParseCategoricalFeatureData(categoricalDataString, trainFileName);
                Data updatedData = Utility.readTrainingFile(classLabelIndex, trainFileName, d , dp);
                //Use this updatedData Object to train and test Logistic Regression.
                updatedData = Utility.readTestData(classLabelIndex, testFileName , updatedData, dp);
                System.out.println("Training Instances : " + updatedData.getTrainingRows().size());
                System.out.println("Testing instances : "  + updatedData.getTestRows().size());
                System.out.println("\n********************* Start Naive Bayes *************************");
                //updatedData.PrintData("test");
                //Apply Logistic Regression and Naive Bayes.
               //Naive bayes has to be run first because we are transforming the train adn test data for logistic regression.
                NaiveBayes nb = new NaiveBayes(updatedData);
                //train naive bayes classifier .
                nb.trainClassifier(updatedData);
                //Read test data.
                //test naive bayes classifier.
                nb.testClassifier(updatedData);
                System.out.println("**************** End Naive Bayes. **********************");

                //Write Non Transformed Dataset to disk.
                if ( dp == 1 || dp == 2){
                    String fileName = "";
                    fileName = outFileNamePrefix.replace("#", "ds-without-feature-transform") ;
                    fileName = fileName.replace("*", "Class-label-" + dp + "-*");
                    Utility.WriteDataSetToFile(fileName, updatedData);
                }

                //Logistic Regression has to be learnt with 3 different learning rates for each model. Looping here.
                double[] learningRates = { 1,.02,.001};
                int c = 0 ;
                for ( double learningRate : learningRates){

                    //Create feature vectors for logistic regression training data.
                    updatedData.CreateBinarySplitsForColumnsInData();
                    //Write the transformed feature space data to a file.
                    if ( c ==  0){
                        String fileName = "";
                        fileName = outFileNamePrefix.replace("#", "feature-transformed") ;
                        fileName = fileName.replace("*", "Class-label-" + dp + "-*");
                        Utility.WriteDataSetToFile(fileName, updatedData);
                        c++ ;
                    }

                    //train logistic regression.
                    LogisticRegression lr = new LogisticRegression(updatedData,learningRate);
                    lr.TrainClassifier();
                    //test logistic regression classifier.
                    lr.TestClassifier(updatedData);
                    lr.PrintConfusionMatrix();
                }

            }

        }
        catch(Exception e){
            System.out.println("Error Occoured");
            e.printStackTrace();
        }

    }
}
