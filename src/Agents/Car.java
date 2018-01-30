package Agents;

import Objects.LogViewer;
import Objects.ParkingLot;
import jade.core.AID;
import jade.core.Agent;
import Objects.Driver;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class Car extends Agent {
    private AID[] managers;
    private Driver driver;
    private ParkingLot parkingLot;
    private LogViewer log;

    @Override
    protected void setup() {
        driver = new Driver();
        log = (LogViewer) this.getArguments()[0];
        //System.out.println(driver.getName()+" ("+driver.getCar()+") arrived.");
        log.add(new String[]{"[Car Agent]", getAID().getLocalName(), "Hello! Driver " + driver.getName() + " (" + driver.getCar() + ") is ready"});
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Manager");
        sd.setName("ParkingLot");
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

        addBehaviour(new getParkingName());
    }

    private class getParkingName extends Behaviour {
        private int step = 0;
        private MessageTemplate mt;
        private MessageTemplate template;

        public void action() {
            switch (step) {
                case 0:
                    //send request to manager
                    ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
                    for (AID manager : managers) {
                        req.addReceiver(manager);
                    }

                    try {
                        req.setContentObject(driver);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    req.setReplyWith("REQUEST" + System.currentTimeMillis());
                    req.setConversationId("Find parking");
                    myAgent.send(req);
                    log.add(new String[]{"[Car Agent]", getAID().getLocalName(), "Send request (find parking lot) to manager " + managers[0].getLocalName()});
                    //System.out.println(driver.getName() + ":\tSend request (find parking lot) to manager " + managers[0].getLocalName());
                    mt = MessageTemplate.and(MessageTemplate.MatchConversationId("Find parking"),
                            MessageTemplate.MatchInReplyTo(req.getReplyWith()));
                    step = 1;
                    break;
                case 1:
                    // receive from manager
                    ACLMessage reply = myAgent.receive(mt);
                    if (reply != null) {
                        // Reply received
                        if (reply.getPerformative() == ACLMessage.INFORM) {
                            try {
                                parkingLot = (ParkingLot) reply.getContentObject();
                            } catch (UnreadableException e) {
                                e.printStackTrace();
                            }
                            if (parkingLot.getName().equals("Unknown")) {
                                System.out.println("there is no empty parking lot");
                                break;
                            }
                            log.add(new String[]{"[Car Agent]", getAID().getLocalName(), "reserved parking is " + parkingLot.getName()});
                            //System.out.println(driver.getName()+": reserved parking is "+ parkingLot.getName());
                        }
                        step = 2;
                    } else {
                        block();
                    }
                    break;
                case 2:
                    try {
                        Thread.sleep(ThreadLocalRandom.current().nextInt(2000, 15000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //send request to parking
                    ACLMessage parkingReq = new ACLMessage(ACLMessage.REQUEST);
                    parkingReq.addReceiver(parkingLot.getAgentID());
                    try {
                        parkingReq.setContentObject(driver);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    parkingReq.setReplyWith("REQUEST" + System.currentTimeMillis());
                    parkingReq.setConversationId("Accept");
                    myAgent.send(parkingReq);
                    log.add(new String[]{"[Car Agent]", getAID().getLocalName(), "Send Accept request to parking " + parkingLot.getName()});
                    //System.out.println(driver.getName() + ":\tSend request (find parking lot) to manager " + managers[0].getLocalName());
                    template = MessageTemplate.and(MessageTemplate.MatchConversationId("Accept"),
                            MessageTemplate.MatchInReplyTo(parkingReq.getReplyWith()));
                    step = 3;
                    break;
                case 3:
                    // receive from parking
                    ACLMessage Aceptreply = myAgent.receive(template);
                    if (Aceptreply != null) {
                        // Reply received
                        if (Aceptreply.getPerformative() == ACLMessage.INFORM) {
                            String inform = Aceptreply.getContent();
                            if (inform.contains("Welcome")) {
                                log.add(new String[]{"[Car Agent]", getAID().getLocalName(), "received inform from parking " + parkingLot.getName()});
                                break;
                            }
                            else{
                                log.add(new String[]{"[Car Agent]", getAID().getLocalName(), "reject inform from parking " + parkingLot.getName()});
                                break;
                            }

                            //System.out.println(driver.getName()+": reserved parking is "+ parkingLot.getName());
                        }
                        step = 5;
                    }
                    break;
//                case 4:
//                    // exit request
//
//                    try {
//                        Thread.sleep(ThreadLocalRandom.current().nextInt(10000, 100000));
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    ACLMessage exitReq = new ACLMessage(ACLMessage.REQUEST);
//                    exitReq.setConversationId("ClaimCar");
//                    try {
//                        exitReq.setContentObject(driver);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    exitReq.addReceiver(parkingLot.getAgentID());
//                    myAgent.send(exitReq);
//                    log.add(new String[]{"[Car Agent]", getAID().getLocalName(), "claim request to parking" + parkingLot.getName()});
//                    System.out.println("lololololo"+parkingLot.getName());
//                    step = 5;
//                    break;
            }
        }
        public boolean done() {
            return (step == 5);
        }
    }

}
