package application;
import Server.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXRadioButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
/**
 * @author Hai Cohen
 */
public class ManagerStatisticsController implements Initializable {

	@FXML
	private AnchorPane lecturerPane;

	@FXML
	private ComboBox<String> teacherTestCombo;

	@FXML
	private ComboBox<String> teacherTestNumberCombo;
	@FXML
	private ComboBox<String> courseTestCombo;
	@FXML
	private ComboBox<String> lecturerNameCombo;
	@FXML
	private ComboBox<String> professions;
	@FXML
	private ImageView exitImage;

	@FXML
	private AnchorPane studentPane;

	@FXML
	private JFXRadioButton lecturerRadio;

	@FXML
	private JFXRadioButton courseRadio;

	@FXML
	private JFXRadioButton studentRadio;

	@FXML
	private AnchorPane coursePane;


	@FXML
	private BarChart<String, Integer> barChart;

	@FXML
	private CategoryAxis xChart;

	@FXML
	private NumberAxis yChart;

	@FXML
	private ComboBox<String> testCombo;

	@FXML
	private ComboBox<String> testNumberCombo;
	@FXML
	private ListView<String> courses;
	@FXML
	private ComboBox<String> studentIDCombo;
	private String profChoice;
	@FXML
	private ComboBox<String> courseTestExecutionCode;

	@FXML
	/*
	 * 
	 * closes the stage when the exit button is clicked
	 * 
	 */
	void exitImageClicked(MouseEvent event) {
		Stage stage = (Stage) exitImage.getScene().getWindow();
		stage.close();
	}
	/**
	 * Initializes the screen 
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ToggleGroup group = new ToggleGroup();
		lecturerRadio.setToggleGroup(group);
		studentRadio.setToggleGroup(group);
		courseRadio.setToggleGroup(group);
	}
	/**
	 * Sets the students' name combobox with all the students usernames
	 */
	@FXML

	private void setStudentCombo() throws IOException, InterruptedException {

		System.out.println(Client.Lectname);
		Client.gotmsg = false;
		Main.client.sendToServer("getAllStudents");
		while (!Client.gotmsg) {
			Thread.sleep(5);
		}
		for (int i = 0; i < Client.studentsList.size(); i++)
			studentIDCombo.getItems().add(Client.studentsList.get(i));
		Client.studentsList.removeAll(Client.studentsList);

	}
	/**
	 * Sets an histogram with given statistic data , ranges and test ID and execution code
	 * @param gradesOfTest
	 * @throws InterruptedException
	 */
	private void setHistogram(StatisticData gradesOfTest) throws InterruptedException {

		barChart.setAnimated(false);

		XYChart.Series<String, Integer> set = new XYChart.Series<String, Integer>();
		System.out.println(gradesOfTest.getFirstRange() + " " + gradesOfTest.getSecondRange());
		String[] arr = { gradesOfTest.getFirstRange(), gradesOfTest.getSecondRange(), gradesOfTest.getThirdRange(),
				gradesOfTest.getFourthRange(), gradesOfTest.getFifthRange(), gradesOfTest.getSixthRange(),
				gradesOfTest.getSeventhRange(), gradesOfTest.getEigthRange(), gradesOfTest.getNinthRange(),
				gradesOfTest.getTenthRange() };
		for (int i = 0; i < 90; i += 10) {
			set.getData().add(new XYChart.Data<String, Integer>(Integer.toString(i) + "-" + Integer.toString(i + 9),
					Integer.parseInt(arr[i / 10])));
		}
		set.setName(gradesOfTest.getTestID() + "-" + gradesOfTest.getExecutionCode() + " AVG: "
				+ gradesOfTest.getAvreage() + " Median: " + gradesOfTest.getMedian());
		set.getData().add(new Data<String, Integer>("90-100", Integer.parseInt(arr[9])));
		barChart.getData().addAll(set);
	}

