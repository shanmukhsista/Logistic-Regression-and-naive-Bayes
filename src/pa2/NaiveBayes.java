package pa2;

import com.sun.xml.internal.ws.wsdl.writer.document.StartWithExtensionsType;
import utilities.Attribute;
import utilities.Data;
import utilities.FeatureRow;

import java.io.PrintWriter;
import java.util.*;

/**
 * Created by Shambhavi on 10/18/2015.
 */
public class NaiveBayes {
    public Map<Integer,Set<Integer>> distinctColumnValues = new HashMap<Integer,Set<Integer>>();
    public Map<String,Integer> counts = new HashMap<String,Integer>();
    public Map<String,Double> probabilities = new HashMap<String,Double>();
    public int totalColumns;
    public double prior0,prior1;

    public NaiveBayes (Data data){
        for(int rowId : data.getTrainingRows().keySet()){
            FeatureRow row = data.getTrainingRows().get(rowId);
            List<Attribute> features = row.getFeatures();
            int coulumnId = 1;
            for(Attribute a :features){
                if(!distinctColumnValues.containsKey(coulumnId)){
                    Set<Integer> distinctValues = new HashSet<Integer>();
                    distinctValues.add(a.getValue());
                    distinctColumnValues.put(coulumnId,distinctValues);
                }
                else{
                    Set<Integer> temp = distinctColumnValues.get(coulumnId);
                    temp.add(a.getValue());
                    distinctColumnValues.put(coulumnId,temp);
                }
                coulumnId++;
            }
            totalColumns = distinctColumnValues.size();
        }
        //adding distinct value '5' in column 13's Set
        Set<Integer> distinctValue = distinctColumnValues.get(13);
        distinctValue.add(5);
        distinctColumnValues.put(13,distinctValue);
    }

    public void trainClassifier(Data trainData){
        Set<String> classLabels = trainData.getClassLabelSet();
        for(int rowId : trainData.getTrainingRows().keySet()){
            FeatureRow row = trainData.getTrainingRows().get(rowId);
            if(row.getClassLabel().getValue() == 0){
                if(!counts.containsKey("y0")){
                    counts.put("y0", 1);
                }
                else{
                    counts.put("y0", (counts.get("y0") + 1));
                }
                List<Attribute> features = row.getFeatures();
                int columnId = 1;
                for(Attribute a : features){
                    Set<Integer> temp = distinctColumnValues.get(columnId);
                    if(temp.contains(a.getValue())){
                        String s = "y0/"+columnId+"/"+a.getValue();
                        if(!counts.containsKey(s)){
                            counts.put(s,1);
                        }
                        else{
                            counts.put(s,(counts.get(s)+1));
                        }
                    }
                    columnId++;
                }
            }
            else if(row.getClassLabel().getValue() == 1){
                if(!counts.containsKey("y1")){
                    counts.put("y1",1);
                }
                else{
                    counts.put("y1",(counts.get("y1")+1));
                }
                List<Attribute> features = row.getFeatures();
                int columnId =1;
                for(Attribute a : features){
                    Set<Integer> temp = distinctColumnValues.get(columnId);
                    if(temp.contains(a.getValue())){
                        String s = "y1/"+columnId+"/"+a.getValue();
                        if(!counts.containsKey(s)){
                            counts.put(s,1);
                        }
                        else{
                            counts.put(s,(counts.get(s)+1));
                        }
                    }
                    columnId++;
                }
            }
        }
        for(int i=1;i<=totalColumns;i++){
            Set<Integer> distinctValues = distinctColumnValues.get(i);
            for(int tempVal : distinctValues) {
                String key0 = "y0/"+i+"/"+tempVal;
                String key1 = "y1/"+i+"/"+tempVal;
                if(counts.containsKey(key0)){
                    double prob = (float)(counts.get(key0)+1)/(counts.get("y0")+distinctColumnValues.get(i).size());
                    probabilities.put(key0,prob);
                }
                else{
                    double prob0 = (float)1/(counts.get("y0")+distinctColumnValues.get(i).size());
                    probabilities.put(key0,prob0);
                }
                if(counts.containsKey(key1)){
                    double prob = (float)(counts.get(key1)+1)/(counts.get("y1")+distinctColumnValues.get(i).size());
                    probabilities.put(key1,prob);
                }
                else{
                    double prob1 = (float)1/(counts.get("y1")+distinctColumnValues.get(i).size());
                    probabilities.put(key1,prob1);
                }
            }
        }
        prior0 = (float)(counts.get("y0")+1)/(trainData.getTrainingRows().size() + classLabels.size());
        prior1 = (float)(counts.get("y1")+1)/(trainData.getTrainingRows().size() + classLabels.size());
    }

    public void testClassifier(Data testData) {
        int correctlyClassified = 0;
        int incorrectlyClassified = 0;
        for(int rowId : testData.getTestRows().keySet()){
            FeatureRow row = testData.getTestRows().get(rowId);
            List<Attribute> features = row.getFeatures();
            int columnId = 1;
            double prob0 = prior0;
            double prob1 = prior1;
            for(Attribute a : features){
                String key0 = "y0/"+columnId+"/"+a.getValue();
                String key1 = "y1/"+columnId+"/"+a.getValue();
                prob0 = prob0 * probabilities.get(key0);
                prob1 = prob1 * probabilities.get(key1);
                columnId++;
            }
            String prediction = (prob0 > prob1) ? "0" : "1";
            row.setPredictedClassLabel(prediction);
            if(row.getExpectedClassLabel().equals(row.getPredictedClassLabel())){
               correctlyClassified++;
            }
            else{
               incorrectlyClassified++;
            }
        }
        System.out.println("Correctly classified :: "+correctlyClassified);
        System.out.println("Incorrectly classified :: "+incorrectlyClassified);
    }
}
