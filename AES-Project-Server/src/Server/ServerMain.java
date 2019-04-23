package Server;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ServerMain extends Application implements Initializable {
	static Connection con;
	@FXML
	private JFXTextField portField;
	@FXML
	private JFXComboBox<String> dbCombo;
	@FXML
	private JFXToggleButton toggle;
	@FXML
	private Button disconnectButton;
	@FXML
	private JFXTextField dbIP;
	@FXML
	private JFXTextField dbPort;
	@FXML
	private JFXTextField dbUsername;
	@FXML
	private JFXPasswordField dbPassword;
	@FXML
	private JFXTextField dbProject;
	private Server server;

	public static void main(String args[]) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/gui/ServerScreen.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("Server Management");
			primaryStage.setResizable(false);
			primaryStage.sizeToScene();
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.getIcons().add(new Image("/resources/icon.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fieldsEditable(boolean flag) {
		dbIP.setEditable(flag);
		dbPort.setEditable(flag);
		dbUsername.setEditable(flag);
		dbPassword.setEditable(flag);
		dbProject.setEditable(flag);
	}

	public void fieldsClear() {
		dbIP.clear();
		dbPort.clear();
		dbUsername.clear();
		dbPassword.clear();
		dbProject.clear();
	}

	public void amazonFields() {
		dbIP.setText("braudeprojectdb.cqbxzkh2oemx.us-east-1.rds.amazonaws.com");
		dbPort.setText("3306");
		dbUsername.setText("yanivsu");
		dbPassword.setText("Henry14#");
		dbProject.setText("Project");
	}

	public void dbSelected() {
		if (dbCombo.getSelectionModel().getSelectedItem().equals("Custom")) {
			fieldsEditable(true);
			fieldsClear();
		} else if (dbCombo.getSelectionModel().getSelectedItem().equals("Amazon")) {
			amazonFields();
			fieldsEditable(false);
		}

	}

	public void setConnection() throws IOException {
		if (toggle.isSelected() == true) {
			if (dbIP.getText().equals("")!=false || dbPort.getText().equals("")!=false || dbPassword.getText().equals("")!=false
					|| dbProject.getText().equals("")!=false) {
				toggle.setSelected(false);
				Alert alert = new Alert(AlertType.ERROR,
						"Missing Fields", ButtonType.OK);
				alert.showAndWait();
			} else {
				getDataFromDB(dbIP.getText(), dbPort.getText(), dbUsername.getText(), dbPassword.getText(),
						dbProject.getText());
				int port = Integer.parseInt(portField.getText());
				server = new Server(port);
				server.listen();
			}
		} else {
			server.close();
		}
	}

	public static void getDataFromDB(String ip, String port, String username, String password, String project) {

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			/* handle the error */}

		try {
			con = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + project, username, password);

		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	public void disconnectUsers() {
		Iterator<String> itr = server.getStatusCon().keySet().iterator();
		while (itr.hasNext()) {
			String k = itr.next();
			server.getStatusCon().put(k, 0);
			System.out.println("User:" + k + " Value:" + server.getStatusCon().get(k));

		}

	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		portField.setText("1234");
		dbCombo.setStyle("-fx-text-fill: white;");
		dbCombo.getItems().add("Amazon");
		dbCombo.getItems().add("Custom");
		dbCombo.getSelectionModel().select(0);
		amazonFields();
		fieldsEditable(false);
	}

}