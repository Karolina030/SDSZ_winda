package mainPackage;

public class ElevatorRequest implements Comparable<ElevatorRequest>
{
	int startFloor;
	int endFloor;
	long appearTime;
	
	public ElevatorRequest( int startFloor, int endFloor, long appearTime )
	{
		this.startFloor = startFloor;
		this.endFloor = endFloor;
		this.appearTime = appearTime;
	}

	@Override
	public int compareTo( ElevatorRequest other )
	{
		return (int) (this.appearTime - other.appearTime);
	}

	public void EndRequest()
	{
		System.out.println( "Inside request done at " + endFloor );
	}
}
