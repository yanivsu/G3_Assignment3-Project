package Server;

import java.io.Serializable;


/**
 * A serializable class that transfers all the question data and being used in the View Question controller for the its table from the server to the client
 * @author Hai Cohen
 *	
 */
@SuppressWarnings("serial")
public class ViewQuestionTable implements Serializable {
	private String qID;
	private String qText;
	private String ans1;
	private String ans2;
	private String ans3;
	private String ans4;
	private int correctAnswer;
	private String instructions;
	private String lecturer;

	public ViewQuestionTable(String qID, String qText, String ans1, String ans2, String ans3, String ans4,
			int correctAnswer, String instructions, String lecturer) {
		super();
		this.qID = qID;
		this.qText = qText;
		this.ans1 = ans1;
		this.ans2 = ans2;
		this.ans3 = ans3;
		this.ans4 = ans4;
		this.correctAnswer = correctAnswer;
		this.instructions = instructions;
		this.lecturer = lecturer;
	}

	public String get_qID() {
		return qID;
	}

	public void set_qID(String qID) {
		this.qID = qID;
	}

	public String get_qText() {
		return qText;
	}

	public void set_qText(String qText) {
		this.qText = qText;
	}

	public String getAns1() {
		return ans1;
	}

	public void setAns1(String ans1) {
		this.ans1 = ans1;
	}

	public String getAns2() {
		return ans2;
	}

	public void setAns2(String ans2) {
		this.ans2 = ans2;
	}

	public String getAns3() {
		return ans3;
	}

	public void setAns3(String ans3) {
		this.ans3 = ans3;
	}

	public String getAns4() {
		return ans4;
	}

	public void setAns4(String ans4) {
		this.ans4 = ans4;
	}

	public int getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(int correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public String get_Instructions() {
		return instructions;
	}

	public void set_Instructions(String instructions) {
		this.instructions = instructions;
	}

	public String getLecturer() {
		return lecturer;
	}

	public void setLecturer(String lecturer) {
		this.lecturer = lecturer;
	}
}
