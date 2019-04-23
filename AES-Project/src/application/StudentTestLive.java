package application;

import java.io.IOException;
import java.net.URL;

import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StudentTestLive implements Initializable {

	public static int questionIndex = 1;
	public static int questionCounter = 0;
	public static int testGrade = 0;

	private ArrayList<String> answers = new ArrayList<String>();

	public int realtimetest = 0;
	private ToggleGroup group;
	private long secondTimer;
	private int stoptestflag = 0;
	@FXML
	private Label timeLabel;
	@FXML
	private Label addExtraTime;
	@FXML
	private Label Quetext;
	@FXML
	private Label instractionlabel;
	@FXML
	private Label WelcomeLabel1;
	@FXML
	private Label questionnum1;
	@FXML
	private Label pointlabel;
	@FXML
	private JFXTextField Answer1;
	@FXML
	private JFXTextField Answer2;
	@FXML
	private JFXTextField Answer3;
	@FXML
	private JFXTextField Answer4;
	@FXML
	private Button Startbutton;
	@FXML
	private Button nextButton;
	@FXML
	private Button backButton;
	@FXML
	private RadioButton r1;
	@FXML
	private RadioButton r2;
	@FXML
	private RadioButton r3;
	@FXML
	private RadioButton r4;
	@FXML
	private JFXTextArea studentTextArea;
	private Timeline stopWatchTimeline;
/**
 * Print on the screen the time from start the test
 */
	private void setTimerDisplay() {
		if (stoptestflag == 0) {
			if (realtimetest != Integer.parseInt(Client.timeforLivetest))
			{
				addExtraTime.setVisible(true);
				addExtraTime.setText("Time added : " + (Integer.parseInt(Client.timeforLivetest) - realtimetest));
				addExtraTime.autosize();
			}
			timeLabel.setText(
					String.format("%02d:%02d:%02d", secondTimer / 3600, (secondTimer % 3600) / 60, secondTimer % 60));
		}
	}
/*
 * Clock of the test
 */
	private void startTimer() {
		if (stoptestflag == 0) {

			stopWatchTimeline = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent event) -> {
				//
				if (secondTimer < Integer.parseInt(Client.timeforLivetest) * 60)
				{
					System.out.println(Client.timeforLivetest);
					secondTimer++;
					setTimerDisplay();
				} else {
					System.out.println("i'm one " + stoptestflag);
					stoptestflag = 1;
					stopWatchTimeline.stop();
					try {
						showMessageAndSubmit();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}));
			stopWatchTimeline.setCycleCount(Timeline.INDEFINITE);
			stopWatchTimeline.play();
		}
	}
/**
 * if the student didnt finish the test in the time this function submit the answers he selected
 * @throws IOException
 */
	private void showMessageAndSubmit() throws IOException {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Info");
		alert.setHeaderText(null);
		alert.setContentText("Test time is over, the system saves your answers.");
		alert.show();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		while (answers.size() < Client.test_question.size() - 1) {
			answers.add("0");
		}

		forceSubmit();
	}


	/**
	 * If the student didn't click on submit button and the time is over
	 */
	private void forceSubmit() throws IOException {

		if (group.getSelectedToggle() == null) {
			answers.add(questionIndex - 1, "0");
		} else {
			RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
			String Answer = selectedRadioButton.getText();
			if (answers.size() >= questionIndex)
				answers.remove(questionIndex - 1);
			answers.add(questionIndex - 1, Answer);
		}
		String stuQuesAnsw = "";
		Client.lecTemp = false;
		for (int i = 0; i < answers.size(); i++) {
			stuQuesAnsw += "@" + Client.test_question.get(i).get_ID() + "@" + answers.get(i);
			if (answers.get(i).equals(String.valueOf(Client.test_question.get(i).get_corr_answer()))) {
				testGrade += Integer.parseInt(Client.test_question_map.get(Client.test_question.get(i)));
				stuQuesAnsw += "@" + Client.test_question_map.get(Client.test_question.get(i));
			} else
				stuQuesAnsw += "@0";
		}

		Main.client.sendToServer("AddTestResults@" + MainPageForSudentTestController.Ecode + "@"
				+ String.valueOf(secondTimer / 60) + "@" + answers.size() + "@" + StudentTestStart.TestID + "@"
				+ Client.StuId + stuQuesAnsw + "@" + testGrade);
		while (Client.lecTemp == false) {
			System.out.println("Add Test Results");
		}
		Client.lecTemp = false;
		// ActionEvent e = null;
		System.out.println(testGrade);

		Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/MainStudentScreen.fxml"));
		Scene loginScene = new Scene(loginParent);
		Stage window = (Stage) timeLabel.getScene().getWindow();
		window.setScene(loginScene);
		window.setTitle("Main Page");
		window.show();
	}
