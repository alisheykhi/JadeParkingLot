package Runner;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import Objects.Driver;

public class StartSystem {
    public static void main(String args[]) throws InterruptedException, StaleProxyException {
        Driver driver = new Driver();
        System.out.println(driver.getName());
        System.out.println(driver.getCar());
        System.out.println(driver.getDateIn());
        System.out.println(driver.getDateOut());
//        final Runtime runTime = Runtime.instance();
//        runTime.setCloseVM(true);
//        Profile mainProfile = new ProfileImpl(true);
//        AgentContainer mainContainer = runTime.createMainContainer(mainProfile);
//        AgentController rma = mainContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
//        rma.start();
//        Thread.sleep(900);
//
//
//        Profile anotherProfile;
//        AgentContainer anotherContainer;
//        AgentController agent;
//
//        anotherProfile = new ProfileImpl(false);
//        anotherContainer = runTime.createAgentContainer(anotherProfile);
//        System.out.println("Starting up a Manager...");
//        agent = anotherContainer.createNewAgent("manager", "Agents.Driver", null);
//        agent.start();
//        Thread.sleep(900);

    }
}