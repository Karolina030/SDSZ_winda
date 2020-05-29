package mainPackage;

import application.SimulationController;

public class Simulation extends Thread
{
	private Building building;
	private int simulationTime;
	private int simulationSpeed;

	private int updatesPerSecond = 5;
	private int targetTime = 1000 / updatesPerSecond;
	private long currentTime;
	private long lastTime;
	private long elapsedTime;
	private long totalElapsedTime;
	private long sleepTime;
	private SimulationController simulationController;
	
	
	public Simulation( Building building, long simulationTimeInSeconds, int simulationSpeed, SimulationController simulationController )
	{
		this.building = building;
		this.simulationTime = (int) simulationTimeInSeconds * 1000 * simulationSpeed;
		this.simulationSpeed = simulationSpeed;
		this.simulationController = simulationController;
	}
	
	
	@Override
	public void run()
	{
		System.out.println( "Starting simulation!" );
		lastTime = System.currentTimeMillis();
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
	    
	    simulationController.ShowResults( building.GetResults() );
	}
	
	
	private void Update()
	{
		building.Simulate( elapsedTime, totalElapsedTime );
	}
}
