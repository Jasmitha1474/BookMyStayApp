//Use Case 12: Data Persistence & System Recovery
import java.io.*;
import java.util.*;

// Reservation class (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "ID: " + reservationId + ", Guest: " + guestName + ", Room: " + roomType;
    }
}

// Wrapper class to store full system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> bookingHistory;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // SAVE (Serialization)
    public void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("\nSystem state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // LOAD (Deserialization)
    public SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("System state loaded successfully.");
            return state;

        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state. Starting with safe defaults.");
        }

        // fallback (safe state)
        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);

        return new SystemState(inventory, new ArrayList<>());
    }
}

// Main class
public class BookMyStayApp {
    public static void main(String[] args) {

        PersistenceService persistence = new PersistenceService();

        // LOAD previous state
        SystemState state = persistence.load();

        Scanner sc = new Scanner(System.in);

        // Display recovered data
        System.out.println("\nRecovered Booking History:");
        for (Reservation r : state.bookingHistory) {
            System.out.println(r);
        }

        System.out.println("\nRecovered Inventory:");
        for (String type : state.inventory.keySet()) {
            System.out.println(type + ": " + state.inventory.get(type));
        }

        // Simulate new booking
        System.out.print("\nAdd new booking? (yes/no): ");
        String choice = sc.nextLine();

        if (choice.equalsIgnoreCase("yes")) {

            System.out.print("Reservation ID: ");
            String id = sc.nextLine();

            System.out.print("Guest Name: ");
            String name = sc.nextLine();

            System.out.print("Room Type: ");
            String roomType = sc.nextLine();

            // Update state
            state.bookingHistory.add(new Reservation(id, name, roomType));

            if (state.inventory.getOrDefault(roomType, 0) > 0) {
                state.inventory.put(roomType, state.inventory.get(roomType) - 1);
            } else {
                System.out.println("No rooms available for this type!");
            }
        }

        // SAVE state before exit
        persistence.save(state);

        sc.close();
    }
}
