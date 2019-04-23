package application;

import java.io.IOException;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert.AlertType;

import javafx.stage.Stage;

public class LoginController {
	public static String userid;
	public static String userClass;
	User temp_user;
	@FXML
	Button loginbutton;
	@FXML
	JFXPasswordField pwField;
	@FXML
	JFXTextField userField;
	@FXML
	JFXTextField portField;
	@FXML
	JFXTextField ipField;
	@FXML
	Label errorLabel;
	@FXML
	AnchorPane settingsPane;
	Stage window;
	private boolean settingsFlag = false;

	/**
	 *  The function is responsible for connect the user for the data base =>
	 *  by enter id and password
     *  display a new request on the screen(as long as it exists)
	 * @param e
	 * @throws IOException
	 */
	public void buttonClick(ActionEvent e) throws IOException {

		if (userField.getText().equals("")) {
			errorLabel.setVisible(true);
			errorLabel.setText("*One of the fields is missing");
		}
		if (pwField.getText().equals("")) {
			errorLabel.setVisible(true);
			errorLabel.setText("*One of the fields is missing");
		}
		if (userField.getText().equals("") == false && pwField.getText().equals("") == false) {
			/* Add to temp user the details the user input */
			temp_user = new User(userField.getText(), pwField.getText());
			/* Connect to data base */

			Client.loginFlag = 2;

			Main.client.sendToServer("CheckLogin@" + temp_user.getUserName() + "@" + temp_user.getPassword());
			try {

				while (Client.loginFlag == 2) {
					System.out.println("");
				}

				if (Client.loginFlag == 1) {

					String userStatus;
					userStatus = getUserStatus(temp_user.getUserName());
					System.out.println(userStatus);
					userid = userField.getText();
					switch (userStatus) {
					case "Stu": {
						errorLabel.setVisible(false);
						Client.StuId = userid;
						userClass = "Stu";
						Parent mainScreenParent = FXMLLoader
								.load(getClass().getResource("/gui/MainStudentScreen.fxml"));
						Scene mainScreenScene = new Scene(mainScreenParent);
						Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
						window.setScene(mainScreenScene);
						window.setTitle("Main Student Screen");
						window.show();

						break;
					}
					case "Lec": {
						userClass = "Lec";
						setConnectionforLecturerName(userid);
						errorLabel.setVisible(false);
						Parent mainScreenParent = FXMLLoader.load(getClass().getResource("/gui/MainScreen.fxml"));
						Scene mainScreenScene = new Scene(mainScreenParent);
						Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
						window.setScene(mainScreenScene);
						window.setTitle("Main Screen");
						window.show();
						break;
					}

					case "Scd": 
					{
						userClass = "Scd";
						Parent mainScreenParent = FXMLLoader.load(getClass().getResource("/gui/MainManagerScreen.fxml"));
						Scene mainScreenScene = new Scene(mainScreenParent);
						Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
						window.setScene(mainScreenScene);
						window.setTitle("Main Manager Screen");
						window.show();
						break;
					}
					default:
						System.out.println("no match");
					}

				}

				else if (Client.loginFlag == 0) {
					userField.clear();
					pwField.clear();
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information");
					alert.setHeaderText(null);
					alert.setContentText("Wrong username/password");
					alert.showAndWait();
				} else if (Client.already_conn == 0) {
					userField.clear();
					pwField.clear();
					Client.already_conn = 1;
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information");
					alert.setHeaderText(null);
					alert.setContentText("Already connected");
					alert.showAndWait();
				}
				// Main.client.closeConnection();

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				System.out.println("No Connection");
			}

		}

	}
	/**
	 * The function is responsible to connect lecturer by his id
	 * @param str
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void setConnectionforLecturerName(String str) throws InterruptedException, IOException {
		try {

			Main.client.sendToServer("lecturerID@" + str);
			while (!Client.gotmsg) {
				Thread.sleep(10);
			}
			Client.gotmsg = false;

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}
	/**
	 * The function is responsible for check user status ( connected or not)
	 * @param ID
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String getUserStatus(String ID) throws IOException, InterruptedException {
		try {

			Main.client.sendToServer("Status@" + ID);
			while (!Client.gotmsg) {
				Thread.sleep(10);
			}
			Client.gotmsg = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Client.UserStatus;
	}
	/**
	 * The function is responsible for set connection
	 * @throws IOException
	 */
	@FXML
	public void settingClicked() throws IOException {
		if (settingsFlag == false) {
			Main.client.closeConnection();
			settingsPane.setVisible(true);
			settingsFlag = true;
			ipField.setText(Main.client.getHost());
			int port = Main.client.getPort();
			portField.setText(Integer.toString(port));
			loginbutton.setVisible(false);
			userField.setFocusTraversable(false);
			pwField.setFocusTraversable(false);

		} else {
			settingsPane.setVisible(false);
			settingsFlag = false;

			Main.client.setHost(ipField.getText());
			Main.client.setPort(Integer.parseInt(portField.getText()));
			System.out.println(ipField.getText() + " " + portField.getText());
			Main.client.openConnection();
			loginbutton.setVisible(true);
			userField.setFocusTraversable(true);
			pwField.setFocusTraversable(true);
			System.out.print("Is connected? " + Main.client.isConnected());
		}
	}

}