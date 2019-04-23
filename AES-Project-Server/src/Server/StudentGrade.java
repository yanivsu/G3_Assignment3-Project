package Server;

import java.io.Serializable;
/**
 * A serializable class that transfers all the student's grades from the server to the client
 * @author Hai Cohen
 *	
 */
@SuppressWarnings("serial")
public class StudentGrade implements Serializable{
	public StudentGrade(int grade, String testID, String executionCode) {
		super();
		this.grade = grade;
		this.testID = testID;
		this.executionCode = executionCode;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
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
	private int grade;
	private String testID;
	private String executionCode;
	
	
	
}
