package utilities;
import java.util.List;


public class FeatureRow {
	
	private List<Attribute> features;
	
	private Attribute classLabel;
	private String type;
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPredictedClassLabel() {
		return predictedClassLabel;
	}

	public void setPredictedClassLabel(String predictedClassLabel) {
		this.predictedClassLabel = predictedClassLabel;
	}

	private String predictedClassLabel; 
	private String expectedClassLabel;
	

	public String getExpectedClassLabel() {
		return expectedClassLabel;
	}

	public void setExpectedClassLabel(String expectedClassLabel) {
		this.expectedClassLabel = expectedClassLabel;
	}

	public List<Attribute> getFeatures() {
		return features;
	}

	public void setFeatures(List<Attribute> features) {
		this.features = features;
	}

	public Attribute getClassLabel() {
		return classLabel;
	}

	public void setClassLabel(Attribute classLabel) {
		this.classLabel = classLabel;
	}
	public void PrintRow(String type){
		if (type == "train"){
			System.out.print("Class Label : " + this.classLabel.getValue() + " => ");
			for ( Attribute a : features){
				System.out.print(a.getValue() + "\t");
			}
			System.out.print("\n"); 
		}
		else if ( type == "test"){
			System.out.print("Expected Class Label : " + this.getExpectedClassLabel()+ " => ");
			for ( Attribute a : features){
				System.out.print(a.getValue() + "\t");
			}
			System.out.print("\n"); 
		}
		
	}

}
