//Use Case 7: Add-On Service Selection
import java.util.*;

// Add-On Service class
class Service {
    private String serviceName;
    private double cost;

    public Service(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
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

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return serviceName + " (Rs. " + cost + ")";
    }
}

// Add-On Service Manager
class AddOnServiceManager {
    private Map<String, List<Service>> reservationServicesMap;

    public AddOnServiceManager() {
        reservationServicesMap = new HashMap<>();
    }

    // Add service to a reservation
    public void addService(String reservationId, Service service) {
        reservationServicesMap.putIfAbsent(reservationId, new ArrayList<>());
        reservationServicesMap.get(reservationId).add(service);

        System.out.println("Added service: " + service.getServiceName() +
                " to Reservation ID: " + reservationId);
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {
        List<Service> services = reservationServicesMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services for Reservation ID: " + reservationId);
            return;
        }

        System.out.println("\nServices for Reservation ID: " + reservationId);
        for (Service s : services) {
            System.out.println(s);
        }
    }

    // Calculate total cost
    public double calculateTotalCost(String reservationId) {
        List<Service> services = reservationServicesMap.get(reservationId);
        double total = 0;

        if (services != null) {
            for (Service s : services) {
                total += s.getCost();
            }
        }

        return total;
    }
}

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
        AddOnServiceManager manager = new AddOnServiceManager();

        System.out.print("Enter Reservation ID: ");
        String reservationId = sc.nextLine();

        System.out.print("Enter number of add-on services: ");
        int n = sc.nextInt();
        sc.nextLine();

        // Input services
        for (int i = 0; i < n; i++) {
            System.out.println("\nEnter service " + (i + 1));

            System.out.print("Service Name: ");
            String name = sc.nextLine();

            System.out.print("Service Cost: ");
            double cost = sc.nextDouble();
            sc.nextLine();

            Service service = new Service(name, cost);
            manager.addService(reservationId, service);
        }

        // Display services
        manager.displayServices(reservationId);

        // Show total cost
        double totalCost = manager.calculateTotalCost(reservationId);
        System.out.println("\nTotal Add-On Cost: Rs. " + totalCost);

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