package mainPackage;

public class Simulation extends Thread
{
	private int updatesPerSecond = 5;
	private int targetTime = 1000 / updatesPerSecond;
	private long currentTime;
	private long lastTime = System.currentTimeMillis();
	private long elapsedTime;
	private long sleepTime;
	private boolean isRunning = true;
	
	private Elevator[] elevators;
	
	
	public Simulation( Elevator[] elevators )
	{
		this.elevators = elevators;
	}
	
	
	@Override
	public void run()
	{
		System.out.println("Starting simulation!");
		
	    while ( isRunning )
	    {
	        // get current time (in nanoseconds)
	        currentTime = System.currentTimeMillis();

	        // get time elapsed since last update
	        elapsedTime = currentTime - lastTime;
	        lastTime = currentTime;

	        // run your update
	        Update( elapsedTime );

	        // compute the thread sleep time in milliseconds.
	        sleepTime = targetTime - elapsedTime;

	        // don't let sleepTime drop below 0
	        if (sleepTime < 0)
	        {
	            sleepTime = 1;
	        }

	        // attempt to sleep
	        try
	        {
	            Thread.sleep(sleepTime);
	        }
	        catch( Exception e )
	        {
	            e.printStackTrace();
	        }
	    }
	}
	
	
	private void Update( long elapsedTime )
	{
		for( Elevator elevator : this.elevators )
		{
			elevator.Simulate( elapsedTime );
		}
	}
}
