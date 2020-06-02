package application;

import java.io.IOException;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class MainController
{
	Stage stage;
	
	// Elevator variables
	@FXML private TextField numOfElevatorsTextField;
	@FXML private TextField elevatorCapacityTextField;
	@FXML private TextField elevatorVelocityTextField;

	// Building variables
	@FXML private TextField numOfFloorsTextField;

	// People variables
	@FXML private TextField numOfPeopleTextField;
	@FXML private TextField numOfPeopleInGroupTextField;
	@FXML private TextField groupFloorTextField;
	@FXML private TextField groupPeriodTextField;
	
	// Simulation variables
	@FXML private TextField simulationTimeTextField;

	
	public void SetStage(Stage s)
	{
		this.stage = s;
	}
	
	
	public void LoadSimulationScene()
	{
		try
		{
			FXMLLoader simulationLoader = new FXMLLoader( this.getClass().getResource( "Simulation.fxml" ) );
			AnchorPane simulationRoot = simulationLoader.load();
			SimulationController simulationController = simulationLoader.getController();
			simulationController.SetStage( stage );
			
			int numOfElevators = Integer.parseInt( numOfElevatorsTextField.getText() );
			int elevatorCapacity = Integer.parseInt( elevatorCapacityTextField.getText() );
			float elevatorVelocity = Float.parseFloat( elevatorVelocityTextField.getText() );
			int numOfFloors = Integer.parseInt( numOfFloorsTextField.getText() );
			int numOfPeople = Integer.parseInt( numOfPeopleTextField.getText() );
			int numOfPeopleInGroup = Integer.parseInt( numOfPeopleInGroupTextField.getText() );
			int groupFloor = Integer.parseInt( groupFloorTextField.getText() );
			int simulationTime = Integer.parseInt( simulationTimeTextField.getText() );
			int groupPeriodTime = Integer.parseInt( groupPeriodTextField.getText() );
			
			simulationController.SetupSimulation(
					numOfElevators,
					elevatorCapacity,
					elevatorVelocity,
					numOfFloors,
					numOfPeople,
					numOfPeopleInGroup,
					groupFloor,
					simulationTime,
					groupPeriodTime );
			
			stage.getScene().setRoot( simulationRoot );
		} catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
