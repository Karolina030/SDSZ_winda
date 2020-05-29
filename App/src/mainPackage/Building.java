package mainPackage;

import java.util.*;
import javafx.scene.control.RadioButton;

public class Building
{
	// height between levels in meters
	public static double FloorHeight = 2.8;
	public static int numOfFloors = 8;

	private ArrayList<Elevator> elevators;
	private RoundRobin roundRobin;
	public ArrayList<ElevatorRequest> OutsideRequests = new ArrayList<ElevatorRequest>();
	public long elapsedTime;
	public int counter;

	private int numOfPeople;
	private int numOfResults = 0;
	private long totalWaitingTime = 0;
	private long totalElapsedTime;
	public long start;

	public PriorityQueue<ElevatorRequest> elevatorRequests;
	private ArrayList<Floor> floors;

	public Building( int numOfFloors, int numOfPeople, ArrayList<Elevator> elevators, ArrayList<RadioButton> buttons )
	{
		this.roundRobin = new RoundRobin();
		this.elevators = elevators;
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
		int time = rand.nextInt( simulationTime );// start
		for( int i = 0; i < people; i++ )
		{
			int endFloor;
			do
			{
				endFloor = rand.nextInt( Building.numOfFloors );
			} while ( endFloor == startFloor );
			int appearTime = rand.nextInt( 20000 ) + time;

			elevatorRequests.add( new ElevatorRequest( startFloor, endFloor, appearTime ) );
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
			chosenElevator = roundRobin.chooseElevator(elevators);
			// dodajemy to zadanie do zadan zewnetrznych windy
			elevators.get(chosenElevator).AddOutsideRequest( nextRequest );
			OutsideRequests.add(nextRequest);

			//dodajemy to zadanie do listy na odpowiednim pietrze
			floors.get( nextRequest.getStartFloor() ).addRequest( nextRequest );
		}

		for( Elevator elevator : elevators )
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

	public Results GetResults()
	{
		Results results = new Results( numOfResults, totalWaitingTime / numOfResults / 1000 );
		return results;
	}
}
