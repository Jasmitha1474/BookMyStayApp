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
