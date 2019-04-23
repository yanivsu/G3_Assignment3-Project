package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXRadioButton;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * 
 * @author Hai Cohen
 *
 */
public class TestPopupController implements Initializable {
	private int index = 0;
	@FXML
	private ToggleGroup group;
	@FXML
	private Button leftButton;

	@FXML
	private Button rightButton;

	@FXML
	private Label ans3;

	@FXML
	private JFXRadioButton r2;

	@FXML
	private JFXRadioButton r3;

	@FXML
	private JFXRadioButton r4;

	@FXML
	private JFXRadioButton r1;
	@FXML
	private ImageView closeImage;
	@FXML
	private Label ans1;

	@FXML
	private Label ans2;

	@FXML
	private Label ans4;

	@FXML
	private Label questionText;

	@FXML
	private Label instructions;

	@FXML
	private Label questionScore;

	/**
	 * Closes the stage if the imageview is clicked
	 * 
	 * @param event
	 */
	@FXML
	void exitClicked(MouseEvent event) {
		Stage stage = (Stage) closeImage.getScene().getWindow();
		stage.close();
	}

	/**
	 * handles the next button click, increases the index of the question count
	 * @param event
	 */
	@FXML
	void nextQuestion(ActionEvent event) {
		leftButton.setVisible(true);
		if (Client.testForManager.size() > index + 1) {
			index++;
			if (index + 1 == Client.testForManager.size())
				rightButton.setVisible(false);
			setData();
		}
	}
	/**
	 * handles the previous button click, decreases the index of the question count
	 * @param event
	 */
	@FXML
	void prevQuestion(ActionEvent event) {
		rightButton.setVisible(false);
		if (index != 0) {
			rightButton.setVisible(true);
			index--;
			if (index == 0)
				leftButton.setVisible(false);
			setData();
		}
	}
	/**
	 * Initializes the screen
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			getTestFromDB();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		leftButton.setVisible(false);
		if (Client.testForManager.size() == 1) {
			rightButton.setVisible(false);
		}

	}
/**
 * Gets all the relevant tests by sending a message to the server
 * @throws IOException
 * @throws InterruptedException
 */
	private void getTestFromDB() throws IOException, InterruptedException {
		System.out.println(ManagerTestController.getTestID());
		Client.gotmsg = false;
		Main.client.sendToServer("GetTestQuestion@" + ManagerTestController.getTestID());
		while (!Client.gotmsg)
			Thread.sleep(5);
		setData();

	}
/**
 * Sets the question data on each click(previous,next)
 */
	public void setData() {
		String correctAnswer = Client.testForManager.get(index).getCorrectAnswer();
		if (correctAnswer.equals("1"))
			group.selectToggle(r1);
		else if (correctAnswer.equals("2"))
			group.selectToggle(r2);
		else if (correctAnswer.equals("3"))
			group.selectToggle(r3);
		else if (correctAnswer.equals("4"))
			group.selectToggle(r4);
		ans1.setText(Client.testForManager.get(index).getAnswer1());
		ans2.setText(Client.testForManager.get(index).getAnswer2());
		ans3.setText(Client.testForManager.get(index).getAnswer3());
		ans4.setText(Client.testForManager.get(index).getAnswer4());
		instructions.setText(Client.testForManager.get(index).getInstructions());
		questionText.setText(Client.testForManager.get(index).getQuestionText());
		questionScore.setText(Client.testForManager.get(index).getQuestionScore());

	}

}
