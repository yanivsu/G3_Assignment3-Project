package application;
import Server.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXRadioButton;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CheckTestResult implements Initializable {
	@FXML
	private Button backButton;
	@FXML
	private TableView<checkTestBox> testTableView;
	@FXML
	private TableColumn<checkTestBox, CheckBox> checkCol;
	@FXML
	private TableColumn<checkTestBox, String> testIDCol;
	@FXML
	private TableColumn<checkTestBox, String> studentIDCol;
	@FXML
	private TableColumn<checkTestBox, String> explaination;
	@FXML
	private TableColumn<checkTestBox, String> statusCol;
	@FXML
	private TableColumn<checkTestBox, String> gradeCol;
	@FXML
	private TableColumn<checkTestBox, String> exeCode;
	@FXML
	private TableColumn<checkTestBox, ImageView> viewTestCol;
	@FXML
	private Button confirm;
	@FXML
	private Button deny;
	@FXML
	private ComboBox<String> testIDCombo;
	@FXML
	private ComboBox<String> executionCodeCombo;
	@FXML
	private JFXRadioButton waitRButton;
	@FXML
	private JFXRadioButton approvesRButton;
	@FXML
	private JFXRadioButton denyRButton;

	private ObservableList<checkTestBox> list;
	private String testIDtoShow = "", stuIDtoShow = "";
	private String explainToSend;
	public static String executionCode;
	ToggleGroup group = new ToggleGroup();
	/**
	 * This function initialize the screen with all professions and courses
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			setTestsCombo();
		} catch (IOException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		checkCol.setCellValueFactory(new PropertyValueFactory<checkTestBox, CheckBox>("CheckBox"));
		testIDCol.setCellValueFactory(new PropertyValueFactory<checkTestBox, String>("_tID"));
		studentIDCol.setCellValueFactory(new PropertyValueFactory<checkTestBox, String>("_stuID"));
		statusCol.setCellValueFactory(new PropertyValueFactory<checkTestBox, String>("_status"));
		gradeCol.setCellValueFactory(new PropertyValueFactory<checkTestBox, String>("_grade"));
		explaination.setCellValueFactory(new PropertyValueFactory<checkTestBox, String>("Explaination"));
		exeCode.setCellValueFactory(new PropertyValueFactory<checkTestBox, String>("ExeCode"));
		viewTestCol.setCellValueFactory(new PropertyValueFactory<checkTestBox, ImageView>("ViewImage"));
		checkCol.setStyle("-fx-alignment: CENTER;");
		gradeCol.setStyle("-fx-alignment: CENTER;");

		testTableView.setEditable(true);
		explaination.setCellFactory(TextFieldTableCell.forTableColumn());
		waitRButton.setToggleGroup(group);
		approvesRButton.setToggleGroup(group);
		denyRButton.setToggleGroup(group);

		waitRButton.setVisible(false);
		approvesRButton.setVisible(false);
		denyRButton.setVisible(false);
		deny.setVisible(true);
		confirm.setVisible(true);

		explaination.setOnEditCommit(new EventHandler<CellEditEvent<checkTestBox, String>>() {
			@Override
			public void handle(CellEditEvent<checkTestBox, String> t) {
				if (t.getNewValue().length() > 100) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText(null);
					alert.setContentText("Explanation is too long [Max is 100 chars]");
					alert.showAndWait();
				} else {
					((checkTestBox) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setExplaination(t.getNewValue());
				}
				testTableView.getSelectionModel().clearSelection();
				testTableView.refresh();
			}
		});
		gradeCol.setCellFactory(TextFieldTableCell.forTableColumn());
		gradeCol.setOnEditCommit(new EventHandler<CellEditEvent<checkTestBox, String>>() {
			@Override
			public void handle(CellEditEvent<checkTestBox, String> t) {
				if (isInt(t.getNewValue())) {
					if (Integer.parseInt(t.getNewValue()) <= 100 && Integer.parseInt(t.getNewValue()) >= 0)
						((checkTestBox) t.getTableView().getItems().get(t.getTablePosition().getRow()))
								.set_grade(t.getNewValue());
					else {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Warning");
						alert.setHeaderText(null);
						alert.setContentText("Grade must be in the range 0-100");
						alert.showAndWait();
					}

				} else {

					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText(null);
					alert.setContentText("Duration time must be integers only");
					alert.showAndWait();
				}
				testTableView.getSelectionModel().clearSelection();
				testTableView.refresh();
			}
		});

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
						if (col == 6) {
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

	}

	public void toggleSelected(ActionEvent event) {
		String selectedToggle = "";
		if (group.getSelectedToggle() == waitRButton) {
			selectedToggle = "waiting for approval";
			deny.setVisible(true);
			confirm.setVisible(true);
		} else if (group.getSelectedToggle() == approvesRButton) {
			selectedToggle = "Approve";
			deny.setVisible(true);
			confirm.setVisible(false);
		} else if (group.getSelectedToggle() == denyRButton) {
			selectedToggle = "Deny";
			deny.setVisible(false);
			confirm.setVisible(true);
		}

		Client.gotmsg = false;
		try {
			Main.client.sendToServer("testApproveBySelection@" + testIDCombo.getSelectionModel().getSelectedItem() + "@"
					+ executionCodeCombo.getSelectionModel().getSelectedItem() + "@" + selectedToggle);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (Client.gotmsg == false) {
			System.out.println("waiting for tests to approve list");
		}
		Client.gotmsg = false;

		list = FXCollections.observableArrayList();

		String[] strArr;
		for (int i = 0; i < Client.testToApprove.size(); i++) {
			strArr = Client.testToApprove.get(i).split("@");
			// System.out.println(Client.testToApprove.get(i));
			// System.out.println(strArr);
			list.add(new checkTestBox(new CheckBox(), strArr[0], strArr[1], strArr[2], strArr[3],
					new ImageView("/resources/view.png"), "", strArr[4]));
			executionCode = strArr[4];
		}

		testTableView.setItems(list);
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
	/**
	 * The function shows a new/existing test along with its answers
	 * @throws IOException
	 */
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
	/**
	 * The function is responsible for Clicking on "back" button =>
    *  stop the progress and return to main screen
	 * @param e
	 * @throws Exception
	 */
	public void backButtonClick(ActionEvent e) throws Exception {
		Parent CreateTestParent = FXMLLoader.load(getClass().getResource("/gui/MainScreen.fxml"));
		Scene CreateTestScene = new Scene(CreateTestParent);
		Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
		window.setScene(CreateTestScene);
		window.setTitle("MainScreen");
		window.show();
	}
	/**
	 * The function is responsible for Clicking on "confirmClick" button =>
    *  send new test to approval
	 * @param e
	 * @throws Exception
	 */
	public void confirmClick(ActionEvent e) throws Exception {
		Client.lecTemp = false;

		String toConfirm = "approveTests " + executionCodeCombo.getSelectionModel().getSelectedItem() + " @";
		Button selectedButton = (Button) e.getSource();
		System.out.println(selectedButton.getText());
		String approveOrDeny = selectedButton.getText();
		CheckBox temp_check = new CheckBox();
		for (int i = 0; i < Client.testToApprove.size(); i++) {
			temp_check = (CheckBox) checkCol.getCellData(i);
			if (explaination.getCellData(i).isEmpty())
				explainToSend = "null";

			else
				explainToSend = explaination.getCellData(i);
			if (temp_check.isSelected()) {
				{
					toConfirm += testIDCol.getCellData(i) + "~" + studentIDCol.getCellData(i) + "~" + approveOrDeny
							+ "~" + explainToSend + "~" + gradeCol.getCellData(i) + "@";
				}
			}
		}
		System.out.println(toConfirm);

		Main.client.sendToServer(toConfirm);

		while (Client.lecTemp == false) {
			System.out.println("change status to approve");
		}
		Client.lecTemp = false;

		Parent CreateTestParent = FXMLLoader.load(getClass().getResource("/gui/CheckTestResult.fxml"));
		Scene CreateTestScene = new Scene(CreateTestParent);
		Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
		window.setScene(CreateTestScene);
		window.setTitle("Check Test Result");
		window.show();
	}

	private void setTestsCombo() throws IOException, InterruptedException {

		System.out.println(Client.Lectname);
		Client.gotmsg = false;
		Main.client.sendToServer("getFinishedTestsPerLecturer@" + Client.Lectname);
		while (!Client.gotmsg) {
			Thread.sleep(5);
		}
		for (int i = 0; i < Client.testsOfLecturer.size(); i++)
			testIDCombo.getItems().add(Client.testsOfLecturer.get(i));
		Client.testsOfLecturer.removeAll(Client.testsOfLecturer);

	}

	@FXML
	private void setExecutionCodeCombo(ActionEvent event) throws IOException, InterruptedException {
		System.out.println("lalala");
		if (executionCodeCombo.getItems().size() > 0)
			testTableView.getItems().clear();
		executionCodeCombo.getItems().clear();
		System.out.println(testIDCombo.getSelectionModel().getSelectedItem());
		Client.gotmsg = false;
		Main.client
				.sendToServer("getFinishedTestNumberPerLecturer@" + testIDCombo.getSelectionModel().getSelectedItem());
		while (!Client.gotmsg) {
			Thread.sleep(5);
		}
		for (int i = 0; i < Client.finishedTestNumber.size(); i++)
			executionCodeCombo.getItems().add(Client.finishedTestNumber.get(i));
		Client.finishedTestNumber.removeAll(Client.finishedTestNumber);
		waitRButton.setVisible(false);
		approvesRButton.setVisible(false);
		denyRButton.setVisible(false);

	}

	@FXML
	void setTestTable(ActionEvent event) {
		Client.gotmsg = false;
		try {
			Main.client.sendToServer("testToApprove@" + testIDCombo.getSelectionModel().getSelectedItem() + "@"
					+ executionCodeCombo.getSelectionModel().getSelectedItem());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (Client.gotmsg == false) {
			System.out.println("waiting for tests to approve list");
		}
		Client.gotmsg = false;

		list = FXCollections.observableArrayList();

		String[] strArr;
		for (int i = 0; i < Client.testToApprove.size(); i++) {
			strArr = Client.testToApprove.get(i).split("@");
			// System.out.println(Client.testToApprove.get(i));
			// System.out.println(strArr);
			list.add(new checkTestBox(new CheckBox(), strArr[0], strArr[1], strArr[2], strArr[3],
					new ImageView("/resources/view.png"), "", strArr[4]));
			executionCode = strArr[4];
		}

		testTableView.setItems(list);
		waitRButton.setVisible(true);
		approvesRButton.setVisible(true);
		denyRButton.setVisible(true);
	}
}