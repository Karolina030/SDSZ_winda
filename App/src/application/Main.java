package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	
	public static void main( String[] args )
	{
		launch(args);
	}

	@Override
	public void start( Stage primaryStage )
	{
		try
		{
			FXMLLoader mainLoader = new FXMLLoader( this.getClass().getResource( "Main.fxml" ) );
			AnchorPane mainRoot = mainLoader.load();
			MainController mainController = mainLoader.getController();
			Scene scene = new Scene( mainRoot, 850, 850 );
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			mainController.SetStage( primaryStage );
			primaryStage.setScene( scene );
			primaryStage.setTitle( "Elevator simulation" );
			primaryStage.show();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
}
