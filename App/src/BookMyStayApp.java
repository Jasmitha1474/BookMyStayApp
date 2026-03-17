import static java.lang.IO.println;
//Use Case 2: Basic Room Types & Static Availability

abstract class Room {

    // Attributes
    protected int numberOfBeds;
    protected int squareFeet;
    protected double pricePerNight;

    // Constructor
    public Room(int numberOfBeds, int squareFeet, double pricePerNight) {
        this.numberOfBeds = numberOfBeds;
        this.squareFeet = squareFeet;
        this.pricePerNight = pricePerNight;
    }

    // Method to display details
    public void displayRoomDetails() {
        System.out.println("Number of Beds: " + numberOfBeds);
        System.out.println("Room Size (sq ft): " + squareFeet);
        System.out.println("Price per Night: " + pricePerNight);
    }
}

// Child class
class DeluxeRoom extends Room {

    public DeluxeRoom() {
        super(2, 400, 2500.0);
    }
}

// Main class
public class BookMyStayApp {
    public static void main(String[] args) {
        Room room = new DeluxeRoom();
        room.displayRoomDetails();
    }
}
