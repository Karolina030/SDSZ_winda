package mainPackage;

public class ElevatorRequest implements Comparable<ElevatorRequest>
{
	private int startFloor;
	private int endFloor;
	public long appearTime;
	
	public ElevatorRequest( int startFloor, int endFloor, long appearTime )
	{
		this.setStartFloor(startFloor);
		this.setEndFloor(endFloor);
		this.appearTime = appearTime * 1000;
	}

	@Override
	public int compareTo( ElevatorRequest other )
	{
		return (int) (this.appearTime - other.appearTime);
	}

	public int getStartFloor()
	{
		return startFloor;
	}

	public void setStartFloor( int startFloor )
	{
		this.startFloor = startFloor;
	}

	public int getEndFloor()
	{
		return endFloor;
	}

	public void setEndFloor( int endFloor )
	{
		this.endFloor = endFloor;
	}
}
