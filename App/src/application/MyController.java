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
	Building building;

	private ArrayList<RadioButton> floorButtons = new ArrayList<RadioButton>();
	private ArrayList<ArrayList<GridPane>> elPanes = new ArrayList<ArrayList<GridPane>>(2);
	
	
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


		ArrayList<GridPane> a1 = new ArrayList<GridPane>();
		ArrayList<GridPane> a2 = new ArrayList<GridPane>();
		elPanes.add(a1);
		elPanes.add(a2);

		insideRequestsStartFloor.setCellValueFactory( new PropertyValueFactory<>( "startFloor" ) );
		insideRequestsEndFloor.setCellValueFactory( new PropertyValueFactory<>( "endFloor" ) );
		outsideRequestsStartFloor.setCellValueFactory( new PropertyValueFactory<>( "startFloor" ) );
		outsideRequestsEndFloor.setCellValueFactory( new PropertyValueFactory<>( "endFloor" ) );

		SetBuildingPane( numOfFloors );


		Building building = new Building(numOfFloors, numOfPeople, floorButtons, elPanes);
		Simulation simulation = new Simulation( building, simulationTime, simulationSpeed );

		simulation.start();

		//simulation.stop();

		insideRequestsTable.setItems( building.InsideRequestsG );
		outsideRequestsTable.setItems( building.OutsideRequestsG );
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

		GridPane elevatorPane1 = CreateElevatorPane( numOfFloors, 0 );
		GridPane elevatorPane2 = CreateElevatorPane( numOfFloors, 1 );

		GridPane requestsPane = CreateRequestsPane();
		
		if ( floor != numOfFloors - 1 ) elevatorPane1.setVisible( false );
		if ( floor != numOfFloors - 1 ) elevatorPane2.setVisible( false );
				
		buildingPane.addRow( floor, elevatorPane1, elevatorPane2, requestsPane );
	}



	private GridPane CreateElevatorPane( int numOfFloors, int elevatorNumber )
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
		elPanes.get(elevatorNumber).add(gridPane);

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
