package application;

import java.io.IOException;
import java.util.ArrayList;

import javafx.stage.Stage;
import mainPackage.Elevator;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class MainController
{
	public class ElevatorData
	{
		private SimpleIntegerProperty capacity;
		private SimpleFloatProperty velocity;
		
		private ElevatorData( int capacity, float velocity )
		{
			this.capacity = new SimpleIntegerProperty( capacity );
			this.velocity = new SimpleFloatProperty( velocity );
		}

		public int getCapacity()
		{
			return capacity.get();
		}

		public void setCapacity( int capacity )
		{
			this.capacity.set( capacity );
		}

		public float getVelocity()
		{
			return velocity.get();
		}

		public void setVelocity( float velocity )
		{
			this.velocity.set( velocity );
		}
	}
	
	Stage stage;
	
	// Elevator variables
	@FXML private TextField elevatorCapacityTextField;
	@FXML private TextField elevatorVelocityTextField;
	@FXML private TableView<ElevatorData> elevatorsTable;

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

		elevatorsTable.getColumns().get( 0 ).setCellValueFactory( new PropertyValueFactory( "capacity" ) );
		elevatorsTable.getColumns().get( 1 ).setCellValueFactory( new PropertyValueFactory( "velocity" ) );
	}
	
	
	public void AddElevator()
	{
		elevatorsTable.getItems().add(
				new ElevatorData(
						Integer.parseInt( elevatorCapacityTextField.getText() ),
						Float.parseFloat( elevatorVelocityTextField.getText() ) ) );
	}
	
	
	public void RemoveElevator()
	{
		ElevatorData elevator = elevatorsTable.getSelectionModel().getSelectedItem();
		if ( elevator != null )
		{
			elevatorsTable.getItems().remove( elevator );
		}
	}
	
	
	public void LoadSimulationScene()
	{
		System.out.println( elevatorsTable.getItems().get( 0 ).getCapacity() );
		try
		{
			FXMLLoader simulationLoader = new FXMLLoader( this.getClass().getResource( "Simulation.fxml" ) );
			AnchorPane simulationRoot = simulationLoader.load();
			SimulationController simulationController = simulationLoader.getController();
			simulationController.SetStage( stage );
			
			ArrayList<Elevator> elevators = new ArrayList<Elevator>();
			for ( ElevatorData elevatorData : elevatorsTable.getItems() )
			{
				elevators.add( new Elevator( elevatorData.getVelocity(), elevatorData.getCapacity() ) );
			}
			int numOfFloors = Integer.parseInt( numOfFloorsTextField.getText() );
			int numOfPeople = Integer.parseInt( numOfPeopleTextField.getText() );
			int numOfPeopleInGroup = Integer.parseInt( numOfPeopleInGroupTextField.getText() );
			int groupFloor = Integer.parseInt( groupFloorTextField.getText() );
			int simulationTime = Integer.parseInt( simulationTimeTextField.getText() );
			int groupPeriodTime = Integer.parseInt( groupPeriodTextField.getText() );
			
			simulationController.SetupSimulation(
					elevators,
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
