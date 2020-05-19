package mainPackage;
import java.util.PriorityQueue;
import java.util.Random;

public class Floor {

    PriorityQueue<ElevatorRequest> floorRequests;
    private int ID;

    Floor(int number) {
        floorRequests = new PriorityQueue<>();
        ID = number;
    }

    public PriorityQueue<ElevatorRequest> getFloorRequests() {
        return floorRequests;
    }

    public int getID() {
        return ID;
    }

    public void addRequest(ElevatorRequest request) {
        floorRequests.add(request);
    }


}