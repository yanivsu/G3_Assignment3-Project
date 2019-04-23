package application;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class LockLiveTestController implements Initializable {


	
	@FXML
	private Button lockButton;

	@FXML
	private Button goBackButton;

	@FXML
	private ComboBox<String> liveTest;

	@FXML
	void goBackButtonClick(ActionEvent e) throws IOException
	{
		Parent mainScreenParent = FXMLLoader.load(getClass().getResource("/gui/MainScreen.fxml"));
		Scene mainScreenScene = new Scene(mainScreenParent);
		Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
		window.setScene(mainScreenScene);
		window.setTitle("Change Status");
		window.show();
	}
	@FXML
	void lockButtonClick(ActionEvent event) 
	{
		try {
			Main.client.sendToServer("ForceLockTest@" + liveTest.getSelectionModel().getSelectedItem());
			Client.gotmsg = false;
			while(Client.gotmsg == false) {
				System.out.println("");
			}
			Client.gotmsg = false;
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Info");
			alert.setHeaderText(null);
			alert.setContentText("Your test Locked!");
			alert.showAndWait();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		try {
			Main.client.sendToServer("GivemeActiTest");
			Client.gotmsg = false;
			while(Client.gotmsg == false) {
				System.out.println("");
			}
			Client.gotmsg = false;
			liveTest.getItems().addAll(Client.activeTest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
