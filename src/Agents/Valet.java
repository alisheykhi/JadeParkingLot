package Agents;


import Objects.Driver;
import Objects.LogViewer;
import Objects.ParkingLot;
import Objects.ParkingStructure;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.util.concurrent.ThreadLocalRandom;

public class Valet extends Agent{
    private ParkingLot parking;
    private ParkingStructure structure;
    private LogViewer log;
    private AID[] valets;
    @Override
    protected void setup() {
        parking = (ParkingLot) this.getArguments()[0];
        log = (LogViewer) this.getArguments()[1];
        structure = (ParkingStructure) this.getArguments()[2];
        log.add(new String[]{"[Valet Agent]", getAID().getLocalName(), "Hello, this is " + this.getLocalName() });
        ACLMessage signIn = new ACLMessage(ACLMessage.INFORM);
        signIn.setConversationId("SingInValet");
        signIn.addReceiver(parking.getAgentID());
        this.send(signIn);
//        addBehaviour(new AcceptCar());
        addBehaviour(new CarReceived());
        addBehaviour(new CarOrdersAccepted());
    }


    private class CarReceived extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // CFP Message received. Process it
                String title = msg.getContent();
                ACLMessage reply = msg.createReply();
                int randomCost = ThreadLocalRandom.current().nextInt(1, 9 + 1);
                if (randomCost != 0) {
                    reply.setPerformative(ACLMessage.PROPOSE);
                    reply.setContent(Integer.toString(randomCost));
                    log.add(new String[]{"[Valet Agent]", getAID().getLocalName(), "send propose to parking " + msg.getSender().getLocalName()+ "by cost: "+randomCost});
                } else {
                    reply.setPerformative(ACLMessage.REFUSE);
                    reply.setContent("not interested");
                    log.add(new String[]{"[Valet Agent]", getAID().getLocalName(), "send refuse to parking " + msg.getSender().getLocalName()});
                }
                myAgent.send(reply);

            }
        }
    }

    private class CarOrdersAccepted extends CyclicBehaviour {
        public void action() {
            Driver driver;
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // ACCEPT_PROPOSAL Message received. Process it
                try {
                    driver = (Driver) msg.getContentObject();
                    ACLMessage reply = msg.createReply();
                    if (!driver.getName().equals("")) {
                        reply.setPerformative(ACLMessage.INFORM);
                        log.add(new String[]{"[Valet Agent]", getAID().getLocalName(), "handle new car " + driver.getName()});
                        addBehaviour(new InsertCar());
                    } else {
                        reply.setPerformative(ACLMessage.FAILURE);
                        reply.setContent("Failed");
                    }
                    myAgent.send(reply);
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private class InsertCar extends Behaviour {
        int done = 0;

        public void action() {
            int random = structure.getFreeLot();
            System.out.println(random);
            done++;
        }

        public boolean done() {
            return done == 1;
        }
    }

}
