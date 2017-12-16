package Agents;

import Objects.*;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.ArrayList;
import java.util.List;

public class Manager extends Agent {
    private List<ParkingLot> parkingList = new ArrayList<>();
    private ManagerGUI gui;
    @Override
    protected void setup() {

        gui = new ManagerGUI();
        gui.setVisible(true);
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Manager");
        sd.setName("ParkingLot");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println(getAID().getLocalName()+":\tHello! Manager \"" + getAID().getLocalName()+ "\" is ready.");

        addBehaviour(new RegisterParking());
    }

    private class RegisterParking extends CyclicBehaviour {
        ParkingLot parking;
        public void action() {
            MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                    MessageTemplate.MatchConversationId("register"));
            ACLMessage msg = this.myAgent.receive(mt);
            if (msg != null) {
                try {
                    parking =  (ParkingLot) msg.getContentObject();
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
                System.out.println(getAID().getLocalName()+":\treceived register inform from " + parking.getName());
                parkingList.add(parking);
                updateUI(parking);
                }
        }
    }

    private void updateUI(ParkingLot pl){
//        String[][] list = new String[parkingList.size()][3];
//        for (int i=0; i<parkingList.size();i++){
//            list[i][0] = parkingList.get(i).getName();
//            list[i][1] = Integer.toString(parkingList.get(i).getCapacity());
//            list[i][2] = Integer.toString(parkingList.get(i).getAvailableSpace());
//        }
        String[] list = {pl.getName(),Integer.toString(pl.getCapacity()),Integer.toString(pl.getAvailableSpace())};
        gui.setRows(list);
    }


}
