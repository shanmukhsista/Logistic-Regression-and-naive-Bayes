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
    double learningRate = 1;
    public LogisticRegression(Data trainData){
        this.d = trainData;
        gradientVector = new double[trainData.getAttributeCount()];
        weightVector = new double[trainData.getAttributeCount()];
    }
    public void TrainClassifier(){
        //For each row, compute the probability
        for ( int k = 0 ; k < 1 ;k++){
            for (int rowId : d.getTrainingRows().keySet()){
                FeatureRow row = d.getTrainingRows().get(rowId);
                if ( row != null){
                    //Compute the z vector by multiplying the weight with the column value.
                    double z = 0.0;
                    List<Attribute> columns = row.getFeatures();
                    for ( int j = 0 ; j < weightVector.length; j++){
                        z = z + (weightVector[j] * columns.get(j).getValue());
                    }
                    //Compute probability for this z
                    double prob = ComputeProbability(z);
                    double error = (row.getClassLabel().getValue()*1.0 )-  prob;
                    //update the gradient vectors
                    for ( int j = 0 ; j < gradientVector.length; j++) {
                        gradientVector[j] = gradientVector[j] + (error * columns.get(j).getValue() * 1.0);
                    }
                }
                //update the weight vector using the learning rate .
                for ( int i =0 ; i < weightVector.length ; i++){
                    weightVector[i] = weightVector[i]  + (learningRate * gradientVector[i]);
                }
            }
        }
        this.PrintDoubleArray(weightVector);
    }
    public void PrintDoubleArray(double[] a ){
        for ( int i = 0 ; i < a.length ; i++){
            System.out.print(a[i] + "   ");
        }
        System.out.println("");
    }
    public double ComputeProbability(double z){
        double probability ;
        System.out.println("Computing probability for " + z);
        probability = 1.0/(1.0 + Math.exp(z));
        System.out.println("prob is "+ probability);
        return probability;
    }
}
