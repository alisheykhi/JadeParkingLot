package Objects;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class Driver {
    private String name;
    private String car;

    public Driver(String name, String car) {
        this.name = name;
        this.car = car;
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
}
