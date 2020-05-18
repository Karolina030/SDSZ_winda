package mainPackage;

import java.util.*;

public class Building
{
	private Elevator[] elevators;
	static int numOfFloors;
	private int numOfPeople;
	
	private PriorityQueue<ElevatorRequest> elevatorRequests;
	private ArrayList<LinkedList<ElevatorRequest>> floors;
	
	public Building( Elevator[] elevators, int numOfFloors, int numOfPeople )
	{
		this.elevators = elevators;
		
		for( Elevator elevator : elevators )
		{
			elevator.AddBuilding( this );
		}
		
		this.numOfFloors = numOfFloors;
		this.numOfPeople = numOfPeople;
		
		elevatorRequests = new PriorityQueue<ElevatorRequest>();
		
		floors = new ArrayList<LinkedList<ElevatorRequest>>( numOfFloors );
		for( int i = 0; i < numOfFloors; i++ )
		{
			floors.add( new LinkedList<ElevatorRequest>() );
		}
	}

	
	public void GeneratePeopleQueue( int simulationTime ) //przypisanie każdemu pasażerowi randomowych zapytań i czasu ich pojawienia się
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
			
			elevatorRequests.add( new ElevatorRequest(startFloor, endFloor, appearTime) ); // sortuje si� automatycznie za pomoc� CompareTo w ElevatorRequest.clas
		}
	}

	public void Simulate( long elapsedTime, long totalElapsedTime )
	{
		ElevatorRequest nextRequest = elevatorRequests.peek(); //pobieramy pierwsze zapytanie
		if ( nextRequest != null && nextRequest.appearTime <= totalElapsedTime ) //warunki żeby to było dobre zapytanie
		{
			nextRequest = elevatorRequests.poll();   //pobranie z usunięciem jednego zapytania
			elevators[ 0 ].AddOutsideRequest( nextRequest );  //dodanie tego zapytania do listy zapytań zewnętrznych w windzie
			floors.get( nextRequest.startFloor ).add( nextRequest );  //dodanie tego zapytania do listy na odpowiednim piętrze
		}
		
		for( Elevator elevator : elevators )
		{
			elevator.Simulate( elapsedTime );
		}
	}
	
	public ElevatorRequest GetFloorRequest( int floor )
	{
		return floors.get( floor ).poll();
	}
}
