package Runner;

import Objects.ParkingLot;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import Objects.Driver;

public class StartSystem {
    public static void main(String args[]) throws InterruptedException, StaleProxyException {
//        Driver driver = new Driver();
//        System.out.println(driver.getName());
//        System.out.println(driver.getCar());
//        System.out.println(driver.getDateIn());
//        System.out.println(driver.getDateOut());
        final Runtime runTime = Runtime.instance();
        runTime.setCloseVM(true);
        Profile mainProfile = new ProfileImpl(true);
        AgentContainer mainContainer = runTime.createMainContainer(mainProfile);
        AgentController rma = mainContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
        rma.start();
        Thread.sleep(900);


        Profile anotherProfile;
        AgentContainer anotherContainer;
        AgentController agent;

        anotherProfile = new ProfileImpl(false);
        anotherContainer = runTime.createAgentContainer(anotherProfile);
        System.out.println("Starting up a Manager...");
        agent = anotherContainer.createNewAgent("Parking Manager", "Agents.Manager", null);
        agent.start();
        Thread.sleep(900);


        ParkingLot parking1 = new ParkingLot();
        anotherProfile = new ProfileImpl(false);
        anotherContainer = runTime.createAgentContainer(anotherProfile);
        System.out.println("Starting up a parking...");
        agent = anotherContainer.createNewAgent(parking1.getName(), "Agents.Parking", new Object[]{ parking1});
        agent.start();
        Thread.sleep(900);

        ParkingLot parking2 = new ParkingLot();
        System.out.println("Starting up a parking...");
        agent = anotherContainer.createNewAgent(parking2.getName(), "Agents.Parking", new Object[]{ parking2});
        agent.start();
        Thread.sleep(900);

        ParkingLot parking3 = new ParkingLot();
        System.out.println("Starting up a parking...");
        agent = anotherContainer.createNewAgent(parking3.getName(), "Agents.Parking", new Object[]{ parking3});
        agent.start();
        Thread.sleep(900);


        Profile driverProfile;
        AgentContainer driverContainer;
        AgentController driver;

        driverProfile = new ProfileImpl(false);
        driverContainer = runTime.createAgentContainer(driverProfile);
        System.out.println("Starting up a Driver...");
        driver = driverContainer.createNewAgent("Driver", "Agents.Car", null);
        driver.start();
        Thread.sleep(900);


    }
}