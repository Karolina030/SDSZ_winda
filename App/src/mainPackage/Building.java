package mainPackage;

import java.util.*;

public class Building
{
	public Elevator elevator;
	static int numOfFloors;
	private int numOfPeople;
	private int numOfResults = 0;
	private long totalWaitingTime = 0;
	private long totalElapsedTime;
	public long start;

	public PriorityQueue<ElevatorRequest> elevatorRequests;
	private ArrayList<Floor> floors;

	public Building( Elevator elevator, int numOfFloors, int numOfPeople )
	{
		this.elevator = elevator;

		elevator.AddBuilding( this );

		this.numOfFloors = numOfFloors;
		this.numOfPeople = numOfPeople;
		start = System.currentTimeMillis();

		elevatorRequests = new PriorityQueue<ElevatorRequest>();

		floors = new ArrayList<Floor>( numOfFloors );
		for( int i = 0; i < numOfFloors; i++ )
		{
			floors.add( new Floor(i) );
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

	public void GeneratePeopleQueue( int simulationTime ) //przypisanie kaÅ¼demu pasaÅ¼erowi randomowych zapytaÅ„ i czasu ich pojawienia siÄ™
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

			elevatorRequests.add( new ElevatorRequest(startFloor, endFloor, appearTime) ); // sortuje siê automatycznie za pomoc¹ CompareTo w ElevatorRequest.clas
		}
		GenerateEvent(simulationTime,10);
	}

	public void Simulate( long elapsedTime, long totalElapsedTime )
	{
		this.totalElapsedTime = totalElapsedTime;
		ElevatorRequest nextRequest = elevatorRequests.peek(); //pobieramy pierwsze zapytanie
		if ( nextRequest != null && nextRequest.appearTime <= totalElapsedTime && (totalElapsedTime-nextRequest.appearTime)<=20*1000) //warunki Å¼eby to byÅ‚o dobre zapytanie
		{
			nextRequest = elevatorRequests.poll();   //pobranie z usuniÄ™ciem jednego zapytania
			elevator.AddOutsideRequest( nextRequest );  //dodanie tego zapytania do listy zapytaÅ„ zewnÄ™trznych w windzie
			floors.get( nextRequest.getStartFloor() ).addRequest(nextRequest); //dodanie tego zapytania do listy na odpowiednim piÄ™trze
		}
		//for( Elevator elevator : elevators )
		//{
		elevator.Simulate( elapsedTime );
		//}
	}

	public ElevatorRequest GetFloorRequest( int floor )
	{
		return floors.get( floor ).getFloorRequests().poll();
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
