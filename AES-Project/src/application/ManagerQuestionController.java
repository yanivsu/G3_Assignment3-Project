package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Server.*;
public class ManagerQuestionController implements Initializable {
	@FXML
	private Button goBackButton;
	@FXML
	TableView<ViewQuestionTable> questionTable;
	@FXML
	TableColumn<ViewQuestionTable, String> idCol;
	@FXML
	TableColumn<ViewQuestionTable, String> questionCol;
	@FXML
	TableColumn<ViewQuestionTable, String> ans1Col;
	@FXML
	TableColumn<ViewQuestionTable, String> ans2Col;
	@FXML
	TableColumn<ViewQuestionTable, String> ans3Col;
	@FXML
	TableColumn<ViewQuestionTable, String> ans4Col;
	@FXML
	TableColumn<ViewQuestionTable, Integer> correctCol;
	@FXML
	TableColumn<ViewQuestionTable, String> insCol;
	@FXML
	TableColumn<ViewQuestionTable, String> lecCol;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			setTable();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/* Sets the questions table with all the questions from the database */
	public void setTable() throws IOException, InterruptedException {

		idCol.setCellValueFactory(new PropertyValueFactory<ViewQuestionTable, String>("_qID"));
		questionCol.setCellValueFactory(new PropertyValueFactory<ViewQuestionTable, String>("_qText"));
		ans1Col.setCellValueFactory(new PropertyValueFactory<ViewQuestionTable, String>("Ans1"));
		ans2Col.setCellValueFactory(new PropertyValueFactory<ViewQuestionTable, String>("Ans2"));
		ans3Col.setCellValueFactory(new PropertyValueFactory<ViewQuestionTable, String>("Ans3"));
		ans4Col.setCellValueFactory(new PropertyValueFactory<ViewQuestionTable, String>("Ans4"));
		correctCol.setCellValueFactory(new PropertyValueFactory<ViewQuestionTable, Integer>("CorrectAnswer"));
		insCol.setCellValueFactory(new PropertyValueFactory<ViewQuestionTable, String>("_Instructions"));
		lecCol.setCellValueFactory(new PropertyValueFactory<ViewQuestionTable, String>("Lecturer"));
		insertQuestionsIntoTable();

	}

	public void insertQuestionsIntoTable() throws IOException, InterruptedException {
		Client.gotmsg = false;
		Main.client.handleMessageFromClientUI("getAllQuestionsPerManager");
		while (!Client.gotmsg) {
			Thread.sleep(5);
		}
		ObservableList<ViewQuestionTable> list = FXCollections.observableArrayList();

		for (int i = 0; i < Client.managerQuestionTable.size(); i++) {
			ViewQuestionTable temp;
			temp = Client.managerQuestionTable.get(i);
			System.out.println(temp.get_qID());
			list.add(temp);
		}
		Client.managerQuestionTable.removeAll(Client.managerQuestionTable);
		questionTable.setItems(list);
		
	}

	public void moveBackToMainScree(ActionEvent e) throws IOException {
		Parent mainScreenParent = FXMLLoader.load(getClass().getResource("/gui/MainManagerScreen.fxml"));
		Scene mainScreenScene = new Scene(mainScreenParent);
		Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
		window.setScene(mainScreenScene);
		window.setTitle("Main Student Screen");
		window.show();
	}
}
