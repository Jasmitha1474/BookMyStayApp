//Use Case 9: Error Handling & Validation

import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

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

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory;

    public InventoryService() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrement(String roomType) throws InvalidBookingException {
        int count = inventory.get(roomType);

        if (count <= 0) {
            throw new InvalidBookingException("Inventory cannot go below zero for " + roomType);
        }

        inventory.put(roomType, count - 1);
    }
}

// Validator Class
class InvalidBookingValidator {
    private InventoryService inventoryService;

    public InvalidBookingValidator(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void validate(Reservation r) throws InvalidBookingException {

        // Validate guest name
        if (r.getGuestName() == null || r.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        // Validate room type
        if (!inventoryService.isValidRoomType(r.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + r.getRoomType());
        }

        // Validate availability
        if (!inventoryService.isAvailable(r.getRoomType())) {
            throw new InvalidBookingException("No rooms available for type: " + r.getRoomType());
        }
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        InventoryService inventoryService = new InventoryService();
        InvalidBookingValidator validator = new InvalidBookingValidator(inventoryService);

        System.out.print("Enter number of booking attempts: ");
        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n; i++) {
            try {
                System.out.println("\nEnter booking details " + (i + 1));

                System.out.print("Guest Name: ");
                String name = sc.nextLine();

                System.out.print("Room Type (Single/Double/Suite): ");
                String roomType = sc.nextLine();

                Reservation r = new Reservation(name, roomType);

                // VALIDATION (Fail Fast)
                validator.validate(r);

                // SAFE INVENTORY UPDATE
                inventoryService.decrement(roomType);

                System.out.println("Booking SUCCESSFUL for " + name);

            } catch (InvalidBookingException e) {
                // Graceful error handling
                System.out.println("Booking FAILED: " + e.getMessage());
            }
        }

        sc.close();
    }
}