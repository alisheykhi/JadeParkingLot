package Runner;

import Objects.LogViewer;
import Objects.ParkingLot;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.StringDlg;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;


public class StartSystem {
    static LogViewer log;
    public static void main(String args[]) throws InterruptedException, StaleProxyException {
        log = new LogViewer();
        log.setVisible(true);
        final Runtime runTime = Runtime.instance();
        runTime.setCloseVM(true);
        Profile mainProfile = new ProfileImpl(true);
        AgentContainer mainContainer = runTime.createMainContainer(mainProfile);
        AgentController rma = mainContainer.createNewAgent("rma", "jade.tools.rma.rma", null);

        log.add(new String[]{"[Runner Object]","Runner", "Starting Up RMA"});
        rma.start();
        Thread.sleep(900);


        Profile anotherProfile;
        AgentContainer anotherContainer;
        AgentController agent;

        anotherProfile = new ProfileImpl(false);
        anotherContainer = runTime.createAgentContainer(anotherProfile);
        log.add(new String[]{"[Runner Object]","Runner", "Starting up parking manager"});
        //System.out.println("Starting up a Manager...");
        agent = anotherContainer.createNewAgent("Parking Manager", "Agents.Manager", new Object[]{log});
        agent.start();
        Thread.sleep(900);


        ParkingLot parking1 = new ParkingLot();
        anotherProfile = new ProfileImpl(false);
        anotherContainer = runTime.createAgentContainer(anotherProfile);

        log.add(new String[]{"[Runner Object]","Runner", "Starting up parking lot "+parking1.getName()});
        //System.out.println("Starting up a parking...");
        agent = anotherContainer.createNewAgent(parking1.getName(), "Agents.Parking", new Object[]{ parking1,log});
        agent.start();
        Thread.sleep(900);

        ParkingLot parking2 = new ParkingLot();
        //System.out.println("Starting up a parking...");

        log.add(new String[]{"[Runner Object]","Runner", "Starting up parking lot "+parking2.getName()});
        agent = anotherContainer.createNewAgent(parking2.getName(), "Agents.Parking", new Object[]{ parking2,log});
        agent.start();
        Thread.sleep(900);

        ParkingLot parking3 = new ParkingLot();
        //System.out.println("Starting up a parking...");
        log.add(new String[]{"[Runner Object]","Runner", "Starting up parking lot "+parking3.getName()});
        agent = anotherContainer.createNewAgent(parking3.getName(), "Agents.Parking", new Object[]{ parking3,log});
        agent.start();
        Thread.sleep(900);


        Profile driverProfile;
        AgentContainer driverContainer;
        AgentController driver;

        driverProfile = new ProfileImpl(false);
        driverContainer = runTime.createAgentContainer(driverProfile);
        for (int i = 0; i < 2; i++) {
            log.add(new String[]{"[Runner Object]","Runner", "Starting up a Driver" });
            //System.out.println("Starting up a Driver...");
            driver = driverContainer.createNewAgent("Driver"+i, "Agents.Car", new Object[]{log});
            driver.start();
            Thread.sleep(900);
        }
    }
}