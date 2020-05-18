package mainPackage;

public class Simulation extends Thread
{
	private Building building;
	private int simulationTime;
	private int simulationSpeed;

	private int updatesPerSecond = 5;
	private int targetTime = 1000 / updatesPerSecond;
	private long currentTime;
	private long lastTime = System.currentTimeMillis();
	private long elapsedTime;
	private long totalElapsedTime;
	private long sleepTime;
	
	
	public Simulation( Building building, long simulationTimeInSeconds, int simulationSpeed )
	{
		this.building = building;
		this.simulationTime = (int) simulationTimeInSeconds * 1000 * simulationSpeed;
		this.simulationSpeed = simulationSpeed;
	}
	
	
	@Override
	public void run()
	{
		System.out.println( "Starting simulation!" );
		
		building.GeneratePeopleQueue( simulationTime );
		
	    while ( totalElapsedTime < simulationTime )
	    {
	        // get current time (in nanoseconds)
	        currentTime = System.currentTimeMillis();

	        // get time elapsed since last update
	        elapsedTime = (currentTime - lastTime) * simulationSpeed;
	        totalElapsedTime += elapsedTime;
	        lastTime = currentTime;

	        // run your update
	        Update();

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
	    
	    System.out.println("Simulation ended!");
		System.out.println("Average waiting time: " + building.GetResults());
	}
	
	
	private void Update()
	{
		building.Simulate( elapsedTime, totalElapsedTime );
	}
}
