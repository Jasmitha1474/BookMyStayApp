// UC6 - Reservation Confirmation & Room Allocation

import java.util.*;

// Reservation class (same as UC5)
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
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory;

    public InventoryService() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " Rooms Left: " + inventory.get(type));
        }
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