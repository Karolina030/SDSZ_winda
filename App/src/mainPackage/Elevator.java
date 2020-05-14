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
	private String direction = "up";
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
		// Je�eli si� nie poruszamy to wybieramy poziom na kt�ry pojedziemy
		if ( !isMoving )
		{


			floorToGo = ChooseLevelToGo();
			if ( floorToGo >= 0 )
			{
				isMoving = true;
				System.out.println("----------------------------------");
				System.out.println("Moving to " + floorToGo);
			}

		}
		// Mamy wybrany poziom i mo�emy jecha�
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
	
	
	// Je�eli mamy ��danie ze �rodka windy to tam jedziemy
	// Je�eli nie to jedziemy na najstarsze ��danie z zewn�trz
	// W przeciwnym wypadku zwracamy -1 - brak wyboru
	private int ChooseLevelToGo()
	{
		if ( !insideRequests.isEmpty() )
		{
			return insideRequests.getFirst().endFloor;
		}
		else if ( !outsideRequests.isEmpty() )
		{
			return outsideRequests.getLast().startFloor;
		}
		else
		{
			return -1;
		}
	}

	public void adjustDirection() {

		if (currentFloor == Building.numOfFloors-1) {
			direction = "down";
		}
		else if (currentFloor == 0) {
			direction = "up";
		}
	}
	
	private void Move( long elapsedTime )
	{
		adjustDirection();
		if (direction.equals("up")) {
			MoveUp( elapsedTime );
		}
		if (direction.equals("down")) {
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
		currentFloor = (int) (currentHeight/floorHeight);
		while (currentFloor < Building.numOfFloors-1) {
			currentHeight += velocity * ((double)elapsedTime / 1000);
			currentFloor = (int) (currentHeight/floorHeight);
			FloorAchieved();
		}
	}
	
	
	private void MoveDown( long elapsedTime )
	{
		currentFloor = (int) (currentHeight/floorHeight);
		while (currentFloor > 0) {
			currentHeight -= velocity * ((double)elapsedTime / 1000);
			currentFloor = (int) (currentHeight/floorHeight);
			FloorAchieved();
		}
	}
	
	
	private void FloorAchieved()
	{
		//currentHeight = floorToGo * floorHeight;
		//currentFloor = floorToGo;
		isMoving = false;
		// wysiadaj�cy
		for( int i = 0; i < insideRequests.size(); i++ )
		{
			ElevatorRequest request = insideRequests.get( i );
			if ( request.endFloor == currentFloor )
			{
				request.EndRequest();
				insideRequests.remove(i);
				i--;
				peopleInside--;
				System.out.println("Floor " + currentFloor+ " achieved!");
				System.out.println( "Removed one person!" );
				System.out.println("Number of people in the elevator " + peopleInside);

			}
		}
		
		// wsiadaj�cy
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
			System.out.println("Floor " + currentFloor + " achieved!");
			System.out.println( "Added one person!" );
			System.out.println("Number of people in the elevator " + peopleInside);
		}

	}
}
