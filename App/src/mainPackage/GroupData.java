package mainPackage;

import javafx.beans.property.SimpleIntegerProperty;

public class GroupData
{
	private SimpleIntegerProperty people;
	private SimpleIntegerProperty floor;
	private SimpleIntegerProperty appear;
	private SimpleIntegerProperty period;
	
	public GroupData( int people, int floor, int appear, int period )
	{
		this.people = new SimpleIntegerProperty( people );
		this.floor = new SimpleIntegerProperty( floor );
		this.appear = new SimpleIntegerProperty( appear );
		this.period = new SimpleIntegerProperty( period );
	}

	public int getPeople()
	{
		return people.get();
	}

	public void setPeople( int people )
	{
		this.people.set( people );
	}

	public int getFloor()
	{
		return floor.get();
	}

	public void setFloor( int floor )
	{
		this.floor.set( floor );
	}

	public int getAppear()
	{
		return appear.get();
	}

	public void setAppear( int appear )
	{
		this.appear.set( appear );
	}

	public int getPeriod()
	{
		return period.get();
	}

	public void setPeriod( int period )
	{
		this.period.set( period );
	}
}
