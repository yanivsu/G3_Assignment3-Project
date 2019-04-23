package application;

import javafx.scene.control.CheckBox;
/**
 * Class for course table 
 * @author talfa
 *
 */
public class CourseTable {
	private String cName;
	private CheckBox checkBox;
	
	
	
	public CourseTable(String cName, CheckBox checkBox) {
		super();
		this.cName = cName;
		this.checkBox = checkBox;
	}
	public String get_cName() {
		return cName;
	}
	public void set_cName(String cName) {
		this.cName = cName;
	}
	public CheckBox getCheckBox() {
		return checkBox;
	}
	public void setCheckBox(CheckBox checkBox) {
		this.checkBox = checkBox;
	}
}