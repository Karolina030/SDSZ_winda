package mainPackage;

public class Simulation extends Thread
{
	public float SimulationSpeed = 1;

	private Building building;
	private int simulationTime;

	private int updatesPerSecond = 5;
	private int targetTime = 1000 / updatesPerSecond;
	private long currentTime;
	private long lastTime;
	private long elapsedTime;
	private long totalElapsedTime;
	private long sleepTime;
	
	
	public Simulation( Building building, long simulationTimeInSeconds )
	{
		this.building = building;
		this.simulationTime = (int) simulationTimeInSeconds * 1000;
	}
	
	
	@Override
	public void run()
	{
		System.out.println( "Starting simulation!" );
		lastTime = System.currentTimeMillis();
		
	    while ( totalElapsedTime < simulationTime || building.RequestsLeft > 0 )
	    {
	        // get current time (in nanoseconds)
	        currentTime = System.currentTimeMillis();

	        // get time elapsed since last update
	        elapsedTime = (long) ( (currentTime - lastTime) * SimulationSpeed );
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
	    
	}
	
	
	private void Update()
	{
		building.Simulate( elapsedTime, totalElapsedTime );
	}
}
