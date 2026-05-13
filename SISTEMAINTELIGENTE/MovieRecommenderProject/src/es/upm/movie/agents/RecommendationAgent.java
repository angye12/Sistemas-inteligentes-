package es.upm.movie.agents;

import es.upm.movie.model.Movie;
import es.upm.movie.model.RecommendationResult;
import es.upm.movie.model.UserPreference;
import es.upm.movie.utils.AgentUtils;
import es.upm.movie.utils.RecommendationEngine;
import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.Serializable;
import java.util.List;

public class RecommendationAgent extends Agent {

    private static final long serialVersionUID = 1L;

    public static final String SERVICE_TYPE = "recommendation";
    public static final String SERVICE_NAME = "Recommendation Service";

    @Override
    protected void setup() {
        System.out.println("RecommendationAgent iniciado: " + getLocalName());

        AgentUtils.registerService(this, SERVICE_NAME, SERVICE_TYPE);

        addBehaviour(new RecommendationBehaviour());
    }

    private class RecommendationBehaviour extends CyclicBehaviour {

        private static final long serialVersionUID = 1L;

        @Override
        public void action() {

            MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage msg = myAgent.blockingReceive(template);

            if (msg != null) {
                try {
                    System.out.println("RecommendationAgent recibe preferencia de: "
                            + msg.getSender().getLocalName());

                    UserPreference preference = (UserPreference) msg.getContentObject();

                    AID movieDataAgent = AgentUtils.searchService(myAgent, MovieDataAgent.SERVICE_TYPE);

                    if (movieDataAgent == null) {
                        ACLMessage errorReply = msg.createReply();
                        errorReply.setPerformative(ACLMessage.FAILURE);
                        errorReply.setContent("No se ha encontrado el servicio movie-data.");
                        myAgent.send(errorReply);
                        return;
                    }

                    System.out.println("RecommendationAgent ha encontrado MovieDataAgent: "
                            + movieDataAgent.getLocalName());

                    ACLMessage requestMovies = new ACLMessage(ACLMessage.REQUEST);
                    requestMovies.addReceiver(movieDataAgent);
                    requestMovies.setOntology("movie-recommendation");
                    requestMovies.setLanguage(new SLCodec().getName());
                    requestMovies.setContent("GET_MOVIES");

                    myAgent.send(requestMovies);
                    System.out.println("RecommendationAgent solicita lista de peliculas.");

                    MessageTemplate responseTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                    ACLMessage movieReply = myAgent.blockingReceive(responseTemplate);

                    @SuppressWarnings("unchecked")
                    List<Movie> movies = (List<Movie>) movieReply.getContentObject();

                    System.out.println("RecommendationAgent ha recibido "
                            + movies.size() + " peliculas.");

                    List<RecommendationResult> results =
                            RecommendationEngine.recommend(movies, preference);

                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContentObject((Serializable) results);
                    myAgent.send(reply);

                    System.out.println("RecommendationAgent envia recomendaciones a InterfaceAgent.");

                } catch (Exception e) {
                    e.printStackTrace();

                    ACLMessage errorReply = msg.createReply();
                    errorReply.setPerformative(ACLMessage.FAILURE);
                    errorReply.setContent("Error calculando recomendaciones.");
                    myAgent.send(errorReply);
                }
            }
        }
    }

    @Override
    protected void takeDown() {
        System.out.println("RecommendationAgent finalizado.");
    }
}