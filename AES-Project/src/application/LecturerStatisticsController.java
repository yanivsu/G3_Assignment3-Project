package application;
import Server.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
/**
 * @author Hai Cohen
 */
public class LecturerStatisticsController implements Initializable {

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
	private Label averageLabel;

	@FXML
	private Label medianLabel;
	@FXML
	private ImageView exitImage;
	@FXML
	private AnchorPane gradesAnchor;
	/**
	 * If the image is clicked it closes the stage
	 * @param event
	 */
	@FXML
	void exitImageClicked(MouseEvent event) {
		Stage stage = (Stage) exitImage.getScene().getWindow();
		stage.close();
	}
	/**
	 * initializes the screen
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			setTestsCombo();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gradesAnchor.setVisible(false);

	}
	/**
	 * Sets the barchart with an histogram with the relevant test id and execution code
	 * @param gradesOfTest
	 * @throws InterruptedException
	 */
	private void setHistogram(StatisticData gradesOfTest) throws InterruptedException {
		
		barChart.setAnimated(false);

		XYChart.Series<String,Integer> set=new XYChart.Series<String,Integer>();
		System.out.println(gradesOfTest.getFirstRange()+" "+gradesOfTest.getSecondRange());
		String[] arr= {gradesOfTest.getFirstRange(),gradesOfTest.getSecondRange(),gradesOfTest.getThirdRange(),gradesOfTest.getFourthRange(),gradesOfTest.getFifthRange(),gradesOfTest.getSixthRange(),gradesOfTest.getSeventhRange(),gradesOfTest.getEigthRange(),gradesOfTest.getNinthRange(),gradesOfTest.getTenthRange()};
		for(int i=0;i<10;i++)
			System.out.println((i+1)+"is "+arr[i]);
		for(int i=0;i<90;i+=10) {
			set.getData().add(new XYChart.Data<String, Integer>(Integer.toString(i)+"-"+Integer.toString(i+9),Integer.parseInt(arr[i/10])));
			System.out.println(Integer.toString(i)+"-"+Integer.toString(i+9));
		}
		set.getData().add(new Data<String, Integer>("90-100",Integer.parseInt(arr[9])));
		barChart.getData().addAll(set);
	}
	/**
	 * Sets the test ID combobox with the relevant lecturer name
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void setTestsCombo() throws IOException, InterruptedException {

		System.out.println(Client.Lectname);
		Client.gotmsg = false;
		Main.client.sendToServer("getFinishedTestsPerLecturer@" + Client.Lectname);
		while (!Client.gotmsg) {
			Thread.sleep(5);
		}
		for (int i = 0; i < Client.testsOfLecturer.size(); i++)
			testCombo.getItems().add(Client.testsOfLecturer.get(i));
		Client.testsOfLecturer.removeAll(Client.testsOfLecturer);

	}
	/**
	 * Sets the execution code combobox with the relevant execution codes by the test ID 
	 * @param event
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@FXML
	private void setTestNumberCombo(ActionEvent event) throws IOException, InterruptedException {
		if (testNumberCombo.getItems().size() > 0)
			barChart.getData().clear();
		testNumberCombo.getItems().clear();
		gradesAnchor.setVisible(false);
		System.out.println(testCombo.getSelectionModel().getSelectedItem());
		Client.gotmsg = false;
		Main.client.sendToServer("getFinishedTestNumberPerLecturer@" + testCombo.getSelectionModel().getSelectedItem());
		while (!Client.gotmsg) {
			Thread.sleep(5);
		}
		for (int i = 0; i < Client.finishedTestNumber.size(); i++)
			testNumberCombo.getItems().add(Client.finishedTestNumber.get(i));
		Client.finishedTestNumber.removeAll(Client.finishedTestNumber);

	}
	/**
	 * Sends a message to the server to request the test statistical data 
	 * @param event
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@FXML
	private void testNumberClicked(ActionEvent event) throws IOException, InterruptedException {
		if (testNumberCombo.getItems().size() > 0) {
			System.out.println(testNumberCombo.getSelectionModel().getSelectedItem());
			Client.gotmsg = false;
			Main.client.sendToServer("getAllGradesByTestID@" + testCombo.getSelectionModel().getSelectedItem() + "@"
					+ testNumberCombo.getSelectionModel().getSelectedItem());
			while (!Client.gotmsg) {
				Thread.sleep(5);
			}
			System.out.println(Client.gradesOfTest.getAvreage());
			setValues(Client.gradesOfTest);
		}
	}


	/**
	 * Sets the average score and the median score in their labels and calls to the SetHistogram method
	 * @param gradesOfTest
	 * @throws InterruptedException
	 */
	private void setValues(StatisticData gradesOfTest) throws InterruptedException {
		gradesAnchor.setVisible(true);

		averageLabel.setText(gradesOfTest.getAvreage());
		medianLabel.setText(gradesOfTest.getMedian());
		setHistogram(gradesOfTest);
		
	

	}

}