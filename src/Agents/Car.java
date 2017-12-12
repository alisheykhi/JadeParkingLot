package Agents;

import jade.core.Agent;
import Objects.Driver;
public class Car extends Agent{
    @Override
    protected void setup() {
        Driver driver = new Driver();
    }
}
