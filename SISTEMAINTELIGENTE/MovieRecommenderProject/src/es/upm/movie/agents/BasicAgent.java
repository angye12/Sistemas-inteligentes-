package es.upm.movie.agents;

import jade.core.Agent;

public class BasicAgent extends Agent {
	
    @Override
    protected void setup() {
        System.out.println("Hola, soy un agente JADE: " + getLocalName());
        System.out.println("Mi AID es: " + getAID());
    }
    
}
