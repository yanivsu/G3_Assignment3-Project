package application;


import java.io.IOException;
import java.net.URL;

import java.util.Optional;
import java.util.ResourceBundle;



import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.util.Duration;

public class TestStatusController extends Thread implements Initializable {
	@FXML
	private Button removeButton;
	@FXML
	private Button activeButton;
	@FXML
	private Button cancelButton;
	@FXML
	private Button lockButton;
	@FXML
	private Button durationButton;
	@FXML
	private ComboBox profList;
	@FXML
	private ListView courses;
	@FXML
	private TextField statusField;
	@FXML
	private Label statusLabel;
	@FXML

	private TextField timeField;
	@FXML
	private TextField exeCodeText;
	@FXML
	private  Label timeLabel;
	@FXML
	private ComboBox<String> testComboBox;
	private String profChoice;
	private Object selectedProf;
	private int int_flag = 1;
	private int char_flag = 1;
	public static String courseName = "";
	public static String testCode = "";
/**
 * Change the test status to 1 (Active)
 * @param e1 Action Button
 * @throws IOException
 * @throws InterruptedException
 */
	public void activeButtonClicked(ActionEvent e1) throws IOException, InterruptedException {
		String state = null;
		int i;
		String examcode = exeCodeText.getText();
		if (examcode.equals("") || examcode.length() != 4) { //////// Check if its iligal exe code
			int_flag = 1;
			char_flag = 1;
		} else {
			for (i = 0; i < 4; i++) {
				char temp_char;
				temp_char = examcode.charAt(i);
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
		}
		if (int_flag == 0 && char_flag == 0) {
			Main.client.sendToServer("isExist@" + examcode);
			Client.gotmsg = false;
			while (Client.gotmsg == false) {
				System.out.println("");
			}
			Client.gotmsg = false;
			if (Client.isExist == true) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning");
				alert.setHeaderText(null);
				alert.setContentText("Execution code already exists");
				alert.showAndWait();
			} else {

				String testID = (String) testComboBox.getSelectionModel().getSelectedItem();
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Test Activation");
				alert.setHeaderText("Please choose the test type");

				ButtonType compButton = new ButtonType("Computerized");
				ButtonType manualButton = new ButtonType("Manual");
				ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

				alert.getButtonTypes().setAll(compButton, manualButton, buttonTypeCancel);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() != buttonTypeCancel) {
					if (result.get() == compButton) {
						System.out.println("CREATE COMPUTERIZED TEST");
						// SEND A MESSAGE TO THE SERVER WITH THE TEST CODE
					
						state = "Computerized";

						Client.gotmsg = false;
						Main.client.sendToServer("Updateteststatus@" + 1 + "@" + state + "@" + testID + "@"
								+ exeCodeText.getText() + "@" + timeField.getText());
						while (!Client.gotmsg) {
							Thread.sleep(5);
						}
					} else {
						if (result.get() == manualButton) {
							System.out.println("CREATE WORD TEST");
						
							state = "Manual";
							Client.gotmsg = false;
							Main.client.handleMessageFromClientUI("CreateManualTest@" + testID);
							while (!Client.gotmsg) {
								Thread.sleep(5);
							}
							Alert createTestAlert = new Alert(AlertType.CONFIRMATION);
							createTestAlert.setTitle("Create manual Test");
							createTestAlert
									.setHeaderText("Test " + testID + " has been created as manual test successfully");
							ButtonType buttonTypeCancel1 = new ButtonType("Ok", ButtonData.OK_DONE);

							createTestAlert.getButtonTypes().setAll(buttonTypeCancel1);
							createTestAlert.showAndWait();

						}
						Client.gotmsg = false;
						Main.client.sendToServer("Updateteststatus@" + 1 + "@" + state + "@" + testID + "@"
								+ exeCodeText.getText() + "@" + timeField.getText());
						while (!Client.gotmsg) {
							Thread.sleep(5);
						}
						// SEND A MESSAGE TO THE SERVER TO DEACTIVATE THE TEST
					
					}
			//		TaskCloser taskrun = new TaskCloser(examcode, timeField.getText());
			//		Timer mission = new Timer();
			//		mission.schedule(taskrun, 0);
					Main.client.sendToServer("StartTimerForTest@" + examcode + "@" + timeField.getText() + "@");
				}
			}
		} else {
			int_flag = 1;
			char_flag = 1;
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText(null);
			alert.setContentText("Your execution code isn't correct");
			alert.showAndWait();
		}

