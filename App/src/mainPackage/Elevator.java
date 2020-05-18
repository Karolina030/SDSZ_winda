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
	private Direction direction = Direction.none;
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
		// Jezeli sie nie poruszamy to wybieramy poziom na ktory pojedziemy
		if ( !isMoving )
		{
			floorToGo = ChooseLevelToGo();
			if ( floorToGo >= 0 )
			{
				AdjustDirection();
				isMoving = true;
				System.out.println("----------------------------------");
				System.out.println("Moving to " + floorToGo);
			}
		}
		// Poruszamy sie
		else
		{
			Move( elapsedTime );
		}
	}
	
	
	public void AddOutsideRequest( ElevatorRequest request )
	{
		outsideRequests.add( request );
		System.out.println( "Outside request added at: "+ request.startFloor + " floor!");
	}
	

	private int ChooseLevelToGo()
	{
		// jezeli ktos jest w srodku windy
		if ( !insideRequests.isEmpty() )
		{
			// jezeli winda jechala w gore to staramy sie jechac w gore
			// "staramy sie" bo moze sie okazac, ze wyzej juz nikogo nie ma
			// wtedy zostanie zwrocony poziom nizej
			if ( direction == Direction.up)
			{
				return GetClosestRequestUp();
			}
			// winda jechala w dol wiec staramy sie jechac w dol
			else
			{
				return GetClosestRequestDown();
			}
		}
		else if ( !outsideRequests.isEmpty() )
		{
			// sprawdzamy czy najstarsze zadanie jest nizej
			if ( outsideRequests.getFirst().startFloor < currentFloor )
			{
				// zwracamy najblizsze zadanie w dol
				return GetClosestOutsideRequestDown().startFloor;
			}
			else
			{
				// zwracamy najblizsze zadanie w gore
				return GetClosestOutsideRequestUp().startFloor;
			}
		}
		else
		{
			return -1;
		}
	}
	

	private void AdjustDirection()
	{
		if ( floorToGo < currentFloor )
		{
			direction = Direction.down;
		}
		else if ( floorToGo > currentFloor )
		{
			direction = Direction.up;
		}
		else
		{
			direction = Direction.none;
		}
	}
	
	
	private void Move( long elapsedTime )
	{
		currentFloor = (int) ( currentHeight / floorHeight );
		
		if ( direction == Direction.up )
		{
			MoveUp( elapsedTime );
		}
		else if ( direction == Direction.down )
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
		if ( currentFloor < floorToGo )
		{
			currentHeight += velocity * ((double)elapsedTime / 1000);
		}
		else
		{
			FloorAchieved();
		}
	}
	
	
	private void MoveDown( long elapsedTime )
	{
		if ( currentFloor > floorToGo )
		{
			currentHeight -= velocity * ((double)elapsedTime / 1000);
		}
		else 
		{
			FloorAchieved();
		}
	}
	
	
	private void FloorAchieved()
	{
		isMoving = false;
		
		System.out.println("Floor " + currentFloor + " achieved!");
		
		// wysiadajacy
		for( int i = 0; i < insideRequests.size(); i++ )
		{
			ElevatorRequest request = insideRequests.get( i );
			if ( request.endFloor == currentFloor )
			{
				insideRequests.remove(i);
				i--;
				peopleInside--;
				System.out.println( "Removed one person!" );
			}
		}
		
		// wsiadajacy
		while( peopleInside < Capacity )
		{
			ElevatorRequest request = building.GetFloorRequest( currentFloor );
			
			if ( request == null )
			{
				break;
			}
			
			building.AddResult( request );
			outsideRequests.remove( request );
			insideRequests.add( request );
			peopleInside++;
			System.out.println( "Added one person!" );
		}
		
		System.out.println("Number of people in the elevator " + peopleInside);
	}

	
	private int GetClosestRequestUp()
	{
		ElevatorRequest inside = GetClosestInsideRequestUp();
		ElevatorRequest outside = GetClosestOutsideRequestUp();
		
		if ( inside == null && outside == null )
		{
			return GetClosestRequestDown();
		}
		
		if ( inside == null )
		{
			return outside.startFloor;
		}
		else if ( outside == null )
		{
			return inside.endFloor;
		}
		
		if ( outside.startFloor < inside.endFloor )
		{
			return outside.startFloor;
		}
		else
		{
			return inside.endFloor;
		}
	}
	
	
	private int GetClosestRequestDown()
	{
		ElevatorRequest inside = GetClosestInsideRequestDown();
		ElevatorRequest outside = GetClosestOutsideRequestDown();
		
		if ( inside == null && outside == null )
		{
			return GetClosestRequestUp();
		}
		
		if ( inside == null )
		{
			return outside.startFloor;
		}
		else if ( outside == null )
		{
			return inside.endFloor;
		}
		
		if ( outside.startFloor > inside.endFloor )
		{
			return outside.startFloor;
		}
		else
		{
			return inside.endFloor;
		}
	}
	

	private ElevatorRequest GetClosestInsideRequestUp()
	{
		ElevatorRequest result = null;
		
		for( int i = 0; i < insideRequests.size(); i++ )
		{
			ElevatorRequest request = insideRequests.get( i );
			if ( request.endFloor > currentFloor )
			{
				if ( result == null || request.endFloor < result.endFloor )
				{
					result = request;
				}
			}
		}
		
		return result;
	}
	
	
	private ElevatorRequest GetClosestInsideRequestDown()
	{
		ElevatorRequest result = null;
		
		for( int i = 0; i < insideRequests.size(); i++ )
		{
			ElevatorRequest request = insideRequests.get( i );
			if ( request.endFloor < currentFloor )
			{
				if ( result == null || request.endFloor > result.endFloor )
				{
					result = request;
				}
			}
		}
		
		return result;
	}


	private ElevatorRequest GetClosestOutsideRequestUp()
	{
		ElevatorRequest result = null;
		
		for( int i = 0; i < outsideRequests.size(); i++ )
		{
			ElevatorRequest request = outsideRequests.get( i );
			if ( request.startFloor > currentFloor )
			{
				if ( result == null || request.startFloor < result.startFloor )
				{
					result = request;
				}
			}
		}
		
		return result;
	}


	private ElevatorRequest GetClosestOutsideRequestDown()
	{
		ElevatorRequest result = null;
		
		for( int i = 0; i < outsideRequests.size(); i++ )
		{
			ElevatorRequest request = outsideRequests.get( i );
			if ( request.startFloor < currentFloor )
			{
				if ( result == null || request.startFloor > result.startFloor )
				{
					result = request;
				}
			}
		}
		
		return result;
	}
}
