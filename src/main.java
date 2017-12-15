import FIPA.DateTime;
import Objects.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;


public class main {

    public static void main(String[] args){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime out = now.plusMinutes(20);
        System.out.println(out.format(formatter));
        System.out.println((Math.random() * 100)-50);
        System.out.println((Math.random() * 100)- 50);
        System.out.println((Math.random() * 100) - 50);
    }
}
