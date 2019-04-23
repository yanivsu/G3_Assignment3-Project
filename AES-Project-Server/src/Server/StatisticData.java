package Server;

import java.io.Serializable;
/**
 * A serializable class that transfers all the statistical data from the server to the client
 * @author Hai Cohen
 *	
 */
@SuppressWarnings("serial")
public class StatisticData implements Serializable{
	public String getAvreage() {
		return avreage;
	}
	public void setAvreage(String avreage) {
		this.avreage = avreage;
	}
	public String getMedian() {
		return median;
	}
	public void setMedian(String median) {
		this.median = median;
	}
	public String getFirstRange() {
		return firstRange;
	}
	public void setFirstRange(String firstRange) {
		this.firstRange = firstRange;
	}
	public String getSecondRange() {
		return secondRange;
	}
	public void setSecondRange(String secondRange) {
		this.secondRange = secondRange;
	}
	public String getThirdRange() {
		return thirdRange;
	}
	public void setThirdRange(String thirdRange) {
		this.thirdRange = thirdRange;
	}
	public String getFourthRange() {
		return fourthRange;
	}
	public void setFourthRange(String fourthRange) {
		this.fourthRange = fourthRange;
	}
	public String getFifthRange() {
		return fifthRange;
	}
	public void setFifthRange(String fifthRange) {
		this.fifthRange = fifthRange;
	}
	public String getSixthRange() {
		return sixthRange;
	}
	public void setSixthRange(String sixthRange) {
		this.sixthRange = sixthRange;
	}
	public String getSeventhRange() {
		return seventhRange;
	}
	public void setSeventhRange(String seventhRange) {
		this.seventhRange = seventhRange;
	}
	public String getEigthRange() {
		return eigthRange;
	}
	public void setEigthRange(String eigthRange) {
		this.eigthRange = eigthRange;
	}
	public String getNinthRange() {
		return ninthRange;
	}
	public void setNinthRange(String ninthRange) {
		this.ninthRange = ninthRange;
	}
	public String getTenthRange() {
		return tenthRange;
	}
	public void setTenthRange(String tenthRange) {
		this.tenthRange = tenthRange;
	}
	public StatisticData(String avreage, String median, String firstRange, String secondRange, String thirdRange,
			String fourthRange, String fifthRange, String sixthRange, String seventhRange, String eigthRange,
			String ninthRange, String tenthRange,String testID,String executionCode) {
		super();
		this.avreage = avreage;
		this.median = median;
		this.firstRange = firstRange;
		this.secondRange = secondRange;
		this.thirdRange = thirdRange;
		this.fourthRange = fourthRange;
		this.fifthRange = fifthRange;
		this.sixthRange = sixthRange;
		this.seventhRange = seventhRange;
		this.eigthRange = eigthRange;
		this.ninthRange = ninthRange;
		this.tenthRange = tenthRange;
		this.testID=testID;
		this.executionCode=executionCode;
	}
	private String testID;
	private String executionCode;
	public String getTestID() {
		return testID;
	}
	public void setTestID(String testID) {
		this.testID = testID;
	}
	public String getExecutionCode() {
		return executionCode;
	}
	public void setExecutionCode(String executionCode) {
		this.executionCode = executionCode;
	}
	private String avreage;
	private String median;
	private String firstRange;
	private String secondRange;
	private String thirdRange;
	private String fourthRange;
	private String fifthRange;
	private String sixthRange;
	private String seventhRange;
	private String eigthRange;
	private String ninthRange;
	private String tenthRange;

	
}