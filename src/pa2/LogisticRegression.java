package pa2;

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
    double learningRate = .5;
    public LogisticRegression(Data trainData){
        this.d = trainData;
        weightVector = new double[trainData.getAttributeCount()];
    }
    public void TrainClassifier(){
        //For each row, compute the probability
        double[] prevWeightVector = new double[weightVector.length];
        int c = 0 ;
        do {
            System.out.println("Iteration : " + c++);
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
            }
            else{
                row.setPredictedClassLabel("1");
            }
            //System.out.println("Expected " + row.getExpectedClassLabel() + " - Predicted : " + row.getPredictedClassLabel());
        }
        System.out.println(this.ComputePredictionAccuracy());
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
}
