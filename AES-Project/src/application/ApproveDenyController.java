package application;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ApproveDenyController implements Initializable
{
	//private Boolean[] req_flag; /*Helps me to know which */
	private int req_counter = 1 ; 
	@FXML
	private Button submitButton;
	@FXML
	private RadioButton rejectRatio;
	@FXML
	private RadioButton approveRatio;
	@FXML
	private Label timelabel;
	@FXML
	private Label exeCodeLabel;
	@FXML
	private TextField reqInfo;
	@FXML
	private Label date;
    @FXML
    private Label requestNum;
    @FXML
    private Label testCode;
    @FXML
    private Button nextButton;
    @FXML
    private Button backButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label couCode;
    @FXML
    private Label lecturerID;
    @FXML
    /**
     * The function is responsible for Clicking on "submit" button
     *  when a student finishes a test and sends it to the system.
     * @param event
     * @throws IOException
     * @throws InterruptedException
     */
    void submitClick(ActionEvent event)throws IOException, InterruptedException
    {
    	if(approveRatio.isSelected()) 
    	{
    		try {
				Main.client.sendToServer("UpdateAcTestTime@"+timelabel.getText()+"@" + testCode.getText() + "@" + exeCodeLabel.getText());
				Client.gotmsg = false;
				while(Client.gotmsg == false) {
					System.out.println("");
				}
				Client.gotmsg = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		if(Client.req_Array_Client.size() == 1)
    		{
    			Alert alert = new Alert(AlertType.INFORMATION);
    			alert.setTitle("INFORMATION");
    			alert.setHeaderText(null);
    			alert.setContentText("You answer to all req");
    			alert.showAndWait();
    			try {
    				Main.client.openConnection();
					Main.client.sendToServer("RemoveReq");
					Client.req_Array_Client.remove(0);
					Thread.sleep(85);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			Parent mainScreenParent = FXMLLoader.load(getClass().getResource("/gui/MainManagerScreen.fxml"));
    			Scene mainScreenScene = new Scene(mainScreenParent);
    			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
    			window.setScene(mainScreenScene);
    			window.setTitle("Main Screen");
    			window.show();
    		}
    		// while there are requests for approval
    		else if(Client.req_Array_Client.size() > req_counter)
    		{
    			requestNum.setText("Request number " + (req_counter) +  ":");
    			testCode.setText(Client.req_Array_Client.get(req_counter).getTestCode());	
    			couCode.setText(Client.req_Array_Client.get(req_counter).getCoursename());
    			lecturerID.setText(Client.req_Array_Client.get(req_counter).getLecturerID());
    			reqInfo.setText(Client.req_Array_Client.get(req_counter).getReson());
    			timelabel.setText(Client.req_Array_Client.get(req_counter).getAmountTime());
    			exeCodeLabel.setText(Client.req_Array_Client.get(req_counter).getExeCode());
    		//	Client.req_Array_Client.remove(req_counter-1);
    		//	req_counter++;
    			try {
    				Main.client.openConnection();
					Main.client.sendToServer("RemoveReq");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		
    	}
    	if(rejectRatio.isSelected()) 
    	{
    		if(Client.req_Array_Client.size() == 1)
			{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("INFORMATION");
				alert.setHeaderText(null);
				alert.setContentText("You answer to all req");
				alert.showAndWait();
				try {
					Main.client.openConnection();
					Main.client.sendToServer("RemoveReq");
					Client.req_Array_Client.remove(0);
					Thread.sleep(85);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Parent mainScreenParent = FXMLLoader.load(getClass().getResource("/gui/MainManagerScreen.fxml"));
    			Scene mainScreenScene = new Scene(mainScreenParent);
    			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
    			window.setScene(mainScreenScene);
    			window.setTitle("Main Screen");
    			window.show();
			}
    		else if(Client.req_Array_Client.size() > req_counter)
    		{
    			requestNum.setText("Request number " + (req_counter) +  ":");
    			testCode.setText(Client.req_Array_Client.get(req_counter).getTestCode());	
    			couCode.setText(Client.req_Array_Client.get(req_counter).getCoursename());
    			lecturerID.setText(Client.req_Array_Client.get(req_counter).getLecturerID());
    			reqInfo.setText(Client.req_Array_Client.get(req_counter).getReson());
    			timelabel.setText(Client.req_Array_Client.get(req_counter).getAmountTime());
    			exeCodeLabel.setText(Client.req_Array_Client.get(req_counter).getExeCode());
    			//Client.req_Array_Client.remove(req_counter-1);
    			//req_counter++;
    			try {
    				Main.client.openConnection();
					Main.client.sendToServer("RemoveReq");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    	if(rejectRatio.isSelected() ==false && approveRatio.isSelected() ==false)
    	{
    		Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("INFORMATION");
			alert.setHeaderText(null);
			alert.setContentText("You need to approve / deny");
			alert.showAndWait();
			
    	}
    	rejectRatio.setSelected(false);
    	approveRatio.setSelected(false);
    }
    /**
    * The function is responsible for clicking on "cancel" button =>
    * return to the main screen
    * @param event
    * @throws IOException
    */
    @FXML
    void cancelClick(ActionEvent event) throws IOException
    {
    	
    	Parent mainScreenParent = FXMLLoader.load(getClass().getResource("/gui/MainManagerScreen.fxml"));
		Scene mainScreenScene = new Scene(mainScreenParent);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(mainScreenScene);
		window.setTitle("Main Screen");
		window.show();
		
    }
    /**
     * The function is responsible for Clicking on "next" button =>
     *  display a new request on the screen(as long as it exists)
     * @param event
     * @throws InterruptedException
     */
    @FXML  
    void nextClick(ActionEvent event) throws InterruptedException 
    {
    	rejectRatio.setSelected(false);
    	approveRatio.setSelected(false);
    	 if (Client.req_Array_Client.size() == req_counter) 
    	 {
    		requestNum.setText("Request number " + (req_counter)+ " :");
  			testCode.setText(Client.req_Array_Client.get(req_counter-1).getTestCode());	
  			couCode.setText(Client.req_Array_Client.get(req_counter-1).getCoursename());
  			lecturerID.setText(Client.req_Array_Client.get(req_counter-1).getLecturerID());
  			reqInfo.setText(Client.req_Array_Client.get(req_counter-1).getReson());
  			timelabel.setText(Client.req_Array_Client.get(req_counter-1).getAmountTime());
  			exeCodeLabel.setText(Client.req_Array_Client.get(req_counter-1).getExeCode());
 			Alert alert = new Alert(AlertType.INFORMATION);
 			alert.setTitle("INFORMATION");
 			alert.setHeaderText(null);
 			alert.setContentText("This is the last Req for now");
 			alert.showAndWait(); 
    	 }
    	 if(Client.req_Array_Client.size() > req_counter)
 		{
 			requestNum.setText("Request number " + (req_counter+1)+ " :");
 			testCode.setText(Client.req_Array_Client.get(req_counter).getTestCode());	
 			couCode.setText(Client.req_Array_Client.get(req_counter).getCoursename());
 			lecturerID.setText(Client.req_Array_Client.get(req_counter).getLecturerID());
 			reqInfo.setText(Client.req_Array_Client.get(req_counter).getReson());
 			timelabel.setText(Client.req_Array_Client.get(req_counter).getAmountTime());
 			exeCodeLabel.setText(Client.req_Array_Client.get(req_counter).getExeCode());
 			req_counter++;
 		}
    

    }
    /**
     *  The function is responsible for Clicking on "back" button =>
     *  browse between existing requests
     * @param event
     * @throws InterruptedException
     */
    @FXML
    void backButtonClick(ActionEvent event) throws InterruptedException
    {
    	if(req_counter > 1) {
    		req_counter--;
    		requestNum.setText("Request number " + req_counter +  ":");
 			testCode.setText(Client.req_Array_Client.get(req_counter-1).getTestCode());	
 			couCode.setText(Client.req_Array_Client.get(req_counter-1).getCoursename());
 			lecturerID.setText(Client.req_Array_Client.get(req_counter-1).getLecturerID());
 			reqInfo.setText(Client.req_Array_Client.get(req_counter-1).getReson());
 			timelabel.setText(Client.req_Array_Client.get(req_counter-1).getAmountTime());
 			exeCodeLabel.setText(Client.req_Array_Client.get(req_counter-1).getExeCode());
 	    	rejectRatio.setSelected(false);
 	    	approveRatio.setSelected(false);
 	    	
    	}
    	if(req_counter == 1)
    	{
    		requestNum.setText("Request number 1:");
 			testCode.setText(Client.req_Array_Client.get(0).getTestCode());	
 			couCode.setText(Client.req_Array_Client.get(0).getCoursename());
 			lecturerID.setText(Client.req_Array_Client.get(0).getLecturerID());
 			reqInfo.setText(Client.req_Array_Client.get(0).getReson());
 			timelabel.setText(Client.req_Array_Client.get(0).getAmountTime());
 			exeCodeLabel.setText(Client.req_Array_Client.get(0).getExeCode());
 	    	rejectRatio.setSelected(false);
 	    	approveRatio.setSelected(false);
    	}
    }   
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate localDate = LocalDate.now();
			date.setText(dtf.format(localDate)); 
			testCode.setText(Client.req_Array_Client.get(0).getTestCode());	
			couCode.setText(Client.req_Array_Client.get(0).getCoursename());
			lecturerID.setText(Client.req_Array_Client.get(0).getLecturerID());
			reqInfo.setText(Client.req_Array_Client.get(0).getReson());
			timelabel.setText(Client.req_Array_Client.get(0).getAmountTime());
			exeCodeLabel.setText(Client.req_Array_Client.get(0).getExeCode());
			nextButton.setVisible(false);
			backButton.setVisible(false);
			requestNum.setVisible(false);
			//req_flag = new Boolean[Client.req_Array_Client.size()+1];
			//Arrays.fill(req_flag, Boolean.FALSE);
	}
}