import java.util.*;
import java.util.concurrent.*;

public class RequestGenerator implements Runnable {
    private BlockingQueue<ElevatorRequest> requests;
    private int maxFloor;

    public RequestGenerator(BlockingQueue<ElevatorRequest> requests, int maxFloor) {
        this.requests = requests;
        this.maxFloor = maxFloor;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            int source = random.nextInt(maxFloor) + 1;
            int destination;
            do {
                destination = random.nextInt(maxFloor) + 1;
            } while (destination == source);
            try {
                requests.put(new ElevatorRequest(source, destination));
                System.out.println("Request generated from floor " + source + " to floor " + destination);
                Thread.sleep(random.nextInt(3000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
