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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ManagerController implements Initializable{
	@FXML
	private Label dateLabel;
	@FXML
	private Label welcomeLabel;
	@FXML
	private Button viewQuestion;
	@FXML
	private Button viewTestButton;
	@FXML
	private Button approveDenyButton;
	@FXML
	private Button checkTestButton;
	@FXML
	private Button logOutButton;
	private static String statisticsFlag;
	public static int req_array_flag = 1 ; //Helps me to know if we already tried to get req list or not/
	public void approveDenyClick(ActionEvent e) throws IOException 
	{
		try {
			Main.client.openConnection();
			Main.client.sendToServer("importRequests");
			Client.lecTemp = false;
			while (Client.lecTemp == false) {
				System.out.println("a");
			}
			Client.lecTemp = false;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (Client.req_Array_Client.size() == 0) 
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("INFORMATION");
			alert.setHeaderText(null);
			alert.setContentText("There is no req for now");
			alert.showAndWait();

		} else {
			Parent CreateTestLoadSec = FXMLLoader.load(getClass().getResource("/gui/ManagerTestRequest.fxml"));
			Scene TestScene = new Scene(CreateTestLoadSec);
			Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
			window.setScene(TestScene);
			window.setTitle("View Approve/Deny");
			window.show();
		}
	}
	public void viewQuestions(ActionEvent e) throws IOException {

		Parent CreateTestLoadSec = FXMLLoader.load(getClass().getResource("/gui/MangerViewsQuestions.fxml"));
		Scene TestScene = new Scene(CreateTestLoadSec);
		Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
		window.setScene(TestScene);
		window.setTitle("View questions");
		window.show();
	}
	public void viewTests(ActionEvent e) throws IOException
	{
		Parent CreateTestLoadSec = FXMLLoader.load(getClass().getResource("/gui/ManagerViewTests.fxml"));
		Scene TestScene = new Scene(CreateTestLoadSec);
		Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
		window.setScene(TestScene);
		window.setTitle("View tests");
		window.show();
		System.out.println(statisticsFlag);
	}
	@FXML
    void statClicked(ActionEvent event) throws IOException {
	   System.out.println("meow meow");
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ManagerViewsStatistics.fxml"));
		Stage popUp = new Stage();
		Scene scene = new Scene(root);
		popUp.initModality(Modality.APPLICATION_MODAL);
		popUp.initStyle(StageStyle.UNDECORATED);
		popUp.setResizable(false);
		popUp.sizeToScene();
		popUp.setScene(scene);
		popUp.showAndWait();
    }
	@FXML
	void CheckTestResult(ActionEvent event) throws IOException {   		   
			Parent CreateTestLoadSec = FXMLLoader.load(getClass().getResource("/gui/ManagerCheckTests.fxml"));
			Scene TestScene = new Scene(CreateTestLoadSec);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			window.setScene(TestScene);
			window.setTitle("View tests");
			window.show();
		   
	   }
	public void logOutClick(ActionEvent e) throws IOException{
		Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/Login.fxml"));
		Scene loginScene = new Scene(loginParent);

		System.out.println(LoginController.userid);
		Main.client.sendToServer("LogOut@" + LoginController.userid);
		Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
		window.setScene(loginScene);
		window.setTitle("User Login");
		window.show();
	}
	public static void setStatisticsButton(String string) {
		statisticsFlag=string;
		
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		welcomeLabel.setText("Welcome " + LoginController.userid);
		LocalDate localDate = LocalDate.now();
		dateLabel.setText(dtf.format(localDate));

	}
}