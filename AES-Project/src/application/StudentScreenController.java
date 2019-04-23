package application;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class StudentScreenController implements Initializable {
	@FXML
	Button LogOutButton;
	@FXML
	Button ViewGradesButton;
	@FXML
	Button StartTestButton;
	@FXML
	Label WelcomeLabel;
	@FXML
	Label DateLabel;
/***
 *	disconnect the user from server.
 * @param e
 * @throws IOException
 */
	public void LogOutClick(ActionEvent e) throws IOException {
		Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/Login.fxml"));
		Scene loginScene = new Scene(loginParent);

		System.out.println(LoginController.userid);
		Main.client.openConnection();
		Main.client.sendToServer("LogOut@" + LoginController.userid);
		Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
		window.setScene(loginScene);
		window.setTitle("User Login");
		window.show();
	}
/***
 * Loads all the courses and the optional exam that student can do .
 * the user can choose one test from the list and start the test
 * @param e
 * @throws IOException
 */
	public void startTestclick(ActionEvent e) throws IOException {

		try {
			Main.client.openConnection();
			Main.client.sendToServer("TestCombobox@" + Client.StuId);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (Client.gotmsg == false) {
			System.out.println("");
		}
		Client.gotmsg = false;

		Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/MainPageForSudentTest.fxml"));
		Scene loginScene = new Scene(loginParent);
		Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
		window.setScene(loginScene);
		window.setTitle("Start Test");
		window.show();

	}
/**
 * view the grades for any exam that student done.
 * @param e
 * @throws IOException
 */
	public void ViewGradesClick(ActionEvent e) throws IOException {

		System.out.println("view grades");
		try {
			Main.client.sendToServer("ViewGrades@" + Client.StuId);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (Client.gotmsg == false) {
			System.out.println("grades");
		}
		Client.gotmsg = false;

		Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/ViewGrades.fxml"));
		Scene loginScene = new Scene(loginParent);
		Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
		window.setScene(loginScene);
		window.setTitle("View Grades");
		window.show();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate localDate = LocalDate.now();
		DateLabel.setText(dtf.format(localDate));
		WelcomeLabel.setText("Welcome :" + Client.StuId);
	}
}