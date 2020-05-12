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
	private LinkedList<Integer> outsideRequests;
	private LinkedList<Integer> insideRequests;
	private boolean isMoving = false;
	
	
	public Elevator( double velocity, int capacity )
	{
		this.velocity = velocity;
		Capacity = capacity;
		outsideRequests = new LinkedList<Integer>();
		insideRequests = new LinkedList<Integer>();
		insideRequests.add(3);
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
			return outsideRequests.removeFirst();
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
			currentHeight += velocity * ((double)elapsedTime / 1000);
			System.out.println(currentHeight);
			if ( currentHeight >= floorToGo * floorHeight )
			{
				currentHeight = floorToGo * floorHeight;
				currentFloor = floorToGo;
				isMoving = false;
				System.out.println("Achieved level!");
			}	
		}
		else if ( floorToGo < currentFloor )
		{
			currentHeight -= velocity * ((double)elapsedTime / 1000);
			
			if ( currentHeight <= floorToGo * floorHeight )
			{
				currentHeight = floorToGo * floorHeight;
				currentFloor = floorToGo;
				isMoving = false;
			}	
		}
	}
}
