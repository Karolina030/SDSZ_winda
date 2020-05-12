package mainPackage;

import java.util.*;

public class Elevator
{
	// height between levels in meters
	public static double floorHeight = 2.8;
	
	// max num of people
	public int Capacity = 8;
	private int peopleInside = 0;
	
	private Building building;
	// in meters per second
	private double velocity = 0.63;
	private int currentFloor = 0;
	private double currentHeight = 0;
	private int floorToGo = 0;
	private LinkedList<ElevatorRequest> outsideRequests;
	private LinkedList<ElevatorRequest> insideRequests;
	private boolean isMoving = false;
	
	
	public Elevator( double velocity, int capacity )
	{
		this.velocity = velocity;
		Capacity = capacity;
		outsideRequests = new LinkedList<ElevatorRequest>();
		insideRequests = new LinkedList<ElevatorRequest>();
	}
	
	
	public void AddBuilding( Building building )
	{
		this.building = building;
	}


	public void Simulate( long elapsedTime )
	{
		// Je¿eli siê nie poruszamy to wybieramy poziom na który pojedziemy
		if ( !isMoving )
		{
			floorToGo = ChooseLevelToGo();
			if ( floorToGo >= 0 )
			{
				isMoving = true;
				System.out.println("Moving to " + floorToGo);
			}
		}
		// Mamy wybrany poziom i mo¿emy jechaæ
		else
		{
			Move( elapsedTime );
		}
	}
	
	
	public void AddOutsideRequest( ElevatorRequest request )
	{
		outsideRequests.add( request );
		System.out.println( "Outside request added!" );
	}
	
	
	// Je¿eli mamy ¿¹danie ze œrodka windy to tam jedziemy
	// Je¿eli nie to jedziemy na najstarsze ¿¹danie z zewn¹trz
	// W przeciwnym wypadku zwracamy -1 - brak wyboru
	private int ChooseLevelToGo()
	{
		if ( !insideRequests.isEmpty() )
		{
			return insideRequests.getFirst().endFloor;
		}
		else if ( !outsideRequests.isEmpty() )
		{
			return outsideRequests.getFirst().startFloor;
		}
		else
		{
			return -1;
		}
	}
	
	
	private void Move( long elapsedTime )
	{		
		if ( floorToGo > currentFloor )
		{
			MoveUp( elapsedTime );
		}
		else if ( floorToGo < currentFloor )
		{
			MoveDown( elapsedTime );
		}
		else
		{
			FloorAchieved();
		}
		
		// System.out.println(currentHeight);
	}
	
	
	private void MoveUp( long elapsedTime )
	{
		currentHeight += velocity * ((double)elapsedTime / 1000);
		
		if ( currentHeight >= floorToGo * floorHeight )
		{
			FloorAchieved();
		}	

	}
	
	
	private void MoveDown( long elapsedTime )
	{
		currentHeight -= velocity * ((double)elapsedTime / 1000);
		
		if ( currentHeight <= floorToGo * floorHeight )
		{
			FloorAchieved();
		}	
	}
	
	
	private void FloorAchieved()
	{
		currentHeight = floorToGo * floorHeight;
		currentFloor = floorToGo;
		isMoving = false;
		
		// wysiadaj¹cy
		for( int i = 0; i < insideRequests.size(); i++ )
		{
			ElevatorRequest request = insideRequests.get( i );
			if ( request.endFloor == currentFloor )
			{
				request.EndRequest();
				insideRequests.remove( i );
				i--;
				peopleInside--;
			}
		}
		
		// wsiadaj¹cy
		while( peopleInside < Capacity )
		{
			ElevatorRequest request = building.GetFloorRequest( currentFloor );
			outsideRequests.remove( request );
			
			if ( request == null )
			{
				return;
			}
			
			insideRequests.add( request );
			peopleInside++;
			System.out.println( "Added one person!" );
		}
		
		System.out.println("Floor " + currentFloor + " achieved!");
	}
}
