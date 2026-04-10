//Use Case 8: Booking History & Reporting

import java.util.*;

// Reservation class (simplified confirmed booking)
class Reservation {
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

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType;
    }
}

// Booking History (stores confirmed reservations)
class BookingHistory {
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed booking
    public void addReservation(Reservation r) {
        history.add(r); // maintains insertion order
        System.out.println("Reservation stored in history: " + r.getReservationId());
    }

    // Get all bookings
    public List<Reservation> getAllReservations() {
        return history;
    }
}

// Reporting Service
class BookingReportService {

    // Display all bookings
    public void displayAllBookings(List<Reservation> history) {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : history) {
            System.out.println(r);
        }
    }

    // Summary report (count per room type)
    public void generateSummary(List<Reservation> history) {
        Map<String, Integer> summary = new HashMap<>();

        for (Reservation r : history) {
            String type = r.getRoomType();
            summary.put(type, summary.getOrDefault(type, 0) + 1);
        }

        System.out.println("\n--- Booking Summary Report ---");
        for (String type : summary.keySet()) {
            System.out.println(type + " Rooms Booked: " + summary.get(type));
        }
    }
}

// Main class
public class BookMyStayApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        BookingHistory bookingHistory = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        System.out.print("Enter number of confirmed bookings: ");
        int n = sc.nextInt();
        sc.nextLine();

        // Input confirmed bookings
        for (int i = 0; i < n; i++) {
            System.out.println("\nEnter details for booking " + (i + 1));

            System.out.print("Reservation ID: ");
            String id = sc.nextLine();

            System.out.print("Guest Name: ");
            String name = sc.nextLine();

            System.out.print("Room Type: ");
            String roomType = sc.nextLine();

            Reservation r = new Reservation(id, name, roomType);
            bookingHistory.addReservation(r);
        }

        // Display history
        List<Reservation> history = bookingHistory.getAllReservations();
        reportService.displayAllBookings(history);

        // Generate summary
        reportService.generateSummary(history);

        sc.close();
    }
}