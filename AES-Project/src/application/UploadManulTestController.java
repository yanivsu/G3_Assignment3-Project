package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UploadManulTestController implements Initializable
{	
	@FXML
	private Label timeLabel;
	@FXML 
	private TextField pathField;
	@FXML
	private Button uploadButton;
	@FXML
	private Button downloadButton;
	@FXML
	private Button submitButton;
	private Timeline stopWatchTimeline;
	private long secondTimer;
	public static String realtime;
	public static long difference;
	private int stoptestflag = 0;
	private String fileName;
	public String getTime() 
	{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		System.out.println(sdf.format(timestamp));
		return (sdf.format(timestamp));
	}
	private void setTimerDisplay() {
		if (stoptestflag == 0) {
			timeLabel.setText(
					String.format("%02d:%02d:%02d", secondTimer / 3600, (secondTimer % 3600) / 60, secondTimer % 60));
		}
	}
	private void startTimer() 
	{
		if (stoptestflag == 0) 
		{
			stopWatchTimeline = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent event) -> {
				//
				System.out.println(Client.timeforLivetest);
				if (secondTimer < Integer.parseInt(Client.timeforLivetest) * 60) {
					secondTimer++;
					setTimerDisplay();
				} else {
					System.out.println("i'm one " + stoptestflag);
					stoptestflag = 1;
					stopWatchTimeline.stop();
					try {
						showMessage();
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
	private void showMessage() throws IOException {
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
		Main.client.sendToServer("UpdateTestStatusForMan");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/MainStudentScreen.fxml"));
		Scene loginScene = new Scene(loginParent);
		Stage window = (Stage) timeLabel.getScene().getWindow();
		window.setScene(loginScene);
		window.setTitle("Main Page");
		window.show();
	}
	public void redownloadTest() throws IOException 
	{
		System.out.println("i am here"+MainPageForSudentTestController.testID);
		Main.client.sendToServer("openManualTest@"+MainPageForSudentTestController.testID);
	
	}
	public void openFileChooser(ActionEvent e) throws Exception{
		
		FileChooser fc = new FileChooser();
		File selectedFile = fc.showOpenDialog(null);
		if(selectedFile!= null)
		{
			pathField.setText(selectedFile.getAbsolutePath());
		}
		else {
			System.out.println("File is not valid");
		}
		fileName=selectedFile.getName();
		
	}
	public void submitTestToServer() throws IOException, InterruptedException {
		
		System.out.println("Submit has benn pressed "+fileName);
		Main.client.handleMessageFromClientUI("uploadManualTestToServer@"+this.fileName);
		Thread.sleep(5000);
		Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/MainStudentScreen.fxml"));
		Scene loginScene = new Scene(loginParent);
		Stage window = (Stage) timeLabel.getScene().getWindow();
		window.setScene(loginScene);
		window.setTitle("Main Page");
		window.show();
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		secondTimer = 0;
		try {
			Main.client.sendToServer("TimeForCheck@" + MainPageForSudentTestController.Ecode );
			Client.gotmsg = false;
			while(Client.gotmsg == false) {
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
			String[] strarr=MainPageForSudentTestController.testChoice.split("id:");
			strarr=strarr[1].split(" ");
			String TestID = strarr[1];
			Main.client.sendToServer("GetDurationTimeForTest@"+TestID );
			while(Client.gotmsg == false) {
				System.out.println("");
			}
			difference = date2.getTime() - date1.getTime(); 
			difference = difference/((1000*60));
			secondTimer = difference*60;
			if(difference > 30) 
			{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Info");
				alert.setHeaderText(null);
				alert.setContentText("Test time is lock for you -> you too late");
				alert.showAndWait();
				Parent Testpage = FXMLLoader.load(getClass().getResource("/gui/MainStudentScreen.fxml"));
				Scene TestStage = new Scene(Testpage);
				Stage window = (Stage) timeLabel.getScene().getWindow();
				window.setScene(TestStage);
				window.setTitle("Main Screen");
				window.show();
			}
			startTimer();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}