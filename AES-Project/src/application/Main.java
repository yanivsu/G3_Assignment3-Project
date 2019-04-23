package application;	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import java.io.IOException;

import java.sql.Connection;

public class Main extends Application {
	Connection con;
	public static Client client;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root= FXMLLoader.load(getClass().getResource("/gui/Login.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("User Login");
			primaryStage.setResizable(false);
			primaryStage.sizeToScene();
			scene.getStylesheets().add(getClass().getResource("/gui/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.getIcons().add(new Image("/resources/icon.png"));
			primaryStage.setOnCloseRequest( event -> {
				System.out.println("Closing Stage");
				
				try {
					Main.client.sendToServer("LogOut@"+LoginController.userid);
					client.closeConnection();
				} catch (IOException e) {	
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} );
		
		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException { 

	    String hostIP = args[0];
	    getConnection(hostIP);
	    client.openConnection();
		launch(args);
	
	}
	
	public static void getConnection(String hostIP)
	{
		client = new Client(hostIP, 1234);
		System.out.println(hostIP);
  		try 
		{
			client.openConnection();
			
		} catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!client.isConnected()) 
		{
			System.out.println("CLIENT NOT CONNECTED!!!");
		}
	}
}
