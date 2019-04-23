package application;

import javafx.scene.control.CheckBox;

public class CheckTable {

	private CheckBox checkBox;
	private String qID;
	private String qText;
	private int score;
	public CheckTable(CheckBox checkBox,String qID,String qText,int score) {
		this.setCheckBox(checkBox);
		this.qID=qID;
		this.qText=qText;
		this.score=score;
	}
	
	
	public String get_ID() {
		return qID;
	}
	public void setqID(String qID) {
		this.qID = qID;
	}
	public String get_Text() {
		return qText;
	}
	public void setqText(String qText) {
		this.qText = qText;
	}
	public int get_Score() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public CheckBox getCheckBox() {
		return checkBox;
	}


	public void setCheckBox(CheckBox checkBox) {
		this.checkBox = checkBox;
	}
	
	
}

