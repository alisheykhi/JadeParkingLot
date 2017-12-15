package Objects;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class ParkingLot {
    private String name;
    private int capacity;
    private int availableSpace;
    private Location location;

    public ParkingLot(String name, int capacity, Location location) {
        this.name = name;
        this.capacity = capacity;
        this.availableSpace = capacity;
        this.location = location;
    }

    public ParkingLot() {
        String parking  = "src/Names/Parking.txt";
        try (Stream<String> stream = Files.lines(Paths.get(parking))) {
            int index = ThreadLocalRandom.current().nextInt(1, 15);
            this.name = stream.toArray()[index].toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.capacity =(int) Math.random() * 50;
        this.location.setLatitude((Math.random() * 100)-50);
        this.location.setLongitude((Math.random() * 100)-50);
        this.availableSpace = capacity;
    }

    public ParkingLot(int capacity) {
        String parking  = "src/Names/Parking.txt";
        try (Stream<String> stream = Files.lines(Paths.get(parking))) {
            int index = ThreadLocalRandom.current().nextInt(1, 15);
            this.name = stream.toArray()[index].toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.capacity = capacity;
        this.location.setLatitude((Math.random() * 100)-50);
        this.location.setLongitude((Math.random() * 100)-50);
        this.availableSpace = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        capacity = capacity;
    }

    public int getAvailableSpace() {
        return availableSpace;
    }

    public void setAvailableSpace(int availableSpace) {
        this.availableSpace = availableSpace;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}