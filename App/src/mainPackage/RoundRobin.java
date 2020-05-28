package mainPackage;

import java.util.ArrayList;

public class RoundRobin {

    private int counter;

    public RoundRobin(){
        this.counter = 0;
    }

    public int chooseElevator( ArrayList<Elevator> elevatorGroup )
    {
    	int L = elevatorGroup.size();
    	
        if (counter == L) {
            counter = 0;
        }

        if (counter == L-1) {
            counter = -1;
        }

        int pick = counter;

        if (counter<L-1) {
            pick = ++this.counter % L;
        }

       while (elevatorGroup.get(pick).Capacity < (elevatorGroup.get(pick).PeopleInside)+1 && pick<L+1) {
            pick = ++this.counter % L;

        }
        counter = pick;
        return pick;


    }
}