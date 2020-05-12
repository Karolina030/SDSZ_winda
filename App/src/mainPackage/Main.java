package mainPackage;

public class Main
{
	public static void main( String[] args )
	{
		double elevatorVelocity = 0.63;
		int elevatorCapacity = 8;
		int numOfFloors = 8;
		int numOfPeople = 100;
		int simulationTime = 60;
		
		Elevator[] elevators = new Elevator[] {new Elevator( elevatorVelocity, elevatorCapacity )};
		Building building = new Building( elevators, numOfFloors, numOfPeople );
		Simulation simulation = new Simulation( building, simulationTime );
		
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
