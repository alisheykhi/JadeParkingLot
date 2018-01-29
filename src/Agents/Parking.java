package Agents;

import Objects.*;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Parking extends Agent{
    private ParkingLot agent;
    private AID[] managers;
    private ParkingStructure structure;
    private LogViewer log;
    private Map<String, Driver> reserved = new HashMap<>();
    @Override
    protected void setup() {

        agent = (ParkingLot) this.getArguments()[0];
        log = (LogViewer) this.getArguments()[1];
        agent.setAgentID(this.getAID());
        //System.out.println(getAID().getLocalName()+":\tHello! Parking "+agent.getName() + " is ready.");
        log.add(new String[]{"[Parking Agent]",getAID().getLocalName(), "Hello! Parking "+agent.getName() + " is ready" });
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

        structure = new ParkingStructure(agent.getName());
        structure.setRows(new String[]{Integer.toString(agent.getCapacity()), Integer.toString(agent.getCapacity()), Integer.toString(agent.getReserved())});
        addBehaviour(new SignUp());
        addBehaviour(new GetReservationInform());
        addBehaviour(new AcceptCar());
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
            log.add(new String[]{"[Parking Agent]",getAID().getLocalName(), "send register inform  to manager "+managers[0].getLocalName()});
            //System.out.println(agent.getName()+ ":\tsend register inform  to manager "+managers[0].getLocalName());
            myAgent.send(reg);
            done++;
        }
        public boolean done() {
            return done == 1;
        }
    }

    private class GetReservationInform extends CyclicBehaviour {
        Driver driver;
        public void action() {
            MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                    MessageTemplate.MatchConversationId("Reservation"));
            ACLMessage msg = this.myAgent.receive(mt);
            if (msg != null) {
                try {
                    driver =  (Driver) msg.getContentObject();
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
                if (agent.getAvailableSpace() > 0) {
                    log.add(new String[]{"[Parking Agent]", getAID().getLocalName(), "received reservation inform for " + driver.getName()});
                    //System.out.println(getAID().getLocalName()+":\treceived register inform from " + parking.getName());
                    reserved.put(driver.getName(),driver);
                }else{
                    log.add(new String[]{"[Parking Agent]", getAID().getLocalName(), "reject reservation inform for " + driver.getName()});
                }
            }
        }
    }

    private class AcceptCar extends CyclicBehaviour {
        Driver driver;
        public void action() {
            MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                    MessageTemplate.MatchConversationId("Accept"));
            ACLMessage msg = this.myAgent.receive(mt);
            if (msg != null) {
                try {
                    driver =  (Driver) msg.getContentObject();
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
                if (agent.getAvailableSpace() > 0 && reserved.containsKey(driver.getName())) {
                    log.add(new String[]{"[Parking Agent]", getAID().getLocalName(), "received Accept request from " + driver.getName()});
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent("Welcome to " + agent.getName() + " parking!");
                    myAgent.send(reply);
                    removeFromReserved(driver);

                }
                else{
                    log.add(new String[]{"[Parking Agent]", getAID().getLocalName(), "reject accept request for " + driver.getName()});
                }
            }
        }
        private void removeFromReserved(Driver driver){
            agent.setAvailableSpace(agent.getAvailableSpace()-1);
            reserved.remove(driver.getName(), driver);
            ACLMessage reg = new ACLMessage(ACLMessage.INFORM);
            reg.setConversationId("AcceptCar");
            for (AID manager : managers) {
                reg.addReceiver(manager);
            }
            try {
                reg.setContentObject(agent);
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.add(new String[]{"[Parking Agent]",getAID().getLocalName(), "send AcceptCar inform  to manager "+managers[0].getLocalName()});
            //System.out.println(agent.getName()+ ":\tsend register inform  to manager "+managers[0].getLocalName());
            myAgent.send(reg);
        }

    }
}

