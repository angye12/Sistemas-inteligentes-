package es.upm.movie.agents;

import es.upm.movie.model.Movie;
import es.upm.movie.utils.AgentUtils;
import es.upm.movie.utils.CSVUtils;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.Serializable;
import java.util.List;

public class MovieDataAgent extends Agent {

    private static final long serialVersionUID = 1L;

    public static final String SERVICE_TYPE = "movie-data";
    public static final String SERVICE_NAME = "Movie Data Service";

    private List<Movie> movies;

    @Override
    protected void setup() {
        System.out.println("MovieDataAgent iniciado: " + getLocalName());

        movies = CSVUtils.readMovies("data/movies.csv");
        System.out.println("MovieDataAgent ha cargado " + movies.size() + " peliculas.");

        AgentUtils.registerService(this, SERVICE_NAME, SERVICE_TYPE);

        addBehaviour(new MovieDataBehaviour());
    }

    private class MovieDataBehaviour extends CyclicBehaviour {

        private static final long serialVersionUID = 1L;

        @Override
        public void action() {
            MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);

            ACLMessage msg = myAgent.blockingReceive(template);

            if (msg != null) {
                System.out.println("MovieDataAgent recibe solicitud de: " + msg.getSender().getLocalName());

                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.INFORM);

                try {
                    reply.setContentObject((Serializable) movies);
                    myAgent.send(reply);
                    System.out.println("MovieDataAgent envia lista de peliculas.");
                } catch (Exception e) {
                    e.printStackTrace();

                    reply.setPerformative(ACLMessage.FAILURE);
                    reply.setContent("Error enviando peliculas");
                    myAgent.send(reply);
                }
            }
        }
    }

    @Override
    protected void takeDown() {
        System.out.println("MovieDataAgent finalizado.");
    }
}