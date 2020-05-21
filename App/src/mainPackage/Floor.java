package mainPackage;
import java.util.LinkedList;

import javafx.application.Platform;
import javafx.scene.control.RadioButton;

public class Floor {

    LinkedList<ElevatorRequest> floorRequests;
    private int ID;
    private RadioButton button;

    Floor( int number, RadioButton button ) {
        floorRequests = new LinkedList<>();
        ID = number;
        this.button = button;
    }

    public ElevatorRequest getFloorRequest()
    {
    	if ( floorRequests.size() == 0 )
    	{
    		return null;
    	}
    	
    	int size = floorRequests.size();

    	Platform.runLater( new Runnable()
		{
			public void run()
			{
		    	if ( size - 1 == 0 )
				{
					button.setSelected( false );
				}
				button.setText( String.valueOf( size - 1) );
			}
		});

        return floorRequests.poll();
    }

    public int getID() {
        return ID;
    }

    public void addRequest(ElevatorRequest request)
    {
    	floorRequests.add( request );
    	
		Platform.runLater( new Runnable()
		{
			public void run()
			{
				button.setSelected( true );
				button.setText( String.valueOf( floorRequests.size() ) );
			}
		});
    }


}