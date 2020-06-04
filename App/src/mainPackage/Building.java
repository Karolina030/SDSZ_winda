package mainPackage;

import java.util.*;
import javafx.scene.control.RadioButton;

public class Building
{
	// height between levels in meters
	public static double FloorHeight = 2.8;
	public static int numOfFloors = 8;

	public PriorityQueue<ElevatorRequest> ElevatorRequests = new PriorityQueue<ElevatorRequest>();
	public int RequestsLeft = 0;

	private ArrayList<Elevator> elevators;
	private RoundRobin roundRobin;
	private int numOfResults = 0;
	private long totalWaitingTime = 0;
	private long totalElapsedTime;
	private ArrayList<Floor> floors;


	public Building( int numOfFloors, ArrayList<Elevator> elevators, ArrayList<RadioButton> buttons )
	{
		this.roundRobin = new RoundRobin();
		this.elevators = elevators;
		Building.numOfFloors = numOfFloors;

		floors = new ArrayList<Floor>( numOfFloors );
		for( int i = 0; i < numOfFloors; i++ )
		{
			floors.add( new Floor( i, buttons.get( numOfFloors - i - 1 ) ) );
		}
	}

	public void GenerateEvent( int people, int floor, int appear, int period )
	{
		Random rand = new Random();

		int startFloor;
		if ( floor == -1 )
		{
			startFloor = rand.nextInt( Building.numOfFloors );
		}
		else
		{
			startFloor = floor;
		}
		
		for( int i = 0; i < people; i++ )
		{
			int endFloor;
			do
			{
				endFloor = rand.nextInt( Building.numOfFloors );
			} while ( endFloor == startFloor );
			int appearTime = rand.nextInt( period ) + appear;

			ElevatorRequests.add( new ElevatorRequest( startFloor, endFloor, appearTime ) );
		}
		
		RequestsLeft += people;
	}

	// przypisuje kazdemu pasazerowi losowe zadanie i czas pojawienia
	public void GeneratePeopleQueue( int simulationTime, int people )
	{
		Random rand = new Random();

		for( int i = 0; i < people; i++ )
		{
			int startFloor = rand.nextInt( numOfFloors );
			int endFloor;
			
			do
			{
				endFloor = rand.nextInt( numOfFloors );
			} while ( endFloor == startFloor );

			int appearTime = rand.nextInt( simulationTime );

			// sortuje sie automatycznie za pomoca CompareTo
			ElevatorRequests.add( new ElevatorRequest(startFloor, endFloor, appearTime) );
		}
		
		RequestsLeft += people;
	}

	public void Simulate( long elapsedTime, long totalElapsedTime )
	{
		this.totalElapsedTime = totalElapsedTime;
		ElevatorRequest nextRequest = ElevatorRequests.peek();

		if ( nextRequest != null
				&& nextRequest.appearTime <= totalElapsedTime
				&& ( totalElapsedTime - nextRequest.appearTime ) <= 20 * 1000 )
		{
			// pobieramy zadanie (i usuwamy)
			nextRequest = ElevatorRequests.poll();
			ElevatorRequests.remove(nextRequest);
			// wybieramy numer windy
			int chosenElevator = roundRobin.chooseElevator(elevators);
			// dodajemy to zadanie do zadan zewnetrznych windy
			elevators.get(chosenElevator).AddOutsideRequest( nextRequest );

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
		Results results = new Results( numOfResults, totalWaitingTime / numOfResults / 1000.0 );
		return results;
	}
}
