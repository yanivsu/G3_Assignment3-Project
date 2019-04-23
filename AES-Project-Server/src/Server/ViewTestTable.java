package Server;

import java.io.Serializable;


import javafx.scene.image.ImageView;
/**
 *  serializable class that transfers all the test data and being used in the View Test controller for the its table from the server to the client
 * @author Hai Cohen
 *	
 */
@SuppressWarnings("serial")
public class ViewTestTable implements Serializable {

  
    public String getTestID() {
		return testID;
	}
	public void setTestID(String testID) {
		this.testID = testID;
	}
	public String getLecturerText() {
		return lecturerText;
	}
	public void setLecturerText(String lecturerText) {
		this.lecturerText = lecturerText;
	}
	public ImageView getViewImage() {
		return viewImage;
	}
	public void setViewImage(ImageView viewImage) {
		this.viewImage = viewImage;
	}
	public String getLecturer() {
		return lecturer;
	}
	public void setLecturer(String lecturer) {
		this.lecturer = lecturer;
	}
	public String getStudentText() {
		return studentText;
	}
	public void setStudentText(String studentText) {
		this.studentText = studentText;
	}
	public String getDurationTime() {
		return durationTime;
	}
	public void setDurationTime(String durationTime) {
		this.durationTime = durationTime;
	}
	public ViewTestTable(String testID, String lecturerText, ImageView viewImage, String lecturer, String studentText,
			String durationTime) {
		super();
		this.testID = testID;
		this.lecturerText = lecturerText;
		this.viewImage = viewImage;
		this.lecturer = lecturer;
		this.studentText = studentText;
		this.durationTime = durationTime;
	}
	private String testID;
    private String lecturerText;
    private ImageView viewImage;
    private String lecturer;
    private String studentText;
    private String durationTime;
    
    
	
 
}