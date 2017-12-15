package Objects;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class Driver {
    private String name;
    private String car;
    private LocalDateTime dateIn;
    private LocalDateTime dateOut;

    public Driver(String name, String car, LocalDateTime dateIn, LocalDateTime dateOut) {
        this.name = name;
        this.car = car;
        this.dateIn = dateIn;
        this.dateOut = dateOut;
    }

    public Driver() {
        String Name = "src/Names/Names.txt";
        String Car  = "src/Names/Cars.txt";

        try (Stream<String> stream = Files.lines(Paths.get(Name))) {
            int index = ThreadLocalRandom.current().nextInt(1, 1000);
            this.name = stream.toArray()[index].toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Stream<String> stream = Files.lines(Paths.get(Car))) {
            int index = ThreadLocalRandom.current().nextInt(1, 20);
            this.car = stream.toArray()[index].toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int rand = 10 + (int)(Math.random() * 200);
        this.dateIn = LocalDateTime.now();
        this.dateOut = dateIn.plusMinutes(rand);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public LocalDateTime getDateIn() {
        return dateIn;
    }

    public void setDateIn(LocalDateTime dateIn) {
        this.dateIn = dateIn;
    }

    public LocalDateTime getDateOut() {
        return dateOut;
    }

    public void setDateOut(LocalDateTime dateOut) {
        this.dateOut = dateOut;
    }
}
