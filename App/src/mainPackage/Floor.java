package mainPackage;
import java.util.LinkedList;

public class Floor {

    LinkedList<ElevatorRequest> floorRequests;
    private int ID;

    Floor(int number) {
        floorRequests = new LinkedList<>();
        ID = number;
    }

    public LinkedList<ElevatorRequest> getFloorRequests() {
        return floorRequests;
    }

    public int getID() {
        return ID;
    }

    public void addRequest(ElevatorRequest request) {
        floorRequests.add(request);
    }


}