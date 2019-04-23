package Server;

import java.io.Serializable;

import javafx.scene.image.ImageView;

@SuppressWarnings("serial")
public class GradesOfStudent implements Serializable{

	private String testID;
	private String grade;
	private String status;
	private ImageView viewImage;
	private String comment;
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public GradesOfStudent(String testID, String grade, String status, ImageView imageView,String comment) {
		this.testID = testID;
		this.grade = grade;
		this.status = status;
		this.viewImage=imageView;
		this.comment=comment;
	}
	
	public ImageView getViewImage() {
		return viewImage;
	}
	public void setViewImage(ImageView viewImage) {
		this.viewImage = viewImage;
	}
	

	
	public String getTestID() {
		return testID;
	}
	public void setTestID(String testID) {
		this.testID = testID;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}


}