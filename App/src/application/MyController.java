package application;

import mainPackage.*;
import java.util.ArrayList;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;

public class MyController
{
	Stage stage;
	@FXML private TableView<ElevatorRequest> insideRequestsTable;
	@FXML private TableView<ElevatorRequest> outsideRequestsTable;
	@FXML private TableColumn<ElevatorRequest, Integer> insideRequestsStartFloor;
	@FXML private TableColumn<ElevatorRequest, Integer> insideRequestsEndFloor;
	@FXML private TableColumn<ElevatorRequest, Integer> outsideRequestsStartFloor;
	@FXML private TableColumn<ElevatorRequest, Integer> outsideRequestsEndFloor;
	@FXML private Label heightLabel;
	@FXML private Pane heightLabelPane;
	@FXML private GridPane buildingPane;
	
	private ArrayList<GridPane> elevatorPanes = new ArrayList<GridPane>(); 
	private ArrayList<RadioButton> floorButtons = new ArrayList<RadioButton>(); 
	
	
	public void SetStage(Stage s)
	{
		this.stage = s;
	}
	
	
	public void StartSimulation()
	{
		double elevatorVelocity = 0.63;
		int elevatorCapacity = 8;
		int numOfFloors = 8;
		int numOfPeople = 20;
		int simulationTime = 100;
		int simulationSpeed = 1;

		SetBuildingPane( numOfFloors );
		
		insideRequestsStartFloor.setCellValueFactory( new PropertyValueFactory<>( "startFloor" ) );
		insideRequestsEndFloor.setCellValueFactory( new PropertyValueFactory<>( "endFloor" ) );
		outsideRequestsStartFloor.setCellValueFactory( new PropertyValueFactory<>( "startFloor" ) );
		outsideRequestsEndFloor.setCellValueFactory( new PropertyValueFactory<>( "endFloor" ) );
		
		Elevator elevator = new Elevator( elevatorVelocity, elevatorCapacity, heightLabel, elevatorPanes );
		Building building = new Building( elevator, numOfFloors, numOfPeople, floorButtons );
		Simulation simulation = new Simulation( building, simulationTime, simulationSpeed );
		
		simulation.start();
		//simulation.stop();
		
		insideRequestsTable.setItems( elevator.InsideRequests );
		outsideRequestsTable.setItems( building.elevator.OutsideRequests );
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
			
			SetFloorPane( numOfFloors, i );
		}
	}
	
	private void SetFloorPane( int numOfFloors, int floor )
	{
		GridPane elevatorPane = CreateElevatorPane( numOfFloors );
		GridPane requestsPane = CreateRequestsPane();
		
		if ( floor != numOfFloors - 1 ) elevatorPane.setVisible( false );
				
		buildingPane.addRow( floor, elevatorPane, requestsPane );
	}
	
	private GridPane CreateElevatorPane( int numOfFloors )
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
		
		elevatorPanes.add( gridPane );
		
		return gridPane;
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