		// Main.client.openConnection();

	}
/*
 * Cancel click helps us to go back to the last screen 
 * */
	public void cancelClick(ActionEvent e) throws IOException {
		Parent mainScreenParent = FXMLLoader.load(getClass().getResource("/gui/MainScreen.fxml"));
		Scene mainScreenScene = new Scene(mainScreenParent);

		Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
		window.setScene(mainScreenScene);
		window.setTitle("Main Screen");
		window.show();

	}
/*
 * Fill the ComboBox with all possible test
 */
	public void setTestIDCombo() throws IOException, InterruptedException {
		if (testComboBox.getItems().size() > 0) {
			testComboBox.getItems().clear();
		}
		Client.profCourseIDList.removeAll(Client.profCourseIDList);
		System.out.println(selectedProf);

		System.out.println("testtest");
		String prof = (String) profList.getSelectionModel().getSelectedItem();
		System.out.println("ma");
		String course = (String) courses.getSelectionModel().getSelectedItem();
		System.out.println("kore");
		comboSelected();
		Main.client.sendToServer("GetTestsID@" + prof + "@" + course);
		Client.gotmsg = false;
		while (!Client.gotmsg) {

			Thread.sleep(5);
		}
		if (Client.profCourseIDList.size() == 0) {
			testComboBox.setPromptText("No tests were found");
		} else {
			for (int i = 0; i < Client.profCourseIDList.size(); i++) {
				testComboBox.getItems().add(Client.profCourseIDList.get(i));
			}
			testComboBox.setPromptText("Select Test");

		}
	}
/**
 * make all the buttons possible 
 * @throws IOException
 * @throws InterruptedException
 */
	public void showButtons() throws IOException, InterruptedException {
		if (testComboBox.getItems().size() > 0) {
			String testID = (String) testComboBox.getSelectionModel().getSelectedItem();
			Client.gotmsg = false;
			Main.client.openConnection();
			Main.client.sendToServer("ShowTestDurationTime@" + testID);
			while (!Client.gotmsg)
				Thread.sleep(5);
			/*
			 * if (Client.testStatus == 0) {/////////////////Check Active or not
			 * statusField.setText("Not Active");
			 * statusField.setStyle("-fx-text-inner-color: red;"); } else
			 * 
			 * { statusField.setText("Active");
			 * statusField.setStyle("-fx-text-inner-color: green;"); }
			 */
			timeField.setText(Client.testDuration);
			activeButton.setVisible(true);
		//	lockButton.setVisible(true);
		//	durationButton.setVisible(true);
		//	statusLabel.setVisible(true);
		//	statusField.setVisible(true);
		//	timeField.setVisible(true);
		//	timeLabel.setVisible(true);
			FadeTransition ft1 = new FadeTransition(Duration.millis(1000), activeButton);
			FadeTransition ft2 = new FadeTransition(Duration.millis(1000), lockButton);
			FadeTransition ft3 = new FadeTransition(Duration.millis(1000), durationButton);
			FadeTransition ft4 = new FadeTransition(Duration.millis(1000), statusLabel);
			FadeTransition ft5 = new FadeTransition(Duration.millis(1000), statusField);
			FadeTransition ft6 = new FadeTransition(Duration.millis(1000), timeLabel);
			FadeTransition ft7 = new FadeTransition(Duration.millis(1000), timeField);
			ft1.setFromValue(0);
			ft1.setToValue(1);
			ft2.setFromValue(0);
			ft2.setToValue(1);
			ft3.setFromValue(0);
			ft3.setToValue(1);
			ft4.setFromValue(0);
			ft4.setToValue(1);
			ft5.setFromValue(0);
			ft5.setToValue(1);
			ft6.setFromValue(0);
			ft6.setToValue(1);
			ft7.setFromValue(0);
			ft7.setToValue(1);
			ft1.play();
			ft2.play();
			ft3.play();
			ft4.play();
			ft5.play();
			ft6.play();
			ft7.play();
		}
	}
	
	public void removeButtonClicked(ActionEvent e) throws Exception {
		String testID = (String) testComboBox.getSelectionModel().getSelectedItem();
		Alert alertBefore = new Alert(AlertType.CONFIRMATION);
		alertBefore.setTitle("Remove test");
		alertBefore.setHeaderText("Are you sure That you want to remove test " + testID + " ?");
		ButtonType removeButton = new ButtonType("Remove");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		alertBefore.getButtonTypes().setAll(removeButton, buttonTypeCancel);
		Optional<ButtonType> result = alertBefore.showAndWait();
		if (result.get() == removeButton) {
			Main.client.handleMessageFromClientUI("removeTestFromDB@" + testID);
		}
		showMessageAfterRemoveClicked(testID);
	}

	public void comboSelected() {
		activeButton.setVisible(false);

	}
