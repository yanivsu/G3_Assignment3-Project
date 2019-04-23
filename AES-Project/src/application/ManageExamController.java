package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;


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
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public class ManageExamController implements Initializable {

	@FXML
	private ComboBox profList;
	@FXML
	private ComboBox profList2;
	@FXML
	private ComboBox testComboBox;
	@FXML
	private ListView courses;
	@FXML
	private ListView courses2;
	@FXML
	TableView<CheckTable> tableView;
	@FXML
	TableColumn<CheckTable, String> QuestionIDCol;
	@FXML
	TableColumn<CheckTable, CheckBox> checkCol;
	@FXML
	TableColumn<CheckTable, String> TextCol;
	@FXML
	TableColumn<CheckTable, Integer> scoreCol;
	ObservableList<CheckTable> list;
	public static String profChoice;
	public static String CourseChoice = null;

	@FXML
	private Button createButton;
	@FXML
	private Label label2;
	@FXML
	private JFXTextField timeField;

	@FXML
	private Button newButton;
	@FXML
	private AnchorPane createPane;
	@FXML
	private AnchorPane editPane;
	@FXML
	private AnchorPane questionPane;
	@FXML
	private JFXTextArea studentTextArea;
	@FXML
	private JFXTextArea lecturerTextArea;
	@FXML
	private Button saveButton;
	@FXML
	private Label minutesLabel;
	private int finalScore = 0;
	private int dialogResult;
	private String testID = "";

	@FXML
	public void newClicked() {
		setVisible();
		studentTextArea.clear();
		lecturerTextArea.clear();
		editPane.setVisible(false);
		createPane.setVisible(true);
		questionPane.setVisible(true);
		timeField.clear();
		saveButton.setVisible(false);
		createButton.setVisible(true);
	}

	@FXML
	public void editClicked() {
		setVisible();
		studentTextArea.clear();
		lecturerTextArea.clear();
		saveButton.setVisible(true);
		editPane.setVisible(true);
		createPane.setVisible(false);
		questionPane.setVisible(false);
		createButton.setVisible(false);
	}
	public void setVisible() {
		timeField.setVisible(true);
		studentTextArea.setVisible(true);
		lecturerTextArea.setVisible(true);
		minutesLabel.setVisible(true);
	}
	@FXML
	public void goBackClick(ActionEvent e) throws IOException {
		Parent mainScreenParent = FXMLLoader.load(getClass().getResource("/gui/MainScreen.fxml"));
		Scene mainScreenScene = new Scene(mainScreenParent);
		Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
		window.setScene(mainScreenScene);
		window.setTitle("Main Screen");
		window.show();
	}

	public void saveClick(ActionEvent e) throws IOException, InterruptedException {
		System.out.println("111111111111111");
		String lecturerText = "null";
		String studentText = "null";
		String testID = (String) testComboBox.getSelectionModel().getSelectedItem();
		if (!studentTextArea.getText().isEmpty()) {
			studentText = studentTextArea.getText();
		}
		if (!lecturerTextArea.getText().isEmpty()) {
			lecturerText = lecturerTextArea.getText();
		}
		String duration = "";
		duration = timeField.getText();
		if (!duration.matches("-?([1-9][0-9]*)?")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText(null);
			alert.setContentText("Duration time must be integers only");
			alert.showAndWait();
		} else if (duration.equals("") || Integer.parseInt(duration) < 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText(null);
			alert.setContentText("you have to enter posiitive duration time");
			alert.showAndWait();
		} else {
			Client.gotmsg=false;
			Main.client.sendToServer("updateTestWithData@" + testID + "@" + duration+"@"+studentText+"@"+lecturerText);
			while (!Client.gotmsg) {
				Thread.sleep(5);
			}
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Successs");
			alert.setHeaderText(null);
			alert.setContentText("Test number:"+testID+" has been updated successfully");
			alert.showAndWait();
			
			Parent mainScreenParent = FXMLLoader.load(getClass().getResource("/gui/MainScreen.fxml"));
			Scene mainScreenScene = new Scene(mainScreenParent);
			Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
			window.setScene(mainScreenScene);
			window.setTitle("Main Screen");
			window.show();
		}

	}

	public void createClick(ActionEvent e) throws IOException, InterruptedException {
		String lecturerText = "null";
		String studentText = "null";
		int i = 0;
		int char_flag = 0;
		int int_flag = 0;
		int finalScore = 0;
		if (!studentTextArea.getText().isEmpty()) {
			studentText = studentTextArea.getText();
		}
		if (!lecturerTextArea.getText().isEmpty()) {
			lecturerText = lecturerTextArea.getText();

		}
		String duration = "";

		duration = timeField.getText();
		if (!duration.matches("-?([1-9][0-9]*)?")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText(null);
			alert.setContentText("Duration time must be integers only");
			alert.showAndWait();
		} else if (duration.equals("") || Integer.parseInt(duration) < 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText(null);
			alert.setContentText("you have to enter posiitive duration time");
			alert.showAndWait();
		} else {

			if (char_flag == 0 && int_flag == 0) {
				CheckBox temp_check = new CheckBox();
				for (i = 0; i < Main.client.questions.size(); i++) {
					temp_check = checkCol.getCellData(i);
					if (temp_check.isSelected()) {
						{
							finalScore += scoreCol.getCellData(i).intValue();
						}
					}
				}
			}

			Alert alertConfirm = new Alert(AlertType.CONFIRMATION,
					"The test score is " + finalScore + ", are you sure you want to continue?", ButtonType.YES,
					ButtonType.NO);
			alertConfirm.showAndWait();

			if (alertConfirm.getResult() == ButtonType.YES) {

				CheckBox temp_check = new CheckBox();
				String testID = "";
				try {

					Client.lecTemp = false;
					Main.client.sendToServer("idByName@" + profChoice + "@" + CourseChoice);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				while (Client.lecTemp == false) {
					System.out.println("T = 5");
				}
				Client.lecTemp = false;
				try {

					Main.client.sendToServer("testCount@" + Client.idProf + "@" + Client.idCourse);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				while (Client.lecTemp == false) {
					System.out.println("2");
				}
				Client.lecTemp = false;

				testID = Client.idProf + Client.idCourse;
				testID += Client.testCounter;

				int questionsCount = 0;
				for (i = 0; i < Main.client.questions.size(); i++) {
					temp_check = checkCol.getCellData(i);
					if (temp_check.isSelected()) {
						{
							questionsCount++;
							Client.lecTemp = false;
							try {
								Client.lecTemp = false;
								int Score = scoreCol.getCellData(i);
								// String Score=""+finalScore;
								Main.client.sendToServer("addTest@" + testID + "@"
										+ Main.client.questions.get(i).get_ID() + "@" + Score + "@" + Client.Lectname
										+ "@" + duration + "@" + studentText + "@" + lecturerText);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							while (Client.lecTemp == false) {
								System.out.println("12");
							}
							Client.lecTemp = false;
						}

					}
				}
				if (questionsCount == 0) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText(null);
					alert.setContentText("you have to choose at least one question");
					alert.showAndWait();
				} else {
					questionsCount = 0;

					System.out.println("finish");

					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Finish");
					alert.setHeaderText(null);
					alert.setContentText("Your test uploaded successfully, the test ID is:" + testID);
					alert.showAndWait();

					Parent mainScreenParent = FXMLLoader.load(getClass().getResource("/gui/MainScreen.fxml"));
					Scene mainScreenScene = new Scene(mainScreenParent);
					Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
					window.setScene(mainScreenScene);
					window.setTitle("Main Screen");
					window.show();
				}
			}
		}
	}

	public void initialize(URL url, ResourceBundle rb) {
		try {

			Main.client.sendToServer("proffesions@" + LoginController.userid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		profList.setPromptText("Please pick one profession");

		while (Client.lecTemp == false) {
			System.out.println("");
		}
		Client.lecTemp = false;

		if (Client.myProff.size() == 0) {
			profList.setPromptText("You don't teach any profession");

		} else {
			for (int i = 0; i < Client.myProff.size(); i++)
				profList.getItems().add(Client.myProff.get(i));

			profList.setOnAction((event) -> {
				// Button was clicked, do something...
				if (event.getSource().equals(profList)) {

					Client.lecTemp = false;
					profChoice = (String) profList.getValue();
					courses.getItems().clear();
					courses.getSelectionModel().clearSelection();
					try {

						Main.client.sendToServer("courses@" + LoginController.userid + "@" + profChoice);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					while (Client.lecTemp == false) {
						System.out.println("");
					}
					Client.lecTemp = false;
					courses.getItems().clear();

					for (int i = 0; i < Client.myProff.size(); i++)
						courses.getItems().add(Client.myProff.get(i));

				}

			});

		}
		checkCol.setCellValueFactory(new PropertyValueFactory<CheckTable, CheckBox>("CheckBox"));
		QuestionIDCol.setCellValueFactory(new PropertyValueFactory<CheckTable, String>("_ID"));
		TextCol.setCellValueFactory(new PropertyValueFactory<CheckTable, String>("_Text"));
		scoreCol.setCellValueFactory(new PropertyValueFactory<CheckTable, Integer>("_Score"));
		checkCol.setStyle("-fx-alignment: CENTER;");
		tableView.setEditable(true);
		// set the score column to be editable
		scoreCol.setCellFactory(TextFieldTableCell.<CheckTable, Integer>forTableColumn(new IntegerStringConverter()));
		scoreCol.setOnEditCommit(new EventHandler<CellEditEvent<CheckTable, Integer>>() {
			@Override
			public void handle(CellEditEvent<CheckTable, Integer> t) {
				((CheckTable) t.getTableView().getItems().get(t.getTablePosition().getRow())).setScore(t.getNewValue());

				/*
				 * if(t.getRowValue().getCheckBox().isSelected()==true) { temp_score +=
				 * t.getNewValue(); scorelabel.setText(String.valueOf(temp_score)); }
				 */
			}
		});
		try {
			Main.client.sendToServer("proffesions@" + LoginController.userid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (Client.lecTemp == false) {
			System.out.println("");
		}
		Client.lecTemp = false;

		if (Client.myProff.size() == 0) {
			profList2.setPromptText("You don't teach any profession");

		} else {
			for (int i = 0; i < Client.myProff.size(); i++)
				profList2.getItems().add(Client.myProff.get(i));

			profList2.setOnAction((event) -> {
				// Button was clicked, do something...

				if (event.getSource().equals(profList2)) {
					testComboBox.getItems().clear();

					Client.lecTemp = false;
					profChoice = (String) profList2.getValue();
					courses2.getItems().clear();
					courses2.getSelectionModel().clearSelection();
					try {
						Main.client.sendToServer("courses@" + LoginController.userid + "@" + profChoice);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					while (Client.lecTemp == false) {
						System.out.println("");
					}
					Client.lecTemp = false;
					courses2.getItems().clear();

					for (int i = 0; i < Client.myProff.size(); i++)
						courses2.getItems().add(Client.myProff.get(i));

				}
			});
		}
	}

	@FXML
	public void setTable() throws IOException {
		Client.gotmsg = false;
		Client.lecTemp = false;
		Main.client.questions.clear();
		CourseChoice = (String) courses.getSelectionModel().getSelectedItem();
		System.out.println(CourseChoice);
		if (CourseChoice == null) {
			Alert alertConfirm = new Alert(AlertType.WARNING, "you have to choose a course first", ButtonType.OK);
			alertConfirm.showAndWait();
		} else {
			try {

				Main.client.sendToServer("questionByCourse@" + CourseChoice);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while (Client.gotmsg == false && Client.lecTemp == false) {
				System.out.println("");
			}
			Client.gotmsg = false;
			Client.lecTemp = false;

			if (Main.client.questions.size() == 0) {
				Alert alert = new Alert(AlertType.CONFIRMATION, "This course doesn't contain any questions",
						ButtonType.YES);
				alert.showAndWait();
			}

			else {
				list = FXCollections.observableArrayList();
				for (int i = 0; i < Main.client.questions.size(); i++) {
					System.out.println(Main.client.questions.get(i).get_text());
				}
				for (int i = 0; i < Main.client.questions.size(); i++) {
					Server.Question temp;
					temp = Main.client.questions.get(i);
					list.add(new CheckTable(new CheckBox(), temp.get_ID(), temp.get_text(), 0));
				}
				tableView.setItems(list);
			}
		}

	}
/**
 * put the values int he screen
 * @throws IOException
 * @throws InterruptedException
 */
	public void setTestIDCombo() throws IOException, InterruptedException {
		testComboBox.getItems().clear();

		String prof = (String) profList2.getSelectionModel().getSelectedItem();
		String course = (String) courses2.getSelectionModel().getSelectedItem();
		Client.gotmsg = false;
		Main.client.sendToServer("GetTestsID@" + prof + "@" + course);
		while (!Client.gotmsg) {
			Thread.sleep(5);
		}
		if (Client.profCourseIDList.size() == 0) {
			testComboBox.setPromptText("No tests were found");
		} else {
			for (int i = 0; i < Client.profCourseIDList.size(); i++) {
				testComboBox.getItems().add(Client.profCourseIDList.get(i));
			}
			testComboBox.setPromptText("Select Test");
			Client.profCourseIDList.removeAll(Client.profCourseIDList);
		}
	}
/**
 * Get all the time and the execution code for each test
 * @throws IOException
 * @throws InterruptedException
 */
	public void testIDSelected() throws IOException, InterruptedException {
		if (testComboBox.getItems().size() > 0) {
			String testID = (String) testComboBox.getSelectionModel().getSelectedItem();
			Client.gotmsg = false;
			Main.client.sendToServer("getTestTimeAndExecutionCode@" + testID);
			while (!Client.gotmsg) {
				Thread.sleep(5);
			}
			System.out.print(Client.testDuration2);
			timeField.setText(Client.testDuration2);
			studentTextArea.setText(Client.studentText);

			lecturerTextArea.setText(Client.lecturerText);
		}
	}

}