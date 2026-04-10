//Use Case 10: Booking Cancellation & Inventory Rollback

import java.util.*;

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "ID: " + reservationId + ", Guest: " + guestName +
                ", RoomType: " + roomType + ", RoomID: " + roomId;
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

    public void increment(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
    }

    public void displayInventory() {
        System.out.println("\nUpdated Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

// Cancellation Service
class CancellationService {
    private Map<String, Reservation> confirmedBookings;
    private Stack<String> rollbackStack;
    private InventoryService inventoryService;

    public CancellationService(Map<String, Reservation> confirmedBookings,
                               InventoryService inventoryService) {
        this.confirmedBookings = confirmedBookings;
        this.inventoryService = inventoryService;
        this.rollbackStack = new Stack<>();
    }

    public void cancelBooking(String reservationId) {

        // Validate existence
        if (!confirmedBookings.containsKey(reservationId)) {
            System.out.println("Cancellation FAILED: Reservation not found");
            return;
        }

        Reservation r = confirmedBookings.get(reservationId);

        System.out.println("\nCancelling Reservation: " + reservationId);

        // Step 1: Push roomId to stack (rollback tracking)
        rollbackStack.push(r.getRoomId());

        // Step 2: Restore inventory
        inventoryService.increment(r.getRoomType());

        // Step 3: Remove from confirmed bookings
        confirmedBookings.remove(reservationId);

        System.out.println("Cancellation SUCCESSFUL for " + reservationId);
    }

    public void displayRollbackStack() {
        System.out.println("\nRollback Stack (Recent Releases): " + rollbackStack);
    }
}

// Main class
public class BookMyStayApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        InventoryService inventoryService = new InventoryService();

        // Pre-loaded confirmed bookings (simulating UC6 output)
        Map<String, Reservation> confirmedBookings = new HashMap<>();

        confirmedBookings.put("R1", new Reservation("R1", "Asha", "Single", "S1"));
        confirmedBookings.put("R2", new Reservation("R2", "Ravi", "Double", "D1"));
        confirmedBookings.put("R3", new Reservation("R3", "John", "Suite", "SU1"));

        CancellationService cancellationService =
                new CancellationService(confirmedBookings, inventoryService);

        System.out.print("Enter Reservation ID to cancel: ");
        String id = sc.nextLine();

        // Perform cancellation
        cancellationService.cancelBooking(id);

        // Display updated state
        cancellationService.displayRollbackStack();
        inventoryService.displayInventory();

        sc.close();
    }
}