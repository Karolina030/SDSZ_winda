package application;

import mainPackage.*;

import java.util.ArrayList;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;

public class SimulationController
{
	Stage stage;
	
	@FXML private Button startButton;
	@FXML private GridPane buildingPane;
	@FXML private Label height1Label;
	@FXML private Label height2Label;
	@FXML private GridPane elevator1Pane;
	@FXML private GridPane elevator2Pane;
	
	@FXML private Pane resultsPane;
	@FXML private Label numOfPeopleLabel;
	@FXML private Label avgWaitingTimeLabel;

	private ArrayList<RadioButton> floorButtons = new ArrayList<RadioButton>();
	private Simulation simulation;
	
	public void SetStage(Stage s)
	{
		this.stage = s;
	}
	
	
	public void SetupSimulation(
			int numOfElevators,
			int elevatorCapacity,
			float elevatorVelocity,
			int numOfFloors,
			int numOfPeople,
			int numOfPeopleInGroup,
			int groupFloor,
			int simulationTime,
			int groupPeriodTime )
	{
		int simulationSpeed = 1;
		
		ArrayList<Elevator> elevators = new ArrayList<Elevator>();
		
		for( int i = 0; i < numOfElevators; i++ )
		{
			Elevator elevator = CreateElevator( i, numOfFloors, elevatorVelocity, elevatorCapacity );
			elevators.add( elevator );
		}
		
		SetBuildingPane( numOfFloors );
		
		Building building = new Building( numOfFloors, numOfPeople, elevators, floorButtons );
		building.GeneratePeopleQueue( simulationTime );
		building.GenerateEvent( simulationTime, groupPeriodTime, numOfPeopleInGroup, groupFloor );
		
		for( int i = 0; i < numOfElevators; i++ )
		{
			elevators.get(i).SetBuilding( building );
		}
		
		simulation = new Simulation( building, simulationTime, simulationSpeed, this );	
	}
	
	
	public void StartSimulation()
	{
		startButton.setDisable( true );
		
		simulation.start();
	}
	
	
	public void ShowResults( Results results )
	{
		Platform.runLater( new Runnable()
		{
			public void run()
			{
				resultsPane.setVisible( true );
				numOfPeopleLabel.setText( String.valueOf( results.TotalPeople ) );
				avgWaitingTimeLabel.setText( String.valueOf( results.AvgWaitingTime ) );
			}
		});
	}
	
	
	private Elevator CreateElevator( int elevatorNumber, int numOfFloors, float velocity, int capacity )
	{
		ArrayList<GridPane> elevatorPanes = null;
		Label heightLabel = null;
		
		if ( elevatorNumber < 2 )
		{
			elevatorPanes = CreateElevatorPanes( numOfFloors, elevatorNumber );
			if ( elevatorNumber == 0 )
			{
				heightLabel = height1Label;
			}
			else
			{
				heightLabel = height2Label;
			}
		}
		
		return new Elevator( velocity, capacity, heightLabel, elevatorPanes );
	}
	
	
	private ArrayList<GridPane> CreateElevatorPanes( int numOfFloors, int elevatorNumber )
	{
		ArrayList<GridPane> result = new ArrayList<GridPane>();
		
		GridPane elevatorPane = null;
		if ( elevatorNumber == 0 )
		{
			elevatorPane = elevator1Pane;
		}
		else if ( elevatorNumber == 1 )
		{
			elevatorPane = elevator2Pane;
		}

		for( int i = 0; i < numOfFloors; i++ )
		{
			GridPane gridPane = new GridPane();
			
			ColumnConstraints colConst = new ColumnConstraints();
			colConst.setPercentWidth( 50.0 );
			gridPane.getColumnConstraints().addAll( colConst, colConst );
			
			for ( int j = 0; j < numOfFloors / 2; j++ )
			{
				RowConstraints rowConst = new RowConstraints();
				rowConst.setPercentHeight( 100.0 / (numOfFloors / 2.0) );
				gridPane.getRowConstraints().add( rowConst );
				
				RadioButton button1 = new RadioButton( String.valueOf( j * 2 ) );
				RadioButton button2 = new RadioButton( String.valueOf( j * 2 + 1 ) );
				gridPane.addRow( j, button1, button2 );
			}
			if ( i != numOfFloors - 1 )
			{
				gridPane.setVisible( false );
			}
			
			if ( i != 0 )
			{
				RowConstraints rowConst = new RowConstraints();
				rowConst.setPercentHeight( 100.0 / numOfFloors );
				elevatorPane.getRowConstraints().add( rowConst );
			}
			else
			{
				elevatorPane.getRowConstraints().get( 0 ).setPercentHeight( 100.0 / numOfFloors );
			}
			elevatorPane.add( gridPane, 0, i );
			result.add( gridPane );
		}
		
		return result;
	}
	
	
	private void SetBuildingPane( int numOfFloors )
	{
		for ( int i = 0; i < numOfFloors; i++ )
		{
			if ( i == 0 )
			{
				buildingPane.getRowConstraints().get( 0 ).setPercentHeight( 100.0 / numOfFloors );
			}
			else
			{
				RowConstraints rowConst = new RowConstraints();
				rowConst.setPercentHeight( 100.0 / numOfFloors );
				buildingPane.getRowConstraints().add( rowConst );
			}
			
			GridPane requestsPane = CreateRequestsPane();
			buildingPane.addRow( i, requestsPane );
		}
	}


	private GridPane CreateRequestsPane()
	{
		GridPane gridPane = new GridPane();
		
		ColumnConstraints colConst = new ColumnConstraints();
		colConst.setPercentWidth( 100.0 );
		gridPane.getColumnConstraints().add( colConst );

		RowConstraints rowConst = new RowConstraints();
		rowConst.setPercentHeight( 100.0 );
		gridPane.getRowConstraints().add( rowConst );
		
		RadioButton button = new RadioButton( "0" );
		gridPane.add( button, 0, 0 );
		floorButtons.add( button );
		
		return gridPane;
	}
}
