//Use Case 3: Centralized Room Inventory Management

import java.util.HashMap;
import java.util.Map;

public class BookMyStayApp {
    // Stores available room count for each room type
    private Map<String, Integer> roomAvailability;

    // Constructor
    public BookMyStayApp() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    // Initialize default room availability
    private void initializeInventory() {
        roomAvailability.put("SingleRoom", 10);
        roomAvailability.put("DoubleRoom", 8);
        roomAvailability.put("DeluxeRoom", 5);
        roomAvailability.put("SuiteRoom", 2);
    }

    // Get availability map
    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }

    // Update availability
    public void updateAvailability(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }

    // Display inventory
    public void displayInventory() {
        System.out.println("Room Availability:");
        for (Map.Entry<String, Integer> entry : roomAvailability.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    // Main method (for testing)
    public static void main(String[] args) {
        BookMyStayApp inventory = new BookMyStayApp();

        inventory.displayInventory();

        System.out.println("\nUpdating DeluxeRoom availability...\n");
        inventory.updateAvailability("DeluxeRoom", 3);

        inventory.displayInventory();
    }
}

