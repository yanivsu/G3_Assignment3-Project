package Server;

import java.io.Serializable;
/**
 * A serializable class that transfers all the student's test data from the server to the client
 * @author Hai Cohen
 *	
 */
@SuppressWarnings("serial")
public class TestOfStudent implements Serializable {

		private String questionID;
		private String questionScore;
		private String questionText;
		private String ans1;
		private String ans2;
		private String ans3;
		private String ans4;
		private String corrAns;
		private String instruction;
		private String stuAns;
		
		public TestOfStudent(String questionID, String questionScore, String questionText, String ans1, String ans2,
				String ans3, String ans4, String corrAns, String instruction, String stuAns) {
			super();
			this.questionID = questionID;
			this.questionScore = questionScore;
			this.questionText = questionText;
			this.ans1 = ans1;
			this.ans2 = ans2;
			this.ans3 = ans3;
			this.ans4 = ans4;
			this.corrAns = corrAns;
			this.instruction = instruction;
			this.stuAns = stuAns;
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

		public String getCorrAns() {
			return corrAns;
		}

		public void setCorrAns(String corrAns) {
			this.corrAns = corrAns;
		}

		public String getInstruction() {
			return instruction;
		}

		public void setInstruction(String instruction) {
			this.instruction = instruction;
		}

		public String getStuAns() {
			return stuAns;
		}

		public void setStuAns(String stuAns) {
			this.stuAns = stuAns;
		}
		
}
