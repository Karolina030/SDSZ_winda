package mainPackage;

import java.util.PriorityQueue;
import java.util.Random;

public class Building
{
	private Elevator[] elevators;
	private int numOfFloors;
	private int numOfPeople;
	
	private PriorityQueue<ElevatorRequest> elevatorRequests;
	
	public Building( Elevator[] elevators, int numOfFloors, int numOfPeople )
	{
		this.elevators = elevators;
		this.numOfFloors = numOfFloors;
		this.numOfPeople = numOfPeople;
		
		elevatorRequests = new PriorityQueue<ElevatorRequest>();
	}
	
	
	public void GeneratePeopleQueue( int simulationTime )
	{
		Random rand = new Random();
		
		for( int i = 0; i < numOfPeople; i++ )
		{
			int startFloor = rand.nextInt( numOfFloors );
			int endFloor = rand.nextInt( numOfFloors );
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
			elevators[0].AddOutsideRequest( nextRequest );
		}
		
		for( Elevator elevator : elevators )
		{
			elevator.Simulate( elapsedTime );
		}
	}
}
