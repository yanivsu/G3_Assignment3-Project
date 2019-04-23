package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import Server.*;
public class ManagerTestController implements Initializable {

	@FXML
	private Button goBackButton;

	@FXML
	private Label answerError;

	@FXML
	private TableView<ViewTestTable> testTable;

	@FXML
	private TableColumn<ViewTestTable, String> testidCol;



	@FXML
	private TableColumn<ViewTestTable, String> studentText;

	@FXML
	private TableColumn<ViewTestTable, String> lecturerText;

	@FXML
	private TableColumn<ViewTestTable, ImageView> viewCol;

	@FXML
	private TableColumn<ViewTestTable, String> lecturerCol;


	@FXML
	private TableColumn<ViewTestTable, String> durationTimeCol;

	private static String testID = "";

	public static String getTestID() {
		return testID;
	}


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

		testidCol.setCellValueFactory(new PropertyValueFactory<ViewTestTable, String>("TestID"));

		studentText.setCellValueFactory(new PropertyValueFactory<ViewTestTable, String>("StudentText"));
		lecturerText.setCellValueFactory(new PropertyValueFactory<ViewTestTable, String>("LecturerText"));
		viewCol.setCellValueFactory(new PropertyValueFactory<ViewTestTable, ImageView>("ViewImage"));
		lecturerCol.setCellValueFactory(new PropertyValueFactory<ViewTestTable, String>("Lecturer"));
		durationTimeCol.setCellValueFactory(new PropertyValueFactory<ViewTestTable, String>("DurationTime"));
		insertTestsIntoTable();

	}

	public void insertTestsIntoTable() throws IOException, InterruptedException {
		Client.gotmsg = false;
		Main.client.sendToServer("getAllTestsPerManager");
		while (!Client.gotmsg) {
			Thread.sleep(5);
		}

		ObservableList<ViewTestTable> list = FXCollections.observableArrayList();
		for (int i = 0; i < Client.managerTestTable.size(); i++) {
			ViewTestTable temp;

			temp = Client.managerTestTable.get(i);
			System.out.println(temp.getTestID());
			list.add(temp);
		}
		Client.managerTestTable.removeAll(Client.managerTestTable);
		testTable.setItems(list);
		addEventToTestTable();
	}

	private void addEventToTestTable() {
		testTable.getSelectionModel().setCellSelectionEnabled(true); // selects cell only, not the whole row
		testTable.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent click) {
				try {
					if (click.getClickCount() == 1) {
						@SuppressWarnings("rawtypes")
						TablePosition pos = testTable.getSelectionModel().getSelectedCells().get(0);
						int row = pos.getRow();
						int col = pos.getColumn();
						@SuppressWarnings("rawtypes")
						TableColumn column = pos.getTableColumn();
						String val = column.getCellData(row).toString();
						System.out.println("Selected Value, " + val + ", Column: " + col + ", Row: " + row);
						ViewTestTable qb = testTable.getSelectionModel().getSelectedItem();
						if (col == 5) {
							testID=qb.getTestID();
							Parent root = FXMLLoader.load(getClass().getResource("/gui/ViewTestWindow.fxml"));
							Stage popUp = new Stage();
							Scene scene = new Scene(root);
							popUp.initModality(Modality.APPLICATION_MODAL);
							popUp.initStyle(StageStyle.UNDECORATED);
							popUp.setResizable(false);
							popUp.sizeToScene();
							popUp.setScene(scene);
							popUp.show();
							testTable.getSelectionModel().clearSelection();
						}

					}
				} catch (IndexOutOfBoundsException indexOutOfBoundsException) {
					System.out.println("Out of bounds");
				}catch (NullPointerException nullPointerException) {
					System.out.println("Null Pointer");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

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