/**
 * fill the proffetions in the ComboBox
 * @param e1
 */
	@FXML
	public void selectProff(ActionEvent e1) {
		if (e1.getSource() == profList) {
			// testComboBox.getItems().clear();
			Client.myProff.removeAll(Client.myProff);
			selectedProf = profList.getSelectionModel().getSelectedItem();

			System.out.println("TEST " + Client.profCourseIDList);
			comboSelected();
			Client.lecTemp = false;
			profChoice = (String) profList.getValue();
			// courses.getItems().removeAll(courses);

			// courses.getSelectionModel().clearSelection();
			try {
				Main.client.sendToServer("courses@" + LoginController.userid + "@" + profChoice);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while (Client.lecTemp == false) {
				System.out.println("sdfs");
			}
			Client.lecTemp = false;
			courses.getItems().clear();
			System.out.println("Hai is the best");
			for (int i = 0; i < Client.myProff.size(); i++) {
				System.out.println(Client.myProff);
				courses.getItems().add(Client.myProff.get(i));
			}
			if (testComboBox.getItems().size() > 0) {
				testComboBox.getItems().clear();
			}
			Client.profCourseIDList.removeAll(Client.profCourseIDList);
			System.out.println("MEOW MEOW" + Client.profCourseIDList);
		}
	}

	public void initialize(URL url, ResourceBundle rb) {
		comboSelected();
		int_flag = 1;
		char_flag = 1;
		Client.lecTemp = false;

		exeCodeText.setVisible(true);
		try {
			Main.client.sendToServer("proffesions@" + LoginController.userid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (Client.lecTemp == false) {
			System.out.println("q");
		}
		Client.lecTemp = false;

		if (Client.myProff.size() == 0) {
			profList.setPromptText("You don't teach any profession");

		} else {
			for (int i = 0; i < Client.myProff.size(); i++)
				profList.getItems().add(Client.myProff.get(i));
		}
	}

	public void showMessageAfterRemoveClicked(String testID) {

		Alert alertAfter = new Alert(AlertType.CONFIRMATION);
		alertAfter.setTitle("Remove test");
		ButtonType okButton = new ButtonType("Ok", ButtonData.OK_DONE);

		alertAfter.getButtonTypes().setAll(okButton);

		/*if (Server.myFlag == 0) {
			alertAfter.setHeaderText("Unable to remove test " + testID + " becuase test is currently active");
		} else if (Server.myFlag == 1) {
			alertAfter.setHeaderText("Test " + testID + " has been removed successfully");
		}*/
		alertAfter.showAndWait();
	}

	public void changeDurationTimeClick(ActionEvent event) throws IOException {
		if (statusField.getText().equals("Active")) {
			courseName = (String) courses.getSelectionModel().getSelectedItem();
			testCode = (String) testComboBox.getSelectionModel().getSelectedItem();
			Parent root = FXMLLoader.load(getClass().getResource("/gui/RequestChangeTime.fxml"));
			Stage primaryStage = new Stage();
			Scene scene = new Scene(root);
			primaryStage.initModality(Modality.APPLICATION_MODAL);
			primaryStage.setTitle("User Login");
			primaryStage.setResizable(false);
			primaryStage.sizeToScene();
			scene.getStylesheets().add(getClass().getResource("/gui/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("WARNING");
			alert.setHeaderText(null);
			alert.setContentText("The test must be active");
			alert.showAndWait();
		}
	}

}