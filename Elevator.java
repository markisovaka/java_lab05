import java.util.*;
import java.util.concurrent.*;

public class Elevator implements Runnable {
    private int id;
    private int currentFloor;
    private boolean movingUp;
    private PriorityQueue<Integer> upQueue;
    private PriorityQueue<Integer> downQueue;
    private BlockingQueue<ElevatorRequest> requests;

    public Elevator(int id, BlockingQueue<ElevatorRequest> requests) {
        this.id = id;
        this.currentFloor = 1;
        this.movingUp = true;
        this.upQueue = new PriorityQueue<>();
        this.downQueue = new PriorityQueue<>(Comparator.reverseOrder());
        this.requests = requests;
    }

    private void moveToFloor(int floor) {
        while (currentFloor != floor) {
            currentFloor += currentFloor < floor ? 1 : -1;
            log("Elevator " + id + " moving to floor " + currentFloor);
        }
    }

    private void processRequest(ElevatorRequest request) {
        upQueue.add(request.sourceFloor);
        if (request.sourceFloor < request.destinationFloor) {
            upQueue.add(request.destinationFloor);
        } else {
            downQueue.add(request.destinationFloor);
        }
    }

    private void handleRequests() {
        while (!upQueue.isEmpty() || !downQueue.isEmpty()) {
            if (movingUp && !upQueue.isEmpty()) {
                int nextFloor = upQueue.poll();
                moveToFloor(nextFloor);
            } else if (!movingUp && !downQueue.isEmpty()) {
                int nextFloor = downQueue.poll();
                moveToFloor(nextFloor);
            }
            movingUp = !upQueue.isEmpty() || downQueue.isEmpty();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                ElevatorRequest request = requests.poll(100, TimeUnit.MILLISECONDS);
                if (request != null) {
                    processRequest(request);
                }
                handleRequests();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void log(String message) {
        System.out.println(message);
    }
}