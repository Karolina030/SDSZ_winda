package mainPackage;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

public class Elevator
{
	// height between levels in meters
	public static double floorHeight = 2.8;
	
	// max num of people
	public int Capacity = 8;
	public int PeopleInside = 0;
	public ObservableList<ElevatorRequest> OutsideRequests;
	public ObservableList<ElevatorRequest> InsideRequests;
	
	private Building building;
	private double velocity = 0.63; // in meters per second
	private int currentFloor = 0;
	private Direction direction = Direction.none;
	private final DoubleProperty currentHeight = new SimpleDoubleProperty( 0.0 );
	private int floorToGo = 0;
	private boolean isMoving = false;
	private Label heightLabel;


	public Elevator( double velocity, int capacity, Label heightLabel )
	{
		this.velocity = velocity;
		Capacity = capacity;
		OutsideRequests = FXCollections.observableArrayList();
		InsideRequests = FXCollections.observableArrayList();
		this.heightLabel = heightLabel;
	}
	
	
	public double getCurrentHeight()
	{
		return currentHeight.get();
	}
	
	
	public void setCurrentHeight( double value )
	{
		Platform.runLater( new Runnable()
				{
					public void run()
					{
						currentHeight.set( value );
						heightLabel.setText( String.format( "%.2f", currentHeight.get() ));
						heightLabel.setTranslateY( -heightLabel.getLayoutY() / (Building.numOfFloors * floorHeight) * currentHeight.get());
					}
				});
	}
	
	
	public DoubleProperty currentHeightProperty()
	{
		return currentHeight;
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
		OutsideRequests.add( request );
	}
	

	private int ChooseLevelToGo()
	{
		// jezeli ktos jest w srodku windy
		if ( !InsideRequests.isEmpty() )
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
		else if ( !OutsideRequests.isEmpty() )
		{
			// sprawdzamy czy najstarsze zadanie jest nizej
			if ( OutsideRequests.get( 0 ).getStartFloor() < currentFloor )
			{
				// zwracamy najblizsze zadanie w dol
				return GetClosestOutsideRequestDown().getStartFloor();
			}
			else
			{
				// zwracamy najblizsze zadanie w gore
				return GetClosestOutsideRequestUp().getStartFloor();
			}
		}
		else
		{
			return -1;
		}
	}
	

	private void AdjustDirection()
	{
		if ( floorToGo < currentFloor || currentFloor==7)
		{
			direction = Direction.down;
		}
		else if ( floorToGo > currentFloor || currentFloor==0)
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
	}
	
	
	private void MoveUp( long elapsedTime )
	{
		currentFloor = (int) Math.floor( currentHeight.doubleValue() / floorHeight );

		if ( currentFloor < floorToGo )
		{
			setCurrentHeight( currentHeight.get() + velocity * ((double)elapsedTime / 1000) );
		}
		else
		{
			FloorAchieved();
		}
	}
	
	
	private void MoveDown( long elapsedTime )
	{
		currentFloor = (int) Math.ceil( currentHeight.doubleValue() / floorHeight );

		if ( currentFloor > floorToGo )
		{
			setCurrentHeight( currentHeight.get() - velocity * ((double)elapsedTime / 1000) );
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
		for( int i = 0; i < InsideRequests.size(); i++ )
		{
			ElevatorRequest request = InsideRequests.get( i );
			if ( request.getEndFloor() == currentFloor )
			{
				InsideRequests.remove(i);
				i--;
				PeopleInside--;
				System.out.println( "Removed one person!" );
			}
		}
		
		// wsiadajacy
		while( PeopleInside < Capacity )
		{
			ElevatorRequest request = building.GetFloorRequest( currentFloor );
			
			if ( request == null )
			{
				break;
			}
			
			building.AddResult( request );
			OutsideRequests.remove( request );
			InsideRequests.add( request );
			PeopleInside++;
			System.out.println( "Added one person!" );
		}
		
		System.out.println("Number of people in the elevator " + PeopleInside);
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
			return outside.getStartFloor();
		}
		else if ( outside == null )
		{
			return inside.getEndFloor();
		}
		
		if ( outside.getStartFloor() < inside.getEndFloor() )
		{
			return outside.getStartFloor();
		}
		else
		{
			return inside.getEndFloor();
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
			return outside.getStartFloor();
		}
		else if ( outside == null )
		{
			return inside.getEndFloor();
		}
		
		if ( outside.getStartFloor() > inside.getEndFloor() )
		{
			return outside.getStartFloor();
		}
		else
		{
			return inside.getEndFloor();
		}
	}
	

	private ElevatorRequest GetClosestInsideRequestUp()
	{
		ElevatorRequest result = null;
		
		for( int i = 0; i < InsideRequests.size(); i++ )
		{
			ElevatorRequest request = InsideRequests.get( i );
			if ( request.getEndFloor() >= currentFloor )
			{
				if ( result == null || request.getEndFloor() < result.getEndFloor() )
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
		
		for( int i = 0; i < InsideRequests.size(); i++ )
		{
			ElevatorRequest request = InsideRequests.get( i );
			if ( request.getEndFloor() <= currentFloor )
			{
				if ( result == null || request.getEndFloor() > result.getEndFloor() )
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
		
		for( int i = 0; i < OutsideRequests.size(); i++ )
		{
			ElevatorRequest request = OutsideRequests.get( i );
			if ( request.getStartFloor() >= currentFloor )
			{
				if ( result == null || request.getStartFloor() < result.getStartFloor() )
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
		
		for( int i = 0; i < OutsideRequests.size(); i++ )
		{
			ElevatorRequest request = OutsideRequests.get( i );
			if ( request.getStartFloor() <= currentFloor )
			{
				if ( result == null || request.getStartFloor() > result.getStartFloor() )
				{
					result = request;
				}
			}
		}
		
		return result;
	}
}
