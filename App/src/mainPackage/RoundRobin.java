package mainPackage;

import java.util.ArrayList;

public class RoundRobin
{
    private int counter = 0;

    public int chooseElevator( ArrayList<Elevator> elevators )
    {
    	int L = elevators.size();
    	
        if ( counter == L )
        {
            counter = 0;
        }

        int pick = counter;
        counter++;
        return pick;
    }
}