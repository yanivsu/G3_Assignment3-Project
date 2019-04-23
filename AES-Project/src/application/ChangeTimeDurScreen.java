package application;

import java.io.IOException;
import java.io.Serializable;

import Server.Request;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

@SuppressWarnings("serial")
public class ChangeTimeDurScreen implements Serializable {
	@FXML
	private TextField amountMin;
	@FXML
	private TextField requestInfo;
	@FXML
	private Button sendButton;
	@FXML
	private Button cancleButto;
	@FXML
	private TextField exeCodeTextField;
	/**
	 * The function is responsible for Clicking on "cancel" button =>
     * close the screen and return to main screen
	 * @param event
	 * @throws IOException 
	 */
	@FXML
	void cancelClick(ActionEvent event) throws IOException /* Go Back to the first window */
	{
	  	Parent testResults = FXMLLoader.load(getClass().getResource("/gui/MainScreen.fxml"));
			Scene TestScene = new Scene(testResults);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			window.setScene(TestScene);
			window.setTitle("Check tests");
			window.show();
	}
	/**
	 * The function is responsible for Clicking on "send" button =>
     *  send new request to the manager
	 * @param event
	 * @throws IOException 
	 */
	@FXML
	void sendClick(ActionEvent event) throws IOException /**/
	{
		
		if (requestInfo.getText().equals("") || amountMin.getText().equals("") || exeCodeTextField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("WARNING");
			alert.setHeaderText(null);
			alert.setContentText("One of you field is empty");
			alert.showAndWait();
		}
		else if(!isInt(amountMin.getText())) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("WARNING");
			alert.setHeaderText(null);
			alert.setContentText("Your time isnt illigal");
			alert.showAndWait();
		}
		else {
			try {
				Client.isExist = false;
				Client.gotmsg = false;
				Main.client.sendToServer("its_ok@" +exeCodeTextField.getText());
				while(Client.gotmsg == false) {
					System.out.println("");
				}
				Client.gotmsg = false;
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(Client.its_ok == 0)
			{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Info");
				alert.setHeaderText(null);
				alert.setContentText("Your test isn't exsit or not active");
				alert.showAndWait();
			}
			else {
			Request temp_req = new Request(TestStatusController.courseName, amountMin.getText(),
					TestStatusController.testCode, LoginController.userid, requestInfo.getText(),exeCodeTextField.getText());
			try {
				Main.client.sendToServer(temp_req);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Info");
				alert.setHeaderText(null);
				alert.setContentText("Duration Change sent to school director");
				alert.showAndWait();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Parent testResults = FXMLLoader.load(getClass().getResource("/gui/MainScreen.fxml"));
			Scene TestScene = new Scene(testResults);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			window.setScene(TestScene);
			window.setTitle("Check tests");
			window.show();
		}
		}

	}
	public static boolean isInt(String s) {
		try {
			int i = Integer.parseInt(s);
			return true;
		}

		catch (NumberFormatException er) {
			return false;
		}
	}
}