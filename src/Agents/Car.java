package Agents;

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

public class Car extends Agent{
    private AID[] managers;
    private Driver driver ;
    private ParkingLot parkingLot;
    @Override
    protected void setup() {
        driver = new Driver();
        System.out.println(driver.getName()+" ("+driver.getCar()+") arrived.");
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

    private class getParkingName extends Behaviour{
        private int step = 0;
        private MessageTemplate mt;
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
                    System.out.println(driver.getName() + ":\tSend request (find parking lot) to manager " + managers[0].getLocalName());
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
                                 parkingLot= (ParkingLot) reply.getContentObject();
                            } catch (UnreadableException e) {
                                e.printStackTrace();
                            }
                            if (parkingLot.getName().equals("Unknown")){
                                System.out.println("there is no empty parking lot");
                                break;
                            }
                            System.out.println(driver.getName()+": reserved parking is "+ parkingLot.getName());
                        }
                        step = 2;
                    }
                    else {
                        block();
                    }
                    break;
            }

        }

        public boolean done(){
            return (step == 2);
        }
    }


}
