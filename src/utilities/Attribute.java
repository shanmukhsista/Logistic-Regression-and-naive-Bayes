package utilities;
import java.util.Map;
public class Attribute {
	
	private String type;//categorical or binary
	
	//hashmap containing distinct features with their ids
	private Map<Integer,String> featureMap;
	
	private int value;

	public Map<Integer, String> getFeatureMap() {
		return featureMap;
	}

	public void setFeatureMap(Map<Integer, String> featureMap) {
		this.featureMap = featureMap;
	}
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

}
