package Server;

import java.io.Serializable;



@SuppressWarnings("serial")
public class Request implements Serializable
{
	private String courseName;
	private String amountTime;
	private String testCode;
	private String lecturerID;
	private String reson;
	private String exeCode;
	
	public Request(String cName,String aTime,String tCode,String lID,String res,String exeCode)
	{
		this.exeCode = exeCode;
		this.courseName = cName;
		this.amountTime = aTime;
		this.testCode = tCode;
		this.lecturerID = lID;
		this.reson = res;
	}
	public String getCoursename() {
		return courseName;
	}
	public String getAmountTime() {
		return amountTime;
	}
	public String getTestCode() {
		return testCode;
	}
	public String getLecturerID() {
		return lecturerID;
	}
	public String getReson() {
		return reson;
	}
	public String getExeCode() {
		return exeCode;
	}
}