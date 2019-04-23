package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


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
public class ManagerCheckTests  implements Initializable {

	@FXML
	private TableView<checkTestBox> testTableView;
    @FXML
    private TableColumn<checkTestBox, String> testIDCol;

    @FXML
    private TableColumn<checkTestBox, String> exeCodeCol;

    @FXML
    private TableColumn<checkTestBox, String> studentIDCol;

    @FXML
    private TableColumn<checkTestBox, String> gradeCol;

    @FXML
    private TableColumn<checkTestBox, String> statusCol;

    @FXML
    private TableColumn<checkTestBox, ImageView> viewCol;
    @FXML
    private TableColumn<checkTestBox, String> typeCol;

    @FXML
    private Button backCol;
    
	private ObservableList<checkTestBox> list;
	private String testIDtoShow = "", stuIDtoShow = "";
	public static String executionCode;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("waf waf");
		// TODO Auto-generated method stub
		testIDCol.setCellValueFactory(new PropertyValueFactory<checkTestBox, String>("_tID"));
		studentIDCol.setCellValueFactory(new PropertyValueFactory<checkTestBox, String>("_stuID"));
		statusCol.setCellValueFactory(new PropertyValueFactory<checkTestBox, String>("_status"));
		gradeCol.setCellValueFactory(new PropertyValueFactory<checkTestBox, String>("_grade"));
		exeCodeCol.setCellValueFactory(new PropertyValueFactory<checkTestBox, String>("ExeCode"));
		viewCol.setCellValueFactory(new PropertyValueFactory<checkTestBox, ImageView>("ViewImage"));
		typeCol.setCellValueFactory(new PropertyValueFactory<checkTestBox, String>("TestType"));

		viewCol.setStyle("-fx-alignment: CENTER;");
		gradeCol.setStyle("-fx-alignment: CENTER;");
		

		testTableView.getSelectionModel().setCellSelectionEnabled(true); // selects cell only, not the whole row
		testTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent click) {
				try {
					if (click.getClickCount() == 1) {
						@SuppressWarnings("rawtypes")
						TablePosition pos = testTableView.getSelectionModel().getSelectedCells().get(0);
						int row = pos.getRow();
						int col = pos.getColumn();
						@SuppressWarnings("rawtypes")
						TableColumn column = pos.getTableColumn();
						String val = column.getCellData(row).toString();
						System.out.println("Selected Value, " + val + ", Column: " + col + ", Row: " + row);
						checkTestBox qb = testTableView.getSelectionModel().getSelectedItem();
						if (col == 5) {
							System.out.print("meow meow");
							testTableView.getSelectionModel().clearSelection();
							stuIDtoShow = qb.get_stuID();
							testIDtoShow = qb.get_tID();
							ShowTest();
						} else
							testTableView.getSelectionModel().clearSelection();

					}
				} catch (IndexOutOfBoundsException | IOException | NullPointerException e) {
					System.out.println("exception");
				}
			}
		});
		
		Client.gotmsg = false;
		try {
			Main.client.sendToServer("testApproveManager");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (Client.gotmsg == false) {
			System.out.println("waiting for tests to approve list");
		}
		Client.gotmsg = false;

		testTableView.getItems().addAll(Client.managerViewsCheckedTests);



		//testTableView.setItems(list);

		
	}
	
	public void ShowTest() throws IOException {
		Client.lecTemp = false;
		Main.client.sendToServer("stuAnsAndCorrAns@" + testIDtoShow + "@" + stuIDtoShow);

		while (Client.lecTemp == false) {
			System.out.println("stu Answers");
		}
		Client.lecTemp = false;

		Parent root = FXMLLoader.load(getClass().getResource("/gui/ViewTestWindowForStudent.fxml"));
		Stage popUp = new Stage();
		Scene scene = new Scene(root);
		popUp.initModality(Modality.APPLICATION_MODAL);
		popUp.initStyle(StageStyle.UNDECORATED);
		popUp.setResizable(false);
		popUp.sizeToScene();
		scene.getStylesheets().add(getClass().getResource("/gui/application.css").toExternalForm());
		popUp.setScene(scene);
		popUp.show();
	}

	public void backButtonClick(ActionEvent e) throws Exception {
		Parent CreateTestParent = FXMLLoader.load(getClass().getResource("/gui/MainManagerScreen.fxml"));
		Scene CreateTestScene = new Scene(CreateTestParent);
		Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
		window.setScene(CreateTestScene);
		window.setTitle("MainScreen");
		window.show();
	}

	public ObservableList<checkTestBox> getList() {
		return list;
	}

	public void setList(ObservableList<checkTestBox> list) {
		this.list = list;
	}

}
