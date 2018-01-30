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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Manager extends Agent {
    private List<ParkingLot> parkingList = new ArrayList<>();
    private ManagerGUI gui;
    private LogViewer log;
    @Override
    protected void setup() {

        gui = new ManagerGUI();
        gui.setVisible(true);
        log = (LogViewer) this.getArguments()[0];
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
        log.add(new String[]{"[Manager Agent]",getAID().getLocalName(), "Hello! Manager "+getAID().getLocalName() + " is ready"});
        //System.out.println(getAID().getLocalName()+":\tHello! Manager \"" + getAID().getLocalName()+ "\" is ready.");

        addBehaviour(new RegisterParking());
        addBehaviour(new MapDriverToParking());
        addBehaviour(new changeReserved());
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
                log.add(new String[]{"[Manager Agent]",getAID().getLocalName(), "received register inform from " + parking.getName() });
                //System.out.println(getAID().getLocalName()+":\treceived register inform from " + parking.getName());
                parkingList.add(parking);
                createUIModel(parking);
                }
        }
    }

    private class MapDriverToParking extends CyclicBehaviour {
        ParkingLot parking;
        Driver driver;
        Location dummy = new Location();
        public void action() {
            double distance = 100000.00;
            double tempDistance;
            MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                    MessageTemplate.MatchConversationId("Find parking"));
            ACLMessage msg = this.myAgent.receive(mt);
            if (msg != null) {
                try {
                    driver = (Driver) msg.getContentObject();
                } catch (UnreadableException e) {
                    e.printStackTrace();
                    //System.err.println(getLocalName()+ " catched exception "+e.getMessage());
                }
                log.add(new String[]{"[Manager Agent]",getAID().getLocalName(), "received parking request from " + driver.getName() });
                //System.out.println(getAID().getLocalName()+":\treceived parking request from " + driver.getName());
                for (ParkingLot parkinglot: parkingList){
                    if((parkinglot.getAvailableSpace()-parkinglot.getReserved()) > 0 ){
                        tempDistance = dummy.distanceByLocation(driver.getLocation(), parkinglot.getLocation());
                        if (tempDistance < distance){
                            distance = tempDistance;
                            parking = parkinglot;
                        }
                    }
                }

                for (ParkingLot parkinglot: parkingList){
                    if (parking.getName().equals(parkinglot.getName())){
                        parkinglot.setReserved(parkinglot.getReserved()+1);
                        updateReserved(parkingList.indexOf(parkinglot),Integer.toString(parkinglot.getReserved()));
                        sendReservedRequest(parkinglot, driver);
                    }
                }
                if(distance == 100000.00){
                    parking = new ParkingLot("Unknown");
                }
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                try {
                    reply.setContentObject(parking);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myAgent.send(reply);
            }
        }
    }

    private void createUIModel(ParkingLot pl){
        String[] list = {pl.getName(),Integer.toString(pl.getCapacity()),Integer.toString(pl.getAvailableSpace()),Integer.toString(pl.getReserved())};
        gui.setRows(list);
    }

    private void updateReserved(int row, String count){
        gui.updateReseved(row ,count);
    }

    private void updateAvailableSpace(int row, String count){
        gui.updateCapacity(row ,count);
    }

    private void sendReservedRequest(ParkingLot parking, Driver driver){
        ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
        inform.setConversationId("Reservation");
        inform.addReceiver(parking.getAgentID());
        try {
            inform.setContentObject(driver);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.add(new String[]{"[Manager Agent]",getAID().getLocalName(), "send Reservation inform  to parking "+parking.getName()});
        this.send(inform);
    }

    private class changeReserved extends CyclicBehaviour {
        ParkingLot parking;
        public void action() {
            MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                    MessageTemplate.MatchConversationId("AcceptCar"));
            ACLMessage msg = this.myAgent.receive(mt);
            if (msg != null) {
                try {
                    parking = (ParkingLot) msg.getContentObject();
                } catch (UnreadableException e) {
                    e.printStackTrace();
                    //System.err.println(getLocalName()+ " catched exception "+e.getMessage());
                }
                log.add(new String[]{"[Manager Agent]",getAID().getLocalName(), "acceptRequest from  parking " + parking.getName() });
                //System.out.println(getAID().getLocalName()+":\treceived parking request from " + driver.getName());
                for (ParkingLot parkinglot: parkingList){
                    if (parking.getName().equals(parkinglot.getName())){
                        parkinglot.setReserved(parkinglot.getReserved()-1);
                        updateReserved(parkingList.indexOf(parkinglot),Integer.toString(parkinglot.getReserved()));
                        parkinglot.setAvailableSpace(parkinglot.getAvailableSpace()-1);
                        updateAvailableSpace(parkingList.indexOf(parkinglot),Integer.toString(parkinglot.getAvailableSpace()));
                    }
                }
            }
        }
    }


}
