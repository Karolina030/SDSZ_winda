package mainPackage;
import java.io.Console;

public class Main
{
	public static void main( String[] args )
	{
		double elevatorVelocity = 0.63;
		int elevatorCapacity = 8;
		int numOfFloors = 8;
		int numOfPeople = 200;
		int simulationTime = 100;
		int simulationSpeed = 1;
		
		Elevator[] elevators = new Elevator[] {new Elevator( elevatorVelocity, elevatorCapacity )};
		Building building = new Building( elevators, numOfFloors, numOfPeople );
		Simulation simulation = new Simulation( building, simulationTime, simulationSpeed );
		
		simulation.start();
		
		Console console = System.console();
		if ( console != null && console.readLine() != null )
		{
			simulation.stop();
		}
		
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
