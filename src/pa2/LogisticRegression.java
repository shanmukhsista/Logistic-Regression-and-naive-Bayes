package pa2;

import com.javafx.tools.doclets.formats.html.SourceToHTMLConverter;
import com.sun.org.apache.xalan.internal.utils.FeatureManager;
import utilities.Attribute;
import utilities.Data;
import utilities.FeatureRow;

import java.util.List;

/**
 * Created by shanmukh on 10/15/15.
 */
public class LogisticRegression {
    Data d  = null;
    double[] gradientVector = null;
    double[] weightVector = null;
    int[][] confusionMatrix  = new int[2][2];
    //Default learning rate.
    double learningRate =.13;
    public LogisticRegression(Data trainData, double learningRate){
        this.d = trainData;
        weightVector = new double[trainData.getAttributeCount()];
        this.learningRate = learningRate;
        for(int i =0; i< 2; i++){
            for (int j =0; j< 2; j++ ){
                confusionMatrix[i][j] = 0;
            }
        }
    }
    public void TrainClassifier(){
        System.out.println("\n*************Started training Logistic Regression with a learning rate of " + learningRate + ". *****************");
        //For each row, compute the probability
        double[] prevWeightVector = new double[weightVector.length];
        int c = 0 ;
        do {
            c++;
            gradientVector = new double[weightVector.length];
            for (int rowId : d.getTrainingRows().keySet()) {
                FeatureRow row = d.getTrainingRows().get(rowId);
                if (row != null) {
                    //Compute the z vector by multiplying the weight with the column value.
                    double z = 0.0;
                    List<Attribute> columns = row.getFeatures();
                    for (int j = 0; j < weightVector.length; j++) {
                        z = z + (weightVector[j] * columns.get(j).getValue());
                    }
                    //Compute probability for this z
                    double prob = ComputeProbability(z);
                    double error = (row.getClassLabel().getValue() * 1.0) - prob;
                    //update the gradient vectors
                    for (int j = 0; j < gradientVector.length; j++) {
                        gradientVector[j] = gradientVector[j] + (error * columns.get(j).getValue() * 1.0);
                    }
                }
                //copy the current weight vector into the previous weight vector.
                //update the weight vector using the learning rate .
                for (int i = 0; i < weightVector.length; i++) {
                    prevWeightVector[i] = weightVector[i];
                    weightVector[i] = weightVector[i] + (learningRate * gradientVector[i]);

                }
            }
        } while(!CheckForConvergence(weightVector, prevWeightVector));
        System.out.println("Learning Rate : "+ learningRate);
        System.out.println("Number of Iterations till convergence : " + c );
        System.out.println("Final Weight Vector is: ");
        this.PrintDoubleArray(weightVector);
    }

    private boolean CheckForConvergence(double[] weightVector, double[] prevWeightVector) {
        boolean result = true;
          for ( int  i = 0  ; i < weightVector.length ; i++){
            if ( Math.abs(prevWeightVector[i] - weightVector[i]) > learningRate ){
                result = false ;
                break;
            }
        }
        return result;
    }

    public void TestClassifier(Data testData) {
        System.out.println("\nTesting Logistic Regression Classifier.");
        for (int rowId : testData.getTestRows().keySet()) {
            double result = 0.0;
            FeatureRow row = d.getTestRows().get(rowId);
            if (row != null) {
                //update the weight vector using the learning rate .
                for ( int i =0 ; i < weightVector.length ; i++){
                   result = result +  (weightVector[i]* row.getFeatures().get(i).getValue());
                }
            }
            if ( result  < 0){
                row.setPredictedClassLabel("0");
                if ( row.getPredictedClassLabel().equals(row.getExpectedClassLabel())){
                    confusionMatrix[1][1]++;
                }
                else{
                    confusionMatrix[1][0]++;
                }
            }
            else{
                row.setPredictedClassLabel("1");
                if ( row.getPredictedClassLabel().equals(row.getExpectedClassLabel())){
                    confusionMatrix[0][0]++;
                }
                else{
                    confusionMatrix[0][1]++;
                }
            }
            //System.out.println("Expected " + row.getExpectedClassLabel() + " - Predicted : " + row.getPredictedClassLabel());
        }
        System.out.println("Done Testing. Classifier Accuracy - " + this.ComputePredictionAccuracy());
        System.out.println("***************************************");

    }
    public void PrintDoubleArray(double[] a ){
        for ( int i = 0 ; i < a.length ; i++){
            System.out.print(a[i] + "   ");
        }
        System.out.println("");
    }
    public double ComputeProbability(double z){
        double probability ;
        probability = 1.0/(1.0 + Math.exp(-1*z));
        return probability;
    }
    public double ComputePredictionAccuracy(){
        double res = 0.0 ;
        int nCount = 0 ;
        int dCount= 0;
        for( int rowId : this.d.getTestRows().keySet()){
            FeatureRow row = this.d.getTestRows().get(rowId);
            if ( row.getExpectedClassLabel().equals(row.getPredictedClassLabel())){
                nCount++;
            }
            dCount++;
        }
        res = ( 100.0 * nCount )/ (1.0* dCount);
        return res;
    }
    public void PrintConfusionMatrix(){
        System.out.println("Confusion Matrix for learning rate :  " + learningRate);
        for(int i =0; i< 2; i++){
            for (int j =0; j< 2; j++ ){
                System.out.print(confusionMatrix[i][j] + "\t");
            }
            System.out.print("\n");
        }
        System.out.println();
    }
}
