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
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Parking extends Agent {
    private ParkingLot agent;
    private AID[] managers;
    private ParkingStructure structure;
    private LogViewer log;
    private Map<String, Driver> reserved = new HashMap<>();
    private Map<String, Driver> accepted = new HashMap<>();
    private Map<String, Boolean> parked = new HashMap<>();
    private Map<String, Boolean> valets = new HashMap<>();

    @Override
    protected void setup() {
        agent = (ParkingLot) this.getArguments()[0];
        log = (LogViewer) this.getArguments()[1];
        agent.setAgentID(this.getAID());
        //System.out.println(getAID().getLocalName()+":\tHello! Parking "+agent.getName() + " is ready.");
        log.add(new String[]{"[Parking Agent]", getAID().getLocalName(), "Hello! Parking " + agent.getName() + " is ready"});
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
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        structure = new ParkingStructure(agent.getName());
        structure.setRows(new String[]{Integer.toString(agent.getCapacity()), Integer.toString(agent.getCapacity()), Integer.toString(agent.getReserved())});

        for (int i = 0; i < 2; i++) {
            try {
                AgentController agentController = this.getContainerController().createNewAgent("valet_" + i + "_" + agent.getName(), "Agents.Valet", new Object[]{agent, log, structure});
                agentController.start();
                Thread.sleep(90);
            } catch (StaleProxyException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        addBehaviour(new SignUp());
        addBehaviour(new GetReservationInform());
        addBehaviour(new AcceptCar());
        addBehaviour(new SignInValet());
    }


    private class SignInValet extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                    MessageTemplate.MatchConversationId("SingInValet"));
            ACLMessage msg = this.myAgent.receive(mt);
            if (msg != null) {
                log.add(new String[]{"[Parking Agent]", getAID().getLocalName(), "Sign in new valet" + msg.getSender().getLocalName()});
                valets.put(msg.getSender().getName(), Boolean.TRUE);
            }
        }
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
            log.add(new String[]{"[Parking Agent]", getAID().getLocalName(), "send register inform  to manager " + managers[0].getLocalName()});
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
                    driver = (Driver) msg.getContentObject();
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
                if (agent.getAvailableSpace() > 0) {
                    log.add(new String[]{"[Parking Agent]", getAID().getLocalName(), "received reservation inform for " + driver.getName()});
                    //System.out.println(getAID().getLocalName()+":\treceived register inform from " + parking.getName());
                    reserved.put(driver.getName(), driver);
                } else {
                    log.add(new String[]{"[Parking Agent]", getAID().getLocalName(), "reject reservation inform for " + driver.getName()});
                }
            }
        }
    }

    private class AcceptCar extends CyclicBehaviour {
        Driver driver;
        MessageTemplate mt;

        public void action() {

            MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                    MessageTemplate.MatchConversationId("Accept"));
            ACLMessage msg = this.myAgent.receive(mt);
            if (msg != null) {
                try {
                    driver = (Driver) msg.getContentObject();
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
                    accepted.put(driver.getName(), driver);
                    parked.put(driver.getName(), Boolean.FALSE);
                    addBehaviour(new toValet());


                }

            }

        }

        private void removeFromReserved(Driver driver) {
            agent.setAvailableSpace(agent.getAvailableSpace() - 1);
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
            log.add(new String[]{"[Parking Agent]", getAID().getLocalName(), "send AcceptCar inform  to manager " + managers[0].getLocalName()});
            //System.out.println(agent.getName()+ ":\tsend register inform  to manager "+managers[0].getLocalName());
            myAgent.send(reg);
        }

    }

    private class toValet extends Behaviour {
        Driver driver;
        MessageTemplate mt;
        private AID bestValet; // The agent who provides the best offer
        private float bestPrice = 10;  // The best offered price
        private int repliesCnt = 0; // The counter of replies from valet agents
        private int step = 0;
        int avaiValet = 0;

        public void action() {

            switch (step) {
                case 0:
                    ACLMessage valetreq = new ACLMessage(ACLMessage.CFP);
                    valetreq.setConversationId("AcceptCar");

                    for (Map.Entry<String, Boolean> entry : valets.entrySet()) {
                        String key = entry.getKey();
                        Boolean value = entry.getValue();
                        if (value) {
                            valetreq.addReceiver(new AID(key));
                            avaiValet++;
                        }
                    }

                    valetreq.setReplyWith("cfp" + System.currentTimeMillis());
                    myAgent.send(valetreq);
                    mt = MessageTemplate.and(MessageTemplate.MatchConversationId("AcceptCar"), MessageTemplate.MatchInReplyTo(valetreq.getReplyWith()));
                    step = 1;
                    break;
                case 1:

                    // Receive all proposals/refusals from valet agents
                    ACLMessage valetreply = myAgent.receive(mt);
                    if (valetreply != null) {
                        // Reply received
                        if (valetreply.getPerformative() == ACLMessage.PROPOSE) {
                            // This is an offer
                            float cost = Float.parseFloat(valetreply.getContent());
                            if ( cost < bestPrice) {
                                // This is the best offer at present
                                bestPrice = cost;
                                bestValet = valetreply.getSender();
                            }
                        }
                        repliesCnt++;
                        if (repliesCnt >= avaiValet) {
                            // We received all replies
                            step = 2;
                        }
                    }
                    break;
                case 2:
                    // Send the driver to the seller that provided the best offer
                    ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                    order.addReceiver(bestValet);
                    for (Map.Entry<String, Boolean> entry : parked.entrySet()) {
                        String key = entry.getKey();
                        Boolean value = entry.getValue();
                        if (!value) {
                            driver = accepted.get(key);
                            parked.replace(key, Boolean.TRUE);
                        }
                    }
                    try {

                        order.setContentObject(driver);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    order.setConversationId("AcceptCar");
                    order.setReplyWith("order" + System.currentTimeMillis());
                    myAgent.send(order);
                    // Prepare the template to get the purchase order reply
                    mt = MessageTemplate.and(MessageTemplate.MatchConversationId("AcceptCar"), MessageTemplate.MatchInReplyTo(order.getReplyWith()));
                    step = 3;
                    break;
                case 3:
                    // Receive the parking order reply
                    ACLMessage reply = myAgent.receive(mt);
                    if (reply != null) {
                        // Purchase order reply received
                        if (reply.getPerformative() == ACLMessage.INFORM) {
                            // parking successful.
                            log.add(new String[]{"[Parking Agent]", getAID().getLocalName(), "car " + driver.getName() + " parked at " + reply.getContent()});
                        }
                    }
                    step = 4;
                    break;
                case 4:
                    // Receive the parking order reply
                    //mt = MessageTemplate.and(MessageTemplate.MatchConversationId("ClaimCar"));
                    mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                    mt.MatchConversationId("ClaimCar");
                    ACLMessage exitReq = myAgent.receive(mt);
                    if (exitReq != null) {
                        // Purchase order reply received
                        if (exitReq.getPerformative() == ACLMessage.REQUEST) {
                            // parking successful.
                            log.add(new String[]{"[Parking Agent]", getAID().getLocalName(), "claim request from  " + driver.getName()});
                        }
                    }
                    step = 5;
                    break;
            }
        }
        public boolean done() {
            return ((step == 2 && bestValet == null) || step == 5);
        }
    }

}

