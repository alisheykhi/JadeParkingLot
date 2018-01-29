package Objects;

import jade.core.AID;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class ParkingLot implements Serializable {
    private String name;
    private int capacity;
    private int availableSpace;
    private int reserved;
    private Location location;
    private AID agentID;

    public ParkingLot(String name, int capacity, Location location, AID agentID, int reserved) {
        this.name = name;
        this.capacity = capacity;
        this.availableSpace = capacity;
        this.location = location;
        this.agentID = agentID;
        this.reserved = reserved;
    }

    public ParkingLot(String name) {
        this.name = name;
    }

    public ParkingLot() {
        String parking = "src/Names/Parking.txt";
        try (Stream<String> stream = Files.lines(Paths.get(parking))) {
            int index = ThreadLocalRandom.current().nextInt(1, 15);
            this.name = stream.toArray()[index].toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //this.capacity =(int) (Math.random() * 50);
        this.capacity = 31;
        this.location = new Location((Math.random() * 100) - 50, (Math.random() * 100) - 50);
        //this.availableSpace = (int) (Math.random() * this.capacity);
        this.availableSpace = 31;
        this.reserved = 0;
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

    public AID getAgentID() {
        return agentID;
    }

    public void setAgentID(AID agentID) {
        this.agentID = agentID;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

}