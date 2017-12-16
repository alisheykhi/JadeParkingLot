package Agents;

import Objects.ParkingLot;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

public class Parking extends Agent{
    private ParkingLot agent;
    private AID[] managers;
    @Override
    protected void setup() {
        agent = new ParkingLot(this.getAID());
        System.out.println(getAID().getLocalName()+":\tHello! Parking "+agent.getName() + " is ready.");
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Manager");
        sd.setName("parkingLot");
        template.addServices(sd);
        try {
            DFAgentDescription[] result = DFService.search(this, template);
            managers = new AID[result.length];
            for (int i = 0; i < result.length; ++i) {
                managers[i] = result[i].getName();
            }
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
        addBehaviour(new SignUp());
    }
    private class SignUp extends Behaviour {
        int done = 0;
        public void action() {
            ACLMessage reg = new ACLMessage(ACLMessage.INFORM);
            reg.setConversationId("register");
            for (AID manager : managers) {
                reg.addReceiver(manager);
            }
            try {
                reg.setContentObject(agent);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(agent.getName()+ ":\tsend register inform  to manager "+managers[0].getLocalName());
            myAgent.send(reg);
            done++;
        }
        public boolean done() {
            return done == 1;
        }
    }
}
