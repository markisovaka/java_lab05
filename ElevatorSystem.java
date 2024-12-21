import java.util.concurrent.*;

public class ElevatorSystem {
    public static void main(String[] args) {
        BlockingQueue<ElevatorRequest> requests = new LinkedBlockingQueue<>();
        int maxFloor = 10;
        int elevatorCount = 3;

        RequestGenerator generator = new RequestGenerator(requests, maxFloor);
        new Thread(generator).start();

        for (int i = 1; i <= elevatorCount; i++) {
            new Thread(new Elevator(i, requests)).start();
        }
    }
}
