package mainPackage;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;


public class Elevator
{
	// max num of people
	public int Capacity = 8;
	public int PeopleInside = 0;

	private ArrayList<ElevatorRequest> insideRequests = new ArrayList<ElevatorRequest>();
	private ArrayList<ElevatorRequest> outsideRequests = new ArrayList<ElevatorRequest>();
	private Building building;
	private double velocity; // in meters per second
	private int currentFloor = 0;
	private Direction direction = Direction.none;
	private final DoubleProperty currentHeight = new SimpleDoubleProperty( 0.0 );
	private int floorToGo = 0;
	private boolean isMoving = false;
	private boolean isHandlingDoor = false;
	private int handlingDoorTime = 0;
	private Label heightLabel;
	private ArrayList<GridPane> panes;
	private Gathering gathering;


	public Elevator( double velocity, int capacity, Gathering gatheringType )
	{
		this.velocity = velocity;
		Capacity = capacity;
		this.gathering = gatheringType;
	}
	
	
	public void AddHeightLabel( Label heightLabel )
	{
		this.heightLabel = heightLabel;
	}
	
	
	public void AddPanes( ArrayList<GridPane> panes )
	{
		this.panes = panes;
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
						if ( heightLabel != null )
						{
							heightLabel.setText( String.format( "%.2f", currentHeight.get() ));
							heightLabel.setTranslateY( -heightLabel.getLayoutY() / (Building.numOfFloors * Building.FloorHeight) * currentHeight.get());							
						}
					}
				});
	}
	
	
	public DoubleProperty currentHeightProperty()
	{
		return currentHeight;
	}


	public void SetBuilding( Building building )
	{
		this.building = building;
	}


	public void Simulate( long elapsedTime )
	{
		if ( isHandlingDoor )
		{
			if ( handlingDoorTime < 5000 )
			{
				handlingDoorTime += elapsedTime;
			}
			else
			{
				handlingDoorTime = 0;
				isHandlingDoor = false;
			}
		}
		else
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
	}
	
	
	public void AddOutsideRequest( ElevatorRequest request )
	{
		outsideRequests.add( request );
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
				if ( gathering == Gathering.Down )
				{
					ElevatorRequest insideRequest = GetClosestInsideRequestUp();
					return insideRequest == null ? GetClosestRequestDown() : insideRequest.getEndFloor();
				}
				else
				{
					return GetClosestRequestUp();
				}
			}
			// winda jechala w dol wiec staramy sie jechac w dol
			else
			{
				return GetClosestRequestDown();
			}
		}
		else if ( !outsideRequests.isEmpty() )
		{
			if ( outsideRequests.get( 0 ).getStartFloor() == currentFloor && PeopleInside != Capacity )
			{
				return currentFloor;
			}
			
			if ( gathering == Gathering.Down )
			{
				return GetHighestOutsideRequest();
			}
			else
			{
				// sprawdzamy czy najstarsze zadanie jest nizej
				if ( outsideRequests.get( 0 ).getStartFloor() < currentFloor )
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
		}
		else
		{
			return -1;
		}
	}
	

	private void AdjustDirection()
	{
		if ( floorToGo < currentFloor || currentFloor == Building.numOfFloors - 1 )
		{
			direction = Direction.down;
		}
		else if ( floorToGo > currentFloor || currentFloor == 0 )
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
		if ( direction == Direction.none || currentFloor == floorToGo )
		{
			FloorAchieved();
		}
		else
		{
			double height;
			int floor;
	
			if ( direction == Direction.up )
			{
				height = currentHeight.get() + velocity * ((double)elapsedTime / 1000);
				floor = (int) Math.floor( currentHeight.doubleValue() / Building.FloorHeight );
			}
			else
			{
				height = currentHeight.get() - velocity * ((double)elapsedTime / 1000);
				floor = (int) Math.ceil( currentHeight.get() / Building.FloorHeight );
			}
			
			setCurrentHeight( height );
			SetCurrentFloor( floor );
		}
	}
	
	
	private void FloorAchieved()
	{
		isMoving = false;
		isHandlingDoor = true;
		
		setCurrentHeight( currentFloor * Building.FloorHeight );
		
		System.out.println("Floor " + currentFloor + " achieved!");
		
		// wysiadajacy
		for( int i = 0; i < insideRequests.size(); i++ )
		{
			ElevatorRequest request = insideRequests.get( i );
			if ( request.getEndFloor() == currentFloor )
			{
				insideRequests.remove(i);
				i--;
				PeopleInside--;
				building.RequestsLeft--;
				System.out.println( "Removed one person!" );
			}
		}
		
		// wsiadajacy
		while( PeopleInside < Capacity )
		{
			ElevatorRequest request = building.GetFloorRequest( currentFloor );
			
			if ( request == null )
			{
				outsideRequests.removeIf( e -> e.getStartFloor() == currentFloor );
				break;
			}
			
			building.AddResult( request );
			outsideRequests.remove( request );
			insideRequests.add( request );
			PeopleInside++;
			System.out.println( "Added one person!" );
		}
		
		if ( panes != null )
		{
			RefreshPane( panes.get( Building.numOfFloors - currentFloor - 1 ));
		}
		
		System.out.println("Number of people in the elevator " + PeopleInside);
	}

	
	private void SetCurrentFloor( int floorToSet )
	{
		if ( floorToSet < 0 ) floorToSet = 0;
		if ( floorToSet >= Building.numOfFloors ) floorToSet = Building.numOfFloors - 1;
		
		if ( currentFloor != floorToSet )
		{
			if ( panes != null )
			{
				panes.get( Building.numOfFloors - currentFloor - 1 ).setVisible( false );
				GridPane currentPane = panes.get( Building.numOfFloors - floorToSet - 1 ); 
				currentPane.setVisible( true );
				
				RefreshPane( currentPane );				
			}
			
			currentFloor = floorToSet;
		}
	}
	
	
	private void RefreshPane( GridPane pane )
	{
		ObservableList<Node> children = pane.getChildren();
		
		for ( int i = 0; i < children.size(); i++ )
		{
			RadioButton button = (RadioButton) children.get( i );
			button.setSelected( false );
		}
		
		for ( int i = 0; i < insideRequests.size(); i++ )
		{
			RadioButton button = (RadioButton) children.get( insideRequests.get( i ).getEndFloor() );
			button.setSelected( true );
		}
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
		
		for( int i = 0; i < insideRequests.size(); i++ )
		{
			ElevatorRequest request = insideRequests.get( i );
			if ( request.getEndFloor() > currentFloor )
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

		for( int i = 0; i < insideRequests.size(); i++ )
		{
			ElevatorRequest request = insideRequests.get( i );
			if ( request.getEndFloor() < currentFloor )
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
		
		for( int i = 0; i < outsideRequests.size(); i++ )
		{
			ElevatorRequest request = outsideRequests.get( i );
			if ( request.getStartFloor() > currentFloor )
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
		
		for( int i = 0; i < outsideRequests.size(); i++ )
		{
			ElevatorRequest request = outsideRequests.get( i );
			if ( request.getStartFloor() < currentFloor )
			{
				if ( result == null || request.getStartFloor() > result.getStartFloor() )
				{
					result = request;
				}
			}
		}
		
		return result;
	}
	
	
	private int GetHighestOutsideRequest()
	{
		int result = 0;
		
		for( int i = 0; i < outsideRequests.size(); i++ )
		{
			int request = outsideRequests.get( i ).getStartFloor();
			if ( request > result )
			{
				result = request;
			}
		}
		
		return result;
	}
}
