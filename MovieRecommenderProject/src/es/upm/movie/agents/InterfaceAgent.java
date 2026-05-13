package es.upm.movie.agents;

import es.upm.movie.model.RecommendationResult;
import es.upm.movie.model.UserPreference;
import es.upm.movie.utils.AgentUtils;
import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.io.Serializable;
import java.util.List;

public class InterfaceAgent extends Agent {

    private static final long serialVersionUID = 1L;
    
    public static final String SERVICE_TYPE = "interface";
    public static final String SERVICE_NAME = "Interface Service";

    @Override
    protected void setup() {
        System.out.println("InterfaceAgent iniciado: " + getLocalName());
        
        AgentUtils.registerService(this, SERVICE_NAME, SERVICE_TYPE);

        addBehaviour(new UserInteractionBehaviour());
    }

    private class UserInteractionBehaviour extends OneShotBehaviour {

        private static final long serialVersionUID = 1L;

        @Override
        public void action() {
            try {
                myAgent.doWait(2000);

                String genre = JOptionPane.showInputDialog(
                        null,
                        "Introduce el género preferido:\nSci-Fi, Action, Animation, Thriller, Romance, Drama",
                        "Preferencias del usuario",
                        JOptionPane.PLAIN_MESSAGE
                );

                String minYearText = JOptionPane.showInputDialog(
                        null,
                        "Introduce el año mínimo:",
                        "Preferencias del usuario",
                        JOptionPane.PLAIN_MESSAGE
                );

                String minRatingText = JOptionPane.showInputDialog(
                        null,
                        "Introduce la valoración mínima:",
                        "Preferencias del usuario",
                        JOptionPane.PLAIN_MESSAGE
                );

                String maxDurationText = JOptionPane.showInputDialog(
                        null,
                        "Introduce la duración máxima en minutos:",
                        "Preferencias del usuario",
                        JOptionPane.PLAIN_MESSAGE
                );

                if (genre == null || minYearText == null || minRatingText == null || maxDurationText == null) {
                    JOptionPane.showMessageDialog(null, "Operación cancelada.");
                    return;
                }

                int minYear = Integer.parseInt(minYearText);
                double minRating = Double.parseDouble(minRatingText);
                int maxDuration = Integer.parseInt(maxDurationText);

                UserPreference preference = new UserPreference(genre, minYear, minRating, maxDuration);

                AID recommendationAgent = AgentUtils.searchService(myAgent, RecommendationAgent.SERVICE_TYPE);

                if (recommendationAgent == null) {
                    JOptionPane.showMessageDialog(null, "No se ha encontrado el servicio de recomendación.");
                    return;
                }

                ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                request.addReceiver(recommendationAgent);
                request.setOntology("movie-recommendation");
                request.setLanguage(new SLCodec().getName());
                request.setContentObject((Serializable) preference);

                myAgent.send(request);

                System.out.println("InterfaceAgent envia preferencias al RecommendationAgent.");

                MessageTemplate template = MessageTemplate.or(
                        MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                        MessageTemplate.MatchPerformative(ACLMessage.FAILURE)
                );

                ACLMessage reply = myAgent.blockingReceive(template);

                if (reply.getPerformative() == ACLMessage.FAILURE) {
                    JOptionPane.showMessageDialog(null, reply.getContent(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                @SuppressWarnings("unchecked")
                List<RecommendationResult> results =
                        (List<RecommendationResult>) reply.getContentObject();

                mostrarResultados(results);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Error: año, valoración y duración deben ser valores numéricos.",
                        "Error de entrada",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        "Error inesperado: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }

        private void mostrarResultados(List<RecommendationResult> results) {
            StringBuilder sb = new StringBuilder();

            sb.append("RECOMENDACIONES DE PELÍCULAS\n");
            sb.append("====================================\n\n");

            if (results == null || results.isEmpty()) {
                sb.append("No se han encontrado recomendaciones para esas preferencias.");
            } else {
                int i = 1;
                for (RecommendationResult result : results) {
                    sb.append(i).append(". ");
                    sb.append(result.getMovie().toString()).append("\n");
                    sb.append("Score: ").append(result.getScore()).append("\n");
                    sb.append("Motivo: ").append(result.getReason()).append("\n");
                    sb.append("------------------------------------\n");
                    i++;
                }
            }

            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(700, 400));

            JOptionPane.showMessageDialog(
                    null,
                    scrollPane,
                    "Resultados de recomendación",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    @Override
    protected void takeDown() {
        System.out.println("InterfaceAgent finalizado.");
    }
}