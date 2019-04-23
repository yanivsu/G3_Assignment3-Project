package application;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;



import com.jfoenix.controls.JFXTextField;

import javafx.animation.FadeTransition;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.scene.control.ComboBox;

import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainPageForSudentTestController implements Initializable {
	public static String Ecode;
	public static int isexist = 0;
	public static String testChoice;
	private int int_flag = 1;
	private int char_flag = 1;
	private boolean computerize = false;
	public static String testID;
	@FXML
	TextField IDtext;
	@FXML
	ComboBox<String> courseCombo;
	@FXML
	JFXTextField exeCodeField;
/**
 * Check if the exectution code write ok and check if the code exsit
 * @param e
 * @throws IOException
 */
	public void nextbuttonclick(ActionEvent e) throws IOException {
		Ecode = exeCodeField.getText();
		for (int i = 0; i < 4; i++) {
			char temp_char;
			temp_char = Ecode.charAt(i);
			if (temp_char >= '0' && temp_char <= '9') {
				int_flag = 0;
			}
			if (temp_char >= 'a' && temp_char <= 'z') {
				char_flag = 0;
			}
			if (temp_char >= 'A' && temp_char <= 'Z') {
				char_flag = 0;
			}
		}
		if (computerize == true) {

			if (char_flag == 0 && int_flag == 0) {
				String[] strArr = testChoice.split("id:");
				strArr = strArr[1].split(" ");
				// strArr[1].substring(2,8);
				System.out.println(strArr[1]);

				Main.client.sendToServer("isExeCodeMatch@" + Ecode + "@" + strArr[1]);
				while (Client.gotmsg == false) {
					System.out.println("");
				}
				Client.gotmsg = false;
				if (Client.isExist == false) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText(null);
					alert.setContentText("Your execution code isn't correct,please try again or choose another test");
					alert.showAndWait();
				} else {
					// ** continue to start test screen
					Parent mainScreenParent = FXMLLoader.load(getClass().getResource("/gui/MainExamPage.fxml"));
					Scene mainScreenScene = new Scene(mainScreenParent);
					Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
					window.setScene(mainScreenScene);
					window.setTitle("Test Screen");
					window.show();
				}
			}
		} else {
			if (char_flag == 0 && int_flag == 0) 
			{
				String[] strArr = testChoice.split("id:");
				strArr = strArr[1].split(" ");
				Client.isExist = true;
				Main.client.sendToServer("isExeCodeMatch@" + Ecode + "@" + strArr[1]);
				while (Client.gotmsg == false) {
					System.out.println("");
				}
				Client.gotmsg = false;
				if (Client.isExist == false) 
				{
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText(null);
					alert.setContentText("Your execution code isn't correct,please try again or choose another test");
					alert.showAndWait();
				} 
				else 
				{
					String[] strArr2 = testChoice.split("id: ");
					testID = strArr2[1].substring(0, 6);
					Parent mainScreenParent = FXMLLoader.load(getClass().getResource("/gui/LiveManualTest.fxml"));
					Scene mainScreenScene = new Scene(mainScreenParent);
					Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
					window.setScene(mainScreenScene);
					window.setTitle("Test Screen");
					window.show();
				}
			}
		}
	}
/**
 * Go back to the last screen
 * @param e
 * @throws IOException
 */
	public void cancelClick(ActionEvent e) throws IOException {
		Parent mainScreenParent = FXMLLoader.load(getClass().getResource("/gui/MainStudentScreen.fxml"));
		Scene mainScreenScene = new Scene(mainScreenParent);
		Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
		window.setScene(mainScreenScene);
		window.setTitle("Main Student Screen");
		window.show();
	}

	public void showButtons() {

		exeCodeField.setVisible(true);
		FadeTransition ft2 = new FadeTransition(Duration.millis(1000), exeCodeField);
		ft2.setFromValue(0);
		ft2.setToValue(1);
		ft2.play();
	}

	public void comboSelected() {
		exeCodeField.setVisible(false);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		comboSelected();
		courseCombo.setPromptText("please select a test to do");

		if (Client.courseList.size() == 0) {
			courseCombo.setPromptText("You don't have any active test to do");

		} else {
			for (int i = 0; i < Client.courseList.size(); i++)
				courseCombo.getItems().add(Client.courseList.get(i));

			courseCombo.setOnAction((event) -> {
				// Button was clicked, do something...
				testChoice = (String) courseCombo.getValue();

				if (testChoice.contains("Com")) {
					computerize = true;
					showButtons();
				} else {
					computerize = false;
					exeCodeField.setVisible(false);
					showButtons();
				}

			});
		}

	}

}