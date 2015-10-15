package pa2;

import utilities.Data;

/**
 * Created by shanmukh on 10/15/15.
 */
public class LogisticRegression {
    Data d  = null;
    double[] gradientVector = null;
    public LogisticRegression(Data trainData){
        this.d = trainData;
        gradientVector = new double[trainData.getAttributeCount()];
    }
    public void TrainClassifier(){
        
    }
    public double ComputeProbability(double z){
        double probability ;
        probability = 1.0/(1 + Math.exp(z));
        return probability;
    }
}