/**
 * Give the user the option to see the next question.
 * @param e
 * @throws IOException
 */
	public void nextButtonclick(ActionEvent e) throws IOException {
		if (nextButton.getText().equals("Next")) {

			if (group.getSelectedToggle() == null) {
				System.out.println("laurent");
				if (answers.size() >= questionIndex)
					answers.remove(questionIndex - 1);
				answers.add(questionIndex - 1, "0");
			} else {
				RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
				String Answer = selectedRadioButton.getText();
				if (answers.size() >= questionIndex)
					answers.remove(questionIndex - 1);
				answers.add(questionIndex - 1, Answer);
				group.getSelectedToggle().setSelected(false);
			}
			questionCounter++;
			questionIndex++;
			setToggle();
			setQuestion();
		} else // if submit
		{
			Alert alert = new Alert(AlertType.CONFIRMATION, "Would you like to submit the test?", ButtonType.YES,
					ButtonType.NO);
			alert.showAndWait();

			if (alert.getResult() == ButtonType.YES) {
				forceSubmit();
			}
		}
	}
/***
 * Give the user the option to see the prev question.
 * @param e
 * @throws IOException
 */
	public void backButtonclick(ActionEvent e) throws IOException {

		questionIndex--;
		setToggle();
		setQuestion();

		System.out.println("question index back: " + questionIndex + "  Answers:" + answers);

	}
/**
 * Init the Toggles buttons and if the toggle is selected in the past this method remember that.
 */
	public void setToggle() {

		if (answers.size() >= questionIndex) {
			if (answers.get(questionIndex - 1).equals("1")) {
				group.selectToggle(r1);
			} else if (answers.get(questionIndex - 1).equals("2")) {
				group.selectToggle(r2);
			} else if (answers.get(questionIndex - 1).equals("3")) {
				group.selectToggle(r3);
			} else if (answers.get(questionIndex - 1).equals("4")) {
				group.selectToggle(r4);
			} else if (answers.get(questionIndex - 1).equals("0")) {
				if (group.getSelectedToggle() != null)
					group.getSelectedToggle().setSelected(false);
			} else {
				group.getSelectedToggle().setSelected(false);
			}
		}

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		group = new ToggleGroup();
		r1.setToggleGroup(group);
		r2.setToggleGroup(group);
		r3.setToggleGroup(group);
		r4.setToggleGroup(group);
		WelcomeLabel1.setText("Student  ID : " + LoginController.userid);
		/* Init Question */
		setQuestion();
		addExtraTime.setVisible(false);
		secondTimer = StudentTestStart.difference * 60;
		realtimetest = Integer.parseInt(Client.timeforLivetest);
		startTimer();
		try {
			setNoteField();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
/**
 * Retrieves the note for the student by sending a message to the server
 * @throws IOException
 * @throws InterruptedException
 */
	private void setNoteField() throws IOException, InterruptedException {
		Client.gotmsg = false;
		Main.client.sendToServer("getStudentTextArea@" + StudentTestStart.TestID);
		while (!Client.gotmsg) {
			Thread.sleep(5);
		}
		studentTextArea.setText(Client.studentTextArea);
	}
/**
 * 
 * Load the questions/
 */
	public void setQuestion() {
		if (Client.test_question.size() >= questionIndex) {
			if (questionIndex == 1) {
				backButton.setVisible(false);
			} else {
				backButton.setVisible(true);
			}
			questionnum1.setText("Question number: " + questionIndex);
			instractionlabel.setText("Instruction: " + Client.test_question.get(questionIndex - 1).getInstructions());
			Quetext.setText(Client.test_question.get(questionIndex - 1).get_text());
			Answer1.setText(Client.test_question.get(questionIndex - 1).get_a1());
			Answer2.setText(Client.test_question.get(questionIndex - 1).get_a2());
			Answer3.setText(Client.test_question.get(questionIndex - 1).get_a3());
			Answer4.setText(Client.test_question.get(questionIndex - 1).get_a4());

			pointlabel.setText(
					" (" + Client.test_question_map.get(Client.test_question.get(questionIndex - 1)) + " Points)");
			System.out.println(Client.test_question.size() + " " + (questionIndex - 1));
			if (Client.test_question.size() == questionIndex) {
				nextButton.setText("Submit");
			} else {
				nextButton.setText("Next");
			}

		}
	}
}