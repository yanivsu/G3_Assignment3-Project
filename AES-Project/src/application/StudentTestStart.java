package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.Date;

import java.util.ResourceBundle;


import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class StudentTestStart implements Initializable {
	public static long difference;
	public static String realtime;
	public static String time;
	public static String TestID = "";
	@FXML
	TextField idTextField;
	@FXML
	Label CourseName;
	@FXML
	Label DateLabel;
	@FXML
	Label Answer1;
	@FXML
	Label Answer2;
	@FXML
	Label Answer3;
	@FXML
	Label Answer4;
	@FXML
	Button Startbutton;
	@FXML
	Button nextButton;
	@FXML
	Button backButton;
	@FXML
	Button cancelButton;
/**
 * 
 * @return the time system time 
 */
	public String getTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		System.out.println(sdf.format(timestamp));
		return (sdf.format(timestamp));
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate localDate = LocalDate.now();
		DateLabel.setText(dtf.format(localDate));

		
		String strarr[] = MainPageForSudentTestController.testChoice.split("");


		strarr = MainPageForSudentTestController.testChoice.split("id:");
		strarr = strarr[1].split(" ");
		TestID = strarr[1];

		
	}
/*
 * Check if the user id is correct and start the test with the execution code.
 */
	public void Startbuttonclick(ActionEvent e) throws IOException {
		/* Going to Test */
		if (idTextField.getText().isEmpty() || !idTextField.getText().equals(LoginController.userid)) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Info");
			alert.setHeaderText(null);
			alert.setContentText("Your id Is not correct, please try again");
			alert.showAndWait();
		} else {
			try {
				Main.client.sendToServer("TimeForCheck@" + MainPageForSudentTestController.Ecode);
				Client.gotmsg = false;
				while (Client.gotmsg == false) {
					System.out.println("");
				}
				Client.gotmsg = false;
				SimpleDateFormat format = new SimpleDateFormat("HH:mm");
				Date date1 = null;
				try {
					date1 = format.parse(realtime);
				} catch (ParseException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				Date date2 = null;
				try {
					date2 = format.parse(getTime());
				} catch (ParseException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				System.out.println(realtime);
				difference = date2.getTime() - date1.getTime();
				difference = difference / ((1000 * 60));
				if (difference > 30) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Info");
					alert.setHeaderText(null);
					alert.setContentText("Test time is lock for you -> you too late");
					alert.showAndWait();
					Parent Testpage = FXMLLoader.load(getClass().getResource("/gui/MainStudentScreen.fxml"));
					Scene TestStage = new Scene(Testpage);
					Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
					window.setScene(TestStage);
					window.setTitle("Main Screen");
					window.show();

				} else {
					Main.client.sendToServer("GetQuestionForTest@" + TestID + "@");
					Client.gotmsg = false;
					while (Client.gotmsg == false) {
						System.out.println("");
					}
					System.out.println("The test questions in load to arraylist");
					Client.gotmsg = false;
					Main.client.sendToServer("GetDurationTimeForTest@" + TestID);
					while (Client.gotmsg == false) {
						System.out.println("");
					}
					System.out.println("The test time uploaded");
					Client.gotmsg = false;
					Parent Testpage = FXMLLoader.load(getClass().getResource("/gui/TestPage.fxml"));
					Scene TestStage = new Scene(Testpage);
					Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
					TestStage.getStylesheets()
							.add(getClass().getResource("/gui/createQuestionCSS.css").toExternalForm());
					window.setScene(TestStage);
					window.setTitle(" ");
					window.show();

				}

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}