package application;

import mainPackage.*;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

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
	
	
	public void SetStage(Stage s)
	{
		this.stage = s;
	}
	
	
	public void ButtonPressed()
	{
		insideRequestsStartFloor.setCellValueFactory( new PropertyValueFactory<>( "startFloor" ) );
		insideRequestsEndFloor.setCellValueFactory( new PropertyValueFactory<>( "endFloor" ) );
		outsideRequestsStartFloor.setCellValueFactory( new PropertyValueFactory<>( "startFloor" ) );
		outsideRequestsEndFloor.setCellValueFactory( new PropertyValueFactory<>( "endFloor" ) );

		double elevatorVelocity = 0.63;
		int elevatorCapacity = 8;
		int numOfFloors = 8;
		int numOfPeople = 20;
		int simulationTime = 100;
		int simulationSpeed = 1;
		
		Elevator elevator = new Elevator( elevatorVelocity, elevatorCapacity, heightLabel );
		Building building = new Building( elevator, numOfFloors, numOfPeople );
		Simulation simulation = new Simulation( building, simulationTime, simulationSpeed );
		
		simulation.start();
		//simulation.stop();
		
		insideRequestsTable.setItems( elevator.InsideRequests );
		outsideRequestsTable.setItems( building.elevator.OutsideRequests );
	}
}
