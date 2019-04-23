package Server;

import java.io.Serializable;

import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
/**
 * The class check information in given combox
 * @author talfa
 *
 */
@SuppressWarnings("serial")
public class checkTestBox implements Serializable{
	public checkTestBox(String testID, String studID, String grade, String status, ImageView viewImage, String exeCode,
			String testType) {
		super();
		this.testID = testID;
		this.studID = studID;
		this.grade = grade;
		this.status = status;
		this.viewImage = viewImage;
		this.exeCode = exeCode;
		this.testType = testType;
	}
	private CheckBox checkBox;
	private String testID;
	private String studID;
	private String grade;
	private String status;
	private ImageView viewImage;
	private String explaination;
	private String exeCode;
	private String testType;
	
	public String getTestType() {
		return testType;
	}


	public void setTestType(String testType) {
		this.testType = testType;
	}


	public checkTestBox(CheckBox checkBox, String testID, String studID, String grade, String status,
			ImageView viewImage,String explaination,String exeCode) {
		super();
		this.checkBox = checkBox;
		this.testID = testID;
		this.studID = studID;
		this.grade = grade;
		this.status = status;
		this.viewImage = viewImage;
		this.setExplaination(explaination);
		this.exeCode=exeCode;
	
	}
	
	
	public String getExeCode() {
		return exeCode;
	}
	public void setExeCode(String exeCode) {
		this.exeCode = exeCode;
	}
	public ImageView getViewImage() {
		return viewImage;
	}
	public void setViewImage(ImageView viewImage) {
		this.viewImage = viewImage;
	}

	public CheckBox getCheckBox() {
		return checkBox;
	}
	public void setCheckBox(CheckBox checkBox) {
		this.checkBox = checkBox;
	}	
	
	public String get_tID() {
		return testID;
	}
	public void setTID(String tID) {
		this.testID = tID;
	}
	
	public String get_stuID() {
		return studID;
	}
	public void setqID(String sID) {
		this.studID = sID;
	}
	
	public String get_status() {
		return status;
	}
	public void set_status(String status) {
		this.status = status;
	}
	public String get_grade() {
		return grade;
	}
	public void set_grade(String grade) {
		this.grade = grade;
	}
	public String getExplaination() {
		return explaination;
	}
	public void setExplaination(String explaination) {
		this.explaination = explaination;
	}
}