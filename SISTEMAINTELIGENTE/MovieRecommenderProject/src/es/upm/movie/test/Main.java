package es.upm.movie.test;

import es.upm.movie.agents.InterfaceAgent;
import es.upm.movie.agents.MovieDataAgent;
import es.upm.movie.agents.RecommendationAgent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class Main {

    public static void main(String[] args) {

        try {
            System.out.println("Iniciando plataforma JADE...");

            Runtime runtime = Runtime.instance();
            runtime.setCloseVM(true);

            Profile profile = new ProfileImpl();
            profile.setParameter(Profile.GUI, "true");

            AgentContainer mainContainer = runtime.createMainContainer(profile);

            System.out.println("Plataforma JADE iniciada.");

            AgentController movieDataAgent = mainContainer.createNewAgent(
                    "movieData",
                    MovieDataAgent.class.getName(),
                    null
            );

            AgentController recommendationAgent = mainContainer.createNewAgent(
                    "recommender",
                    RecommendationAgent.class.getName(),
                    null
            );

            AgentController interfaceAgent = mainContainer.createNewAgent(
                    "interface",
                    InterfaceAgent.class.getName(),
                    null
            );

            movieDataAgent.start();
            recommendationAgent.start();
            interfaceAgent.start();

            System.out.println("Agentes iniciados correctamente:");
            System.out.println("- movieData");
            System.out.println("- recommender");
            System.out.println("- interface");

        } catch (Exception e) {
            System.err.println("Error iniciando el sistema multiagente.");
            e.printStackTrace();
        }
    }
}