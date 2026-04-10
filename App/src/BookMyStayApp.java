//Use Case 11: Concurrent Booking Simulation

import java.util.*;

// Reservation class
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Shared Inventory Service (Thread-Safe)
class InventoryService {
    private Map<String, Integer> inventory;

    public InventoryService() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    // synchronized critical section
    public synchronized boolean allocateRoom(String roomType) {
        int count = inventory.getOrDefault(roomType, 0);

        if (count > 0) {
            inventory.put(roomType, count - 1);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

// Concurrent Booking Processor
class BookingProcessor implements Runnable {
    private Queue<Reservation> queue;
    private InventoryService inventory;

    public BookingProcessor(Queue<Reservation> queue, InventoryService inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            Reservation r;

            // synchronized access to shared queue
            synchronized (queue) {
                if (queue.isEmpty()) {
                    break;
                }
                r = queue.poll();
            }

            // critical section for allocation
            boolean success = inventory.allocateRoom(r.getRoomType());

            if (success) {
                System.out.println(Thread.currentThread().getName() +
                        " SUCCESS: " + r.getGuestName() +
                        " booked " + r.getRoomType());
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " FAILED: No " + r.getRoomType() +
                        " rooms for " + r.getGuestName());
            }
        }
    }
}

// Main class
public class BookMyStayApp {
    public static void main(String[] args) {

        // Shared Queue
        Queue<Reservation> queue = new LinkedList<>();

        // Simulated concurrent requests
        queue.add(new Reservation("Asha", "Single"));
        queue.add(new Reservation("Ravi", "Single"));
        queue.add(new Reservation("John", "Single"));
        queue.add(new Reservation("Meena", "Double"));
        queue.add(new Reservation("Arun", "Double"));
        queue.add(new Reservation("Priya", "Suite"));
        queue.add(new Reservation("Kiran", "Suite"));

        InventoryService inventory = new InventoryService();

        // Multiple threads (guests)
        Thread t1 = new Thread(new BookingProcessor(queue, inventory), "Thread-1");
        Thread t2 = new Thread(new BookingProcessor(queue, inventory), "Thread-2");
        Thread t3 = new Thread(new BookingProcessor(queue, inventory), "Thread-3");

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final state
        inventory.displayInventory();
    }
}