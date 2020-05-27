package mainPackage;

import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;

public class Building
{

	public ArrayList<Elevator> elevatorGroup;// tablica L wind
	public int L=2; // ilość wind
	private RoundRobin roundRobin;
	double elevatorVelocity = 0.63;
	int elevatorCapacity = 8;
	public ObservableList<ElevatorRequest> OutsideRequestsG;
	public ObservableList<ElevatorRequest> InsideRequestsG;
	public long elapsedTime;
	public int counter;

	static int numOfFloors=8;
	private int numOfPeople;
	private int numOfResults = 0;
	private long totalWaitingTime = 0;
	private long totalElapsedTime;
	public long start;

	public PriorityQueue<ElevatorRequest> elevatorRequests;
	private ArrayList<Floor> floors;

	public Building( int numOfFloors, int numOfPeople, ArrayList<RadioButton> buttons, ArrayList<ArrayList<GridPane>> elPanes )
	{
		this.roundRobin = new RoundRobin();

		elevatorGroup = new ArrayList<Elevator>(L);
		for( int i = 0; i < L; i++ )
		{
			ArrayList<GridPane> elevatorPanes = elPanes.get(i);
			Label heightLabel = new Label();
			Elevator elevator = new Elevator(elevatorVelocity, elevatorCapacity, heightLabel, elevatorPanes );
			elevatorGroup.add( elevator);
			elevatorGroup.get(i).SetBuilding(this);
		}

		OutsideRequestsG = FXCollections.observableArrayList();
		InsideRequestsG = FXCollections.observableArrayList();

		Building.numOfFloors = numOfFloors;
		this.numOfPeople = numOfPeople;
		start = System.currentTimeMillis();

		elevatorRequests = new PriorityQueue<ElevatorRequest>();

		floors = new ArrayList<Floor>( numOfFloors );
		for( int i = 0; i < numOfFloors; i++ )
		{
			floors.add( new Floor( i, buttons.get( numOfFloors - i - 1 ) ) );
		}
	}

	public void GenerateEvent( int simulationTime, int people)
	{
		Random rand = new Random();

		int startFloor = rand.nextInt( Building.numOfFloors );
		int time = rand.nextInt(simulationTime);// start
		for( int i = 0; i < people; i++ ) {
			int endFloor;
			do
			{
				endFloor = rand.nextInt( Building.numOfFloors );
			} while ( endFloor == startFloor );
			int appearTime = rand.nextInt( 20000) + time;

			elevatorRequests.add( new ElevatorRequest(startFloor, endFloor, appearTime) );

		}

	}

	// przypisuje kazdemu pasazerowi losowe zadanie i czas pojawienia
	public void GeneratePeopleQueue( int simulationTime )
	{
		Random rand = new Random();

		for( int i = 0; i < numOfPeople; i++ )
		{
			int startFloor = rand.nextInt( numOfFloors );
			int endFloor;
			
			do
			{
				endFloor = rand.nextInt( numOfFloors );
			} while ( endFloor == startFloor );

			int appearTime = rand.nextInt( simulationTime );

			// sortuje sie automatycznie za pomoca CompareTo
			elevatorRequests.add( new ElevatorRequest(startFloor, endFloor, appearTime) );
		}
		GenerateEvent(simulationTime,10);
	}

	public void Simulate( long elapsedTime, long totalElapsedTime )
	{
		this.totalElapsedTime = totalElapsedTime;
		ElevatorRequest nextRequest = elevatorRequests.peek();

		int chosenElevator = counter;

		if ( nextRequest != null
				&& nextRequest.appearTime <= totalElapsedTime
				&& ( totalElapsedTime - nextRequest.appearTime ) <= 20 * 1000 )
		{
			// pobieramy zadanie (i usuwamy)
			nextRequest = elevatorRequests.poll();
			elevatorRequests.remove(nextRequest);
			// wybieramy numer windy
			chosenElevator = roundRobin.choseElevator(elevatorGroup, L);
			// dodajemy to zadanie do zadan zewnetrznych windy
			elevatorGroup.get(chosenElevator).AddOutsideRequest( nextRequest );
			OutsideRequestsG.add(nextRequest);

			//dodajemy to zadanie do listy na odpowiednim pietrze
			floors.get( nextRequest.getStartFloor() ).addRequest( nextRequest );
		}

		for( Elevator elevator : elevatorGroup )
		{
			elevator.Simulate( elapsedTime );
		}


	}

	public ElevatorRequest GetFloorRequest( int floor )
	{
		return floors.get( floor ).getFloorRequest();
	}

	public void AddResult( ElevatorRequest request )
	{
		numOfResults++;
		totalWaitingTime += totalElapsedTime - request.appearTime;
	}

	public double GetResults()
	{
		double result = totalWaitingTime / numOfResults / 1000;
		return result;
	}
}
