package Server;

import java.io.Serializable;
/**
 * A serializable class that transfers all the lecturer's test data from the server to the client
 * @author Hai Cohen
 *	
 */
@SuppressWarnings("serial")
public class TestForManager implements Serializable{
	private String questionID;
	private String questionScore;
	private String questionText;
	private String answer1;
	private String answer2;
	private String answer3;
	private String answer4;
	private String correctAnswer;
	private String instructions;
	public TestForManager(String questionID, String questionScore, String questionText, String answer1, String answer2,
			String answer3, String answer4, String correctAnswer, String instructions) {
		super();
		this.questionID = questionID;
		this.questionScore = questionScore;
		this.questionText = questionText;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.answer4 = answer4;
		this.correctAnswer = correctAnswer;
		this.instructions = instructions;
	}
	public String getQuestionID() {
		return questionID;
	}
	public void setQuestionID(String questionID) {
		this.questionID = questionID;
	}
	public String getQuestionScore() {
		return questionScore;
	}
	public void setQuestionScore(String questionScore) {
		this.questionScore = questionScore;
	}
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public String getAnswer1() {
		return answer1;
	}
	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}
	public String getAnswer2() {
		return answer2;
	}
	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}
	public String getAnswer3() {
		return answer3;
	}
	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}
	public String getAnswer4() {
		return answer4;
	}
	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	public String getInstructions() {
		return instructions;
	}
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
}
