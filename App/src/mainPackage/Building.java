package mainPackage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

public class Building
{
	private Elevator[] elevators;
	private int numOfFloors;
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
			
			elevatorRequests.add( new ElevatorRequest( startFloor, endFloor, appearTime ) );
		}
	}
	
	
	public void Simulate( long elapsedTime, long totalElapsedTime )
	{
		ElevatorRequest nextRequest = elevatorRequests.peek();
		if ( nextRequest != null && nextRequest.appearTime <= totalElapsedTime )
		{
			nextRequest = elevatorRequests.poll();
			elevators[ 0 ].AddOutsideRequest( nextRequest );
			floors.get( nextRequest.startFloor ).add( nextRequest );
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
