package application;

import mainPackage.*;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MyController
{
	Stage stage;
	@FXML private TableView<ElevatorRequest> insideRequestsTable;
	@FXML private TableView<ElevatorRequest> outsideRequestsTable;
	@FXML private TableColumn insideRequestsStartFloor;
	@FXML private TableColumn insideRequestsEndFloor;
	@FXML private TableColumn outsideRequestsStartFloor;
	@FXML private TableColumn outsideRequestsEndFloor;
	
	
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
		
		Elevator elevator = new Elevator( elevatorVelocity, elevatorCapacity );
		Building building = new Building( elevator, numOfFloors, numOfPeople );
		Simulation simulation = new Simulation( building, simulationTime, simulationSpeed );
		
		simulation.start();
		//simulation.stop();
		
		insideRequestsTable.setItems( elevator.insideRequests );
		outsideRequestsTable.setItems( building.elevator.outsideRequests );
	}
}
