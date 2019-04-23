package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import Server.*;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class QuestionController implements Initializable {
	int Qcoutner = -1;
	ToggleGroup group;
	Connection con;
	Statement stmt;
	ResultSet rs;
	Client client_temp;
	private String ID;
	@FXML
	Button cancel;
	@FXML
	Button create;
	@FXML
	Label error1;
	@FXML
	Label error2;
	@FXML
	Label error4;
	@FXML
	Label error5;
	@FXML
	Label error6;
	@FXML
	Label missingError;
	@FXML
	Label answerError;
	@FXML
	JFXTextField insField;
	@FXML
	JFXTextField qField;
	@FXML
	JFXTextField ans1;
	@FXML
	JFXTextField ans2;
	@FXML
	JFXTextField ans3;
	@FXML
	JFXTextField ans4;
	@FXML
	RadioButton r1;
	@FXML
	RadioButton r2;
	@FXML
	RadioButton r3;
	@FXML
	RadioButton r4;
	@FXML
	ComboBox<String> prof;
	@FXML
	TableView<CourseTable> courseTable;
	@FXML
	TableColumn<CourseTable, CheckBox> checkCol;
	@FXML
	TableColumn<CourseTable, String> cNameCol;
	@FXML
	TableView<QuestionTable> questionTable;
	@FXML
	TableColumn<QuestionTable, String> idCol;
	@FXML
	TableColumn<QuestionTable, String> questionCol;
	@FXML
	TableColumn<QuestionTable, String> ans1Col;
	@FXML
	TableColumn<QuestionTable, String> ans2Col;
	@FXML
	TableColumn<QuestionTable, String> ans3Col;
	@FXML
	TableColumn<QuestionTable, String> ans4Col;
	@FXML
	TableColumn<QuestionTable, Integer> correctCol;
	@FXML
	TableColumn<QuestionTable, String> insCol;
	@FXML
	TableColumn<QuestionTable, ImageView> editCol;
	@FXML
	TableColumn<QuestionTable, ImageView> delCol;
	@FXML
	private Button save;
	private String questionEdited;
	boolean courseFlag=false;

	public void setTableValues(ActionEvent e) throws IOException, InterruptedException {
		Client.courseList.removeAll(Client.courseList);
		String profName = prof.getValue();
		String[] profArr = profName.split("-");
		profName = profArr[1];
		Client.gotmsg = false;
		Main.client.openConnection();
		Main.client.sendToServer("GetCourses@" + profName);
	
		while (!Client.gotmsg) {
			Thread.sleep(5);
		}

		courseTable.setVisible(true);
		ObservableList<CourseTable> list = FXCollections.observableArrayList();
		for (int i = 0; i < Client.courseList.size(); i++) {
			String temp = Client.courseList.get(i);
			;
			list.add(new CourseTable(temp, new CheckBox()));

		}
		courseTable.setItems(list);

	}

	public void cancelClick(ActionEvent e) throws IOException {

		Client.courseList.removeAll(Client.courseList);
		Parent mainScreenParent = FXMLLoader.load(getClass().getResource("/gui/MainScreen.fxml"));
		Scene mainScreenScene = new Scene(mainScreenParent);

		Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
		window.setScene(mainScreenScene);
		window.setTitle("Main Screen");
		window.show();

	}

	public void createClick(ActionEvent e) throws IOException, InterruptedException {
		if (qField.getText().equals("")) {
			missingError.setVisible(true);
			error1.setVisible(true);
		} else {
			error1.setVisible(false);

		}
		if (ans1.getText().equals("") || ans2.getText().equals("") || ans3.getText().equals("")
				|| ans4.getText().equals("")) {
			missingError.setVisible(true);
			error2.setVisible(true);
		} else {
			error2.setVisible(false);
		}
		if (insField.getText().equals("")) {
			missingError.setVisible(true);
			error4.setVisible(true);
		} else {
			error4.setVisible(false);
		}
		if (group.getSelectedToggle() == null) {
			answerError.setVisible(true);
		} else
			answerError.setVisible(false);
		if (prof.getValue() == null)
			error5.setVisible(true);
		else
			error5.setVisible(false);
		for(CourseTable ct: courseTable.getItems()) {
			if(ct.getCheckBox().isSelected())
			courseFlag=true;
		}
		if (!courseFlag)
			error6.setVisible(true);
		else
			error6.setVisible(false);
			
		if (qField.getText().equals("") == false && ans1.getText().equals("") == false
				&& ans2.getText().equals("") == false && ans3.getText().equals("") == false
				&& ans4.getText().equals("") == false && insField.getText().equals("") == false
				&& group.getSelectedToggle() != null && prof.getValue() != null && courseFlag!=false) {

			String lecName = Client.Lectname;
			String question = qField.getText();
			String[] ans = new String[4];
			ans[0] = ans1.getText();
			ans[1] = ans2.getText();
			ans[2] = ans3.getText();
			ans[3] = ans4.getText();
			RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
			int correctAnswer = Integer.parseInt(selectedRadioButton.getText());
			String[] professionArr = prof.getValue().split("-");
			String proffession = professionArr[0];
			String NewQuestionID = createQuestionID(proffession);
			String instructions = insField.getText();
			Question temp_que = new Question(NewQuestionID, lecName, question, ans[0], ans[1], ans[2], ans[3],
					correctAnswer,instructions);
			System.out.println(proffession + NewQuestionID);
			Client.gotmsg = false;
			InsertQuestiontoQuestionsTable(temp_que, proffession);
			while (!Client.gotmsg) {
				Thread.sleep(5);
			}
			Client.gotmsg = false;
			InsertQuestiontoQuestionInCourseTable(proffession + NewQuestionID);
			while (!Client.gotmsg) {
				Thread.sleep(5);
			}
			QuestionTable qb = new QuestionTable(proffession + NewQuestionID, question, ans[0], ans[1], ans[2], ans[3],
					correctAnswer, instructions, new ImageView("/resources/edit.png"),
					new ImageView("/resources/delete.png"));
			questionTable.getItems().add(qb);
			questionTable.getSortOrder().add(idCol);

		}
	}

	public String createQuestionID(String proffession) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		Main.client.sendToServer("getQuestionCountForEachProfession@" + proffession);
		Client.gotmsg = false;
		while (!Client.gotmsg) {
			Thread.sleep(5);
		}

		String strToReturn = "";
		if (Client.QuestionCounter <= 9) {
			strToReturn += "00";// 0300
		} else if (Client.QuestionCounter >= 10 && Client.QuestionCounter < 100) {
			strToReturn += "0";
		}
		strToReturn += Client.QuestionCounter;
		return strToReturn;
	}

	public void InsertQuestiontoQuestionsTable(Question question, String prof) throws IOException {
		try {
			Main.client.sendToServer("AddNewQuestion@" + prof + question.get_ID() + "@" + question.get_TeacherName()
					+ "@" + question.get_text() + "@" + question.get_a1() + "@" + question.get_a2() + "@"
					+ question.get_a3() + "@" + question.get_a4() + "@" + question.get_corr_answer() + "@"
					+ question.getInstructions());

		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	public void InsertQuestiontoQuestionInCourseTable(String questionID) throws IOException {
		try {

			CheckBox temp;
			ArrayList<String> courseList = new ArrayList<String>();
			courseList.add("AddToQuestionToQuestionInCourseTable");
			courseList.add(questionID);
			for (int i = 0; i < courseTable.getItems().size(); i++) {
				temp = checkCol.getCellData(i);
				if (temp.isSelected()) {
					courseList.add(cNameCol.getCellData(i));
				}
			}

			Main.client.sendToServer(courseList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getAllProf() throws IOException, InterruptedException {

		Client.gotmsg = false;
		Main.client.sendToServer("Initialize@" + LoginController.userid);
		System.out.println("HERE");
		while (!Client.gotmsg) {
			System.out.println("test test");
			Thread.sleep(5);
		}

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		idCol.setCellValueFactory(new PropertyValueFactory<QuestionTable, String>("_qID"));
		questionCol.setCellValueFactory(new PropertyValueFactory<QuestionTable, String>("_qText"));
		ans1Col.setCellValueFactory(new PropertyValueFactory<QuestionTable, String>("Ans1"));
		ans2Col.setCellValueFactory(new PropertyValueFactory<QuestionTable, String>("Ans2"));
		ans3Col.setCellValueFactory(new PropertyValueFactory<QuestionTable, String>("Ans3"));
		ans4Col.setCellValueFactory(new PropertyValueFactory<QuestionTable, String>("Ans4"));
		correctCol.setCellValueFactory(new PropertyValueFactory<QuestionTable, Integer>("CorrectAnswer"));
		insCol.setCellValueFactory(new PropertyValueFactory<QuestionTable, String>("_Instructions"));
		editCol.setCellValueFactory(new PropertyValueFactory<QuestionTable, ImageView>("EditImage"));
		delCol.setCellValueFactory(new PropertyValueFactory<QuestionTable, ImageView>("DelImage"));
		questionTable.setEditable(true);
		editCol.setStyle("-fx-alignment: CENTER;");
		delCol.setStyle("-fx-alignment: CENTER;");
		save.setVisible(false);
		questionTable.getSelectionModel().setCellSelectionEnabled(true); // selects cell only, not the whole row
		questionTable.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent click) {
				try {
					if (click.getClickCount() == 1) {
						@SuppressWarnings("rawtypes")
						TablePosition pos = questionTable.getSelectionModel().getSelectedCells().get(0);
						int row = pos.getRow();
						int col = pos.getColumn();
						@SuppressWarnings("rawtypes")
						TableColumn column = pos.getTableColumn();
						String val = column.getCellData(row).toString();
						System.out.println("Selected Value, " + val + ", Column: " + col + ", Row: " + row);
						QuestionTable qb = questionTable.getSelectionModel().getSelectedItem();
						if (col == 8) {
							Alert alert = new Alert(AlertType.CONFIRMATION,
									"Would you like to edit question #" + qb.get_qID() + "?", ButtonType.YES,
									ButtonType.NO);
							alert.showAndWait();

							if (alert.getResult() == ButtonType.YES) {
								qField.setText(qb.get_qText());
								ans1.setText(qb.getAns1());
								ans2.setText(qb.getAns2());
								ans3.setText(qb.getAns3());
								ans4.setText(qb.getAns4());
								questionEdited = qb.get_qID();
								insField.setText(qb.get_Instructions());
								if (qb.getCorrectAnswer() == 1)
									r1.setSelected(true);
								else if (qb.getCorrectAnswer() == 2)
									r2.setSelected(true);
								else if (qb.getCorrectAnswer() == 3)
									r3.setSelected(true);
								else if (qb.getCorrectAnswer() == 4)
									r4.setSelected(true);
								save.setVisible(true);
								String profession = qb.get_qID().substring(0, 2);
								System.out.println("Profession:" + profession);
								for (String s : prof.getItems()) {
									if (s.contains(profession))
										prof.setValue(s);
								}
								try {
									setCheckBoxAfterEdit(qb.get_qID());
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								questionTable.getSelectionModel().clearSelection();
							}

						}

						if (col == 9) {
							Alert alert = new Alert(AlertType.CONFIRMATION,
									"Would you like to delete question #" + qb.get_qID() + "?", ButtonType.YES,
									ButtonType.NO);
							alert.showAndWait();

							if (alert.getResult() == ButtonType.YES) {
								System.out.println("Query to delete the question");
								try {
									Main.client.sendToServer("deleteSelectedQuestion@" + qb.get_qID());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								questionTable.getItems().remove(qb);
							}

						}
					}
				} catch (IndexOutOfBoundsException indexOutOfBoundsException) {
					System.out.println("Out of bounds");
				}
			}
		});
		try {
			getAllProf();
			setQuestionsTable();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Collections.sort(Client.myProffinitialize);
		for (int i = 0; i < Client.myProffinitialize.size(); i++) {
			prof.getItems().add(Client.myProffinitialize.get(i));
		}

		prof.setPromptText("Please pick one profession");
		group = new ToggleGroup();
		r1.setToggleGroup(group);
		r2.setToggleGroup(group);
		r3.setToggleGroup(group);
		r4.setToggleGroup(group);
		checkCol.setCellValueFactory(new PropertyValueFactory<CourseTable, CheckBox>("CheckBox"));
		cNameCol.setCellValueFactory(new PropertyValueFactory<CourseTable, String>("_cName"));
		checkCol.setStyle("-fx-alignment: CENTER;");
		courseTable.setVisible(false);
	}

	public void setCheckBoxAfterEdit(String qID) throws InterruptedException, IOException {
		for (CourseTable ct : courseTable.getItems())
			ct.getCheckBox().setSelected(false);
		Client.gotmsg = false;
		System.out.println("Question ID:" + qID);
		Main.client.sendToServer("getAllCoursesByQuestionCode@" + qID);
		while (!Client.gotmsg)
			Thread.sleep(5);
		for (int i = 0; i < Client.coursesByCode.size(); i++) {
			for (CourseTable ct : courseTable.getItems()) {

				if (Client.coursesByCode.get(i).equals(ct.get_cName())) {
					System.out.println(Client.coursesByCode.get(i) + "@@@" + ct.get_cName());
					ct.getCheckBox().setSelected(true);
				}

			}
		}
	}

	public void setQuestionsTable() throws IOException, InterruptedException {
		Client.gotmsg = false;
		Main.client.sendToServer("getAllQuestionsPerLecturer@" + Client.Lectname);
		while (!Client.gotmsg) {
			System.out.println("y");
			Thread.sleep(5);
		}
			
		ObservableList<QuestionTable> list = FXCollections.observableArrayList();
		if(!Client.questionTable.isEmpty()) {
		for (int i = 0; i < Client.questionTable.size(); i++) {
			QuestionTable temp;
			temp = Client.questionTable.get(i);
			list.add(temp);
		}
		questionTable.setItems(list);
		}
	}

	public void clearData() {
		qField.clear();
		ans1.clear();
		ans2.clear();
		ans3.clear();
		ans4.clear();
		insField.clear();
		group.getSelectedToggle().setSelected(false);
		prof.getItems().clear();
		courseTable.getItems().clear();

	}

	public void editedRow(QuestionTable qt) throws IOException, InterruptedException {
		qt.set_qText(qField.getText());
		qt.setAns1(ans1.getText());
		qt.setAns2(ans2.getText());
		qt.setAns3(ans3.getText());
		qt.setAns4(ans4.getText());
		RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
		int correctAnswer = Integer.parseInt(selectedRadioButton.getText());
		qt.setCorrectAnswer(correctAnswer);
		qt.set_Instructions(insField.getText());
		Main.client.sendToServer("UpdateQuestionsTableAfterEdit@" + qt.get_qID() + "@" + qt.get_qText() + "@"
				+ qt.getAns1() + "@" + qt.getAns2() + "@" + qt.getAns3() + "@" + qt.getAns4() + "@"
				+ qt.getCorrectAnswer() + "@" + qt.get_Instructions());
		Main.client.sendToServer("DeleteQuestionInCourseAfterEdit@" + qt.get_qID());

		InsertQuestiontoQuestionInCourseTable(qt.get_qID());
		save.setVisible(false);
	}

	public void saveClicked() throws IOException, InterruptedException {
		for (QuestionTable qt : questionTable.getItems()) {
			if (qt.get_qID() == questionEdited) {
				editedRow(qt);
				questionTable.refresh();
			}
		}

	}
}