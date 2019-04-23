package Server;

import java.io.Serializable;
/**
 * A serializable class that transfers all the lecturer's data from the server to the client
 * @author Hai Cohen
 *	
 */
@SuppressWarnings("serial")
public class Lecturer implements Serializable{
	public Lecturer(String lecturerID, String lecturerName) {
		super();
		this.lecturerID = lecturerID;
		this.lecturerName = lecturerName;
	}
	private String lecturerID;
	private String lecturerName;
	public String getLecturerName() {
		return lecturerName;
	}
	public void setLecturerName(String lecturerName) {
		this.lecturerName = lecturerName;
	}
	public String getLecturerID() {
		return lecturerID;
	}
	public void setLecturerID(String lecturerID) {
		this.lecturerID = lecturerID;
	}
}
