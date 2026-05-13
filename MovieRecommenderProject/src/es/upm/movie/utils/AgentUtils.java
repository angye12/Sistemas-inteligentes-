package es.upm.movie.utils;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class AgentUtils {

    public static void registerService(Agent agent, String serviceName, String serviceType) {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(agent.getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setName(serviceName);
        sd.setType(serviceType);

        dfd.addServices(sd);

        try {
            DFService.register(agent, dfd);
            System.out.println(agent.getLocalName() + " registrado en DF con servicio: " + serviceType);
        } catch (FIPAException e) {
            System.err.println("Error registrando servicio en DF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static AID searchService(Agent agent, String serviceType) {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(serviceType);
        template.addServices(sd);

        try {
            DFAgentDescription[] result = DFService.search(agent, template);

            if (result.length > 0) {
                return result[0].getName();
            }

        } catch (FIPAException e) {
            System.err.println("Error buscando servicio en DF: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}