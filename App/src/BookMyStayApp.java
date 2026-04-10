//Use Case 5: Booking Request

import java.util.*;

// Reservation class represents a booking request
class Reservation {
    private String guestName;
    private String roomType;
    private int nights;

    public Reservation(String guestName, String roomType, int nights) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    @Override
    public String toString() {
        return "Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Nights: " + nights;
    }
}

// Booking Request Queue handler
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>(); // FIFO Queue
    }

    // Add booking request
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    // View all requests (no processing)
    public void viewRequests() {
        if (queue.isEmpty()) {
            System.out.println("No booking requests in queue.");
            return;
        }

        System.out.println("\n--- Booking Request Queue (FIFO Order) ---");
        for (Reservation r : queue) {
            System.out.println(r);
        }
    }
}

// Main class
public class BookMyStayApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        System.out.print("Enter number of booking requests: ");
        int n = sc.nextInt();
        sc.nextLine(); // consume newline

        for (int i = 0; i < n; i++) {
            System.out.println("\nEnter details for request " + (i + 1));

            System.out.print("Guest Name: ");
            String name = sc.nextLine();

            System.out.print("Room Type: ");
            String roomType = sc.nextLine();

            System.out.print("Number of Nights: ");
            int nights = sc.nextInt();
            sc.nextLine(); // consume newline

            Reservation reservation = new Reservation(name, roomType, nights);
            bookingQueue.addRequest(reservation);
        }

        // Display queue
        bookingQueue.viewRequests();

        sc.close();
    }
}

// Booking Service
class BookingService {
    private Queue<Reservation> requestQueue;
    private Set<String> allocatedRoomIds;
    private Map<String, Set<String>> roomTypeMap;
    private InventoryService inventoryService;
    private int roomCounter = 1;

    public BookingService(Queue<Reservation> requestQueue, InventoryService inventoryService) {
        this.requestQueue = requestQueue;
        this.inventoryService = inventoryService;
        this.allocatedRoomIds = new HashSet<>();
        this.roomTypeMap = new HashMap<>();
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        return roomType.substring(0, 1).toUpperCase() + roomCounter++;
    }

    // Process bookings
    public void processBookings() {
        while (!requestQueue.isEmpty()) {
            Reservation r = requestQueue.poll(); // FIFO

            String roomType = r.getRoomType();

            System.out.println("\nProcessing request for " + r.getGuestName());

            // Check availability
            if (!inventoryService.isAvailable(roomType)) {
                System.out.println("No rooms available for type: " + roomType);
                continue;
            }

            // Generate unique ID
            String roomId;
            do {
                roomId = generateRoomId(roomType);
            } while (allocatedRoomIds.contains(roomId));

            // Allocate room
            allocatedRoomIds.add(roomId);

            roomTypeMap.putIfAbsent(roomType, new HashSet<>());
            roomTypeMap.get(roomType).add(roomId);

            // Update inventory immediately
            inventoryService.decrement(roomType);

            // Confirmation
            System.out.println("Reservation CONFIRMED for " + r.getGuestName());
            System.out.println("Room Type: " + roomType + ", Room ID: " + roomId);
        }
    }

    public void displayAllocations() {
        System.out.println("\n--- Allocated Rooms ---");
        for (String type : roomTypeMap.keySet()) {
            System.out.println(type + " -> " + roomTypeMap.get(type));
        }
    }
}

// Main class
public class BookMyStayApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Queue<Reservation> queue = new LinkedList<>();

        System.out.print("Enter number of booking requests: ");
        int n = sc.nextInt();
        sc.nextLine();

        // Input requests (same as UC5)
        for (int i = 0; i < n; i++) {
            System.out.println("\nEnter details for request " + (i + 1));

            System.out.print("Guest Name: ");
            String name = sc.nextLine();

            System.out.print("Room Type (Single/Double/Suite): ");
            String roomType = sc.nextLine();

            System.out.print("Nights: ");
            int nights = sc.nextInt();
            sc.nextLine();

            queue.offer(new Reservation(name, roomType, nights));
        }

        InventoryService inventoryService = new InventoryService();
        BookingService bookingService = new BookingService(queue, inventoryService);

        // Process bookings
        bookingService.processBookings();

        // Show results
        bookingService.displayAllocations();
        inventoryService.displayInventory();

        sc.close();
    }
}