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
			FXMLLoader loader = new FXMLLoader( this.getClass().getResource( "Sample.fxml" ) );
			AnchorPane root = loader.load();
			MyController controller = loader.getController();
			controller.SetStage( primaryStage );
			
			Scene scene = new Scene( root, 800, 800 );
			
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
