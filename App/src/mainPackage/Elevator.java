package mainPackage;

import java.util.*;

public class Elevator
{
	// height between levels in meters
	public static double floorHeight = 2.8;
	
	// max num of people
	public int Capacity = 8;

	// in meters per second
	private double velocity = 0.63;
	private int currentFloor = 0;
	private double currentHeight = 0;
	private int floorToGo = 0;
	private LinkedList<ElevatorRequest> outsideRequests;
	private LinkedList<Integer> insideRequests;
	private boolean isMoving = false;
	
	
	public Elevator( double velocity, int capacity )
	{
		this.velocity = velocity;
		Capacity = capacity;
		outsideRequests = new LinkedList<ElevatorRequest>();
		insideRequests = new LinkedList<Integer>();
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
	}
	
	
	// Je¿eli mamy ¿¹danie ze œrodka windy to tam jedziemy
	// Je¿eli nie to jedziemy na najstarsze ¿¹danie z zewn¹trz
	// W przeciwnym wypadku zwracamy -1 - brak wyboru
	private int ChooseLevelToGo()
	{
		if ( !insideRequests.isEmpty() )
		{
			return insideRequests.removeFirst();
		}
		else if ( !outsideRequests.isEmpty() )
		{
			return outsideRequests.removeFirst().startFloor;
		}
		else
		{
			return -1;
		}
	}
	
	
	private void Move( long elapsedTime )
	{
		System.out.println("Moving!");
		
		if ( floorToGo > currentFloor )
		{
			MoveUp( elapsedTime );
		}
		else if ( floorToGo < currentFloor )
		{
			MoveDown( elapsedTime );
		}
		
		System.out.println(currentHeight);
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
		
		// TODO: sprawdziæ kto wsiada a kto wysiada
		
		System.out.println("Floor achieved!");
	}
}
