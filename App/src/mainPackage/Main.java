package mainPackage;

public class Main
{
	public static void main( String[] args )
	{
		Elevator[] elevators = new Elevator[] {new Elevator(0.63, 8)};
		Simulation simulation = new Simulation( elevators );
		simulation.start();
		try
		{
			simulation.join();
		} catch ( InterruptedException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