	/**
	 * Sets the lecturer's test execution code with the relevant execution codes for the test ID
	 * @param event
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@FXML
	void setLecturerTestExecution(ActionEvent event) throws IOException, InterruptedException {
		if (teacherTestCombo.getItems().size() > 0) {
			teacherTestNumberCombo.getItems().clear();
			System.out.println(teacherTestCombo.getSelectionModel().getSelectedItem());
			Client.gotmsg = false;
			Main.client.sendToServer(
					"getFinishedTestNumberPerLecturer@" + teacherTestCombo.getSelectionModel().getSelectedItem());
			while (!Client.gotmsg) {
				Thread.sleep(5);
			}
			for (int i = 0; i < Client.finishedTestNumber.size(); i++)
				teacherTestNumberCombo.getItems().add(Client.finishedTestNumber.get(i));
			Client.finishedTestNumber.removeAll(Client.finishedTestNumber);
		}

	}
	/**
	 * Sets the lecturer's combobox with the relevant test ID by the lecturer's name
	 * @param event
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@FXML
	private void setTestIDCombo(ActionEvent event) throws IOException, InterruptedException {
		teacherTestCombo.getItems().clear();
		teacherTestNumberCombo.getItems().clear();
		String id = lecturerNameCombo.getSelectionModel().getSelectedItem();
		String[] arr = id.split("-");
		Client.gotmsg = false;
		Main.client.sendToServer("getFinishedTestsPerLecturer@" + arr[1]);
		while (!Client.gotmsg) {
			Thread.sleep(5);
		}
		for (int i = 0; i < Client.testsOfLecturer.size(); i++) {
			teacherTestCombo.getItems().add(Client.testsOfLecturer.get(i));
		}
		Client.testsOfLecturer.removeAll(Client.testsOfLecturer);

	}
	/**
	 * Sends a message to the server with the lecturer's name and required test in order to get all the statistic data for the histogram
	 * @param event
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@FXML
	private void testNumberClicked(ActionEvent event) throws IOException, InterruptedException {
		if (teacherTestNumberCombo.getItems().size() > 0) {
			System.out.println(teacherTestNumberCombo.getSelectionModel().getSelectedItem());
			Client.gotmsg = false;
			Main.client.sendToServer("getAllGradesByTestID@" + teacherTestCombo.getSelectionModel().getSelectedItem()
					+ "@" + teacherTestNumberCombo.getSelectionModel().getSelectedItem());
			while (!Client.gotmsg) {
				Thread.sleep(5);
			}
			System.out.println(Client.gradesOfTest.getAvreage());
			setHistogram(Client.gradesOfTest);

		}

	}
	/**
	 * Student's statistic data radiobutton was selected
	 * @param event
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@FXML
	void studentSelected(ActionEvent event) throws IOException, InterruptedException {
		System.out.println("student");
		coursePane.setVisible(false);
		lecturerPane.setVisible(false);
		studentPane.setVisible(true);
		barChart.getData().clear();
		professions.getItems().clear();
		studentIDCombo.getItems().clear();
		setStudentCombo();
	}
	/**
	 * Lecturer's statistic data radiobutton was selected
	 * @param event
	 */
	@FXML
	void lecturerSelected(ActionEvent event) {
		System.out.println("lecturer");
		coursePane.setVisible(false);
		lecturerPane.setVisible(true);
		studentPane.setVisible(false);
		professions.getItems().clear();
		barChart.getData().clear();
		try {
			setLecturerNamesCombo();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Sets the lecturer's combobox with all the lecturers names from the database
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void setLecturerNamesCombo() throws IOException, InterruptedException {
		if (lecturerNameCombo.getItems().size() > 0)
			lecturerNameCombo.getItems().clear();
		Client.gotmsg = false;
		Main.client.sendToServer("getAllLecturerNames");
		while (!Client.gotmsg) {
			Thread.sleep(5);
		}
		for (Lecturer lec : Main.client.lecturers) {
			lecturerNameCombo.getItems().add(lec.getLecturerID() + "-" + lec.getLecturerName());
		}
		Main.client.lecturers.removeAll(Main.client.lecturers);

	}
	/**
	 * Course's statistic data radiobutton was selected
	 * @param event
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@FXML
	void courseSelected(ActionEvent event) throws InterruptedException, IOException {
		System.out.println("course");
		coursePane.setVisible(true);
		lecturerPane.setVisible(false);
		studentPane.setVisible(false);
		barChart.getData().clear();
		setCourseCombo();

	}
	/**
	 * Sets all the courses names from the database
	 */
	private void setCourseCombo() {
		Client.lecTemp = false;
		try {
			Main.client.sendToServer("getAllProffesions");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (Client.lecTemp == false) {
			System.out.println("q");
		}
		Client.lecTemp = false;

		if (Client.myProff.size() == 0) {
			professions.setPromptText("You don't teach any profession");

		} else {
			for (int i = 0; i < Client.myProff.size(); i++)
				professions.getItems().add(Client.myProff.get(i));
		}

	}
	/**
	 * Fills the courses ListView with the returned data from the db
	 * @param e1
	 * @throws InterruptedException
	 */
	@FXML
	public void setCoursesList(ActionEvent e1) throws InterruptedException {
		if (e1.getSource() == professions) {
			courseTestCombo.getItems().clear();
			courseTestExecutionCode.getItems().clear();
			Client.myProff.removeAll(Client.myProff);

			Client.lecTemp = false;
			profChoice = (String) professions.getValue();
			try {
				Main.client.sendToServer("getAllCourseNames@" + profChoice);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while (Client.lecTemp == false) {
				Thread.sleep(5);
			}
			Client.lecTemp = false;
			courses.getItems().clear();
			for (int i = 0; i < Client.myProff.size(); i++) {

				courses.getItems().add(Client.myProff.get(i));
			}
			Client.profCourseIDList.removeAll(Client.profCourseIDList);
		}
	}
	/**
	 * Sets all the tests ID in its' combobox
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void setIDCombo() throws IOException, InterruptedException {
		String prof = (String) professions.getSelectionModel().getSelectedItem();
		String course = (String) courses.getSelectionModel().getSelectedItem();
		courseTestCombo.getItems().clear();
		courseTestExecutionCode.getItems().clear();
		Main.client.sendToServer("getFinishedTestsPerCourse@" + prof + "@" + course);
		Client.gotmsg = false;
		while (!Client.gotmsg) {
			Thread.sleep(5);
		}
		if (Client.testsOfcourse.size() == 0) {
			courseTestCombo.getItems().clear();
			courseTestCombo.setPromptText("No tests were found");

		} else {
			for (int i = 0; i < Client.testsOfcourse.size(); i++) {
				courseTestCombo.getItems().add(Client.testsOfcourse.get(i));
			}
			courseTestCombo.setPromptText("Select Test");

		}
	}
	/**
	 * Sets all the test id's execution codes in the execution code combobox
	 * @param event
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@FXML
	void setCourseTestExecution(ActionEvent event) throws IOException, InterruptedException {
		if (courseTestCombo.getItems().size() > 0) {
			courseTestExecutionCode.getItems().clear();
			System.out.println(courseTestCombo.getSelectionModel().getSelectedItem());
			Client.gotmsg = false;
			Main.client.sendToServer(
					"getFinishedTestNumberPerLecturer@" + courseTestCombo.getSelectionModel().getSelectedItem());
			while (!Client.gotmsg) {
				Thread.sleep(5);
			}
			for (int i = 0; i < Client.finishedTestNumber.size(); i++)
				courseTestExecutionCode.getItems().add(Client.finishedTestNumber.get(i));
			Client.finishedTestNumber.removeAll(Client.finishedTestNumber);
		}

	}
	/**
	 * After an execution code of a test was selected asks for the database for its' statistic data
	 * @param event
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@FXML
	void courseTestNumberCombo(ActionEvent event) throws IOException, InterruptedException {
		if (courseTestExecutionCode.getItems().size() > 0) {
			System.out.println(courseTestExecutionCode.getSelectionModel().getSelectedItem());
			Client.gotmsg = false;
			Main.client.sendToServer("getAllGradesByTestID@" + courseTestCombo.getSelectionModel().getSelectedItem()
					+ "@" + courseTestExecutionCode.getSelectionModel().getSelectedItem());
			while (!Client.gotmsg) {
				Thread.sleep(5);
			}

			setHistogram(Client.gradesOfTest);

		}
	}
	/**
	 * After a student username was selected it uses a method to set the student grades in a barchart
	 * @param event
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@FXML
	void setStudentStatistic(ActionEvent event) throws IOException, InterruptedException {
		if (studentIDCombo.getItems().size() > 0) {
			String studentSelected = studentIDCombo.getSelectionModel().getSelectedItem();
			setStudentGrades(studentSelected);
		}

	}
	/**
	 * Sends a message to the database to get all of the selected student's statistic data
	 * @param studentSelected
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void setStudentGrades(String studentSelected) throws IOException, InterruptedException {
		Client.gotmsg = false;
		Main.client.sendToServer("getAllGradesByStudentID@" + studentSelected);
		while (!Client.gotmsg) {
			Thread.sleep(5);
		}

		for (StudentGrade sg : Main.client.studentGradesData) {
			System.out.println(sg.getGrade());
		}
		barChart.setAnimated(false);

		XYChart.Series<String, Integer> set = new XYChart.Series<String, Integer>();

		for (StudentGrade sg : Main.client.studentGradesData) {
			set.getData().add(
					new XYChart.Data<String, Integer>(sg.getTestID() + "-" + sg.getExecutionCode(), sg.getGrade()));
		}
		barChart.getData().addAll(set);
		Main.client.studentGradesData.removeAll(Main.client.studentGradesData);
	}

}