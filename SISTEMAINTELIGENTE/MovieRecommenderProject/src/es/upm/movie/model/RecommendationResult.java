package es.upm.movie.model;

import java.io.Serializable;

public class RecommendationResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Movie movie;
    private int score;
    private String reason;

    public RecommendationResult(Movie movie, int score, String reason) {
        this.movie = movie;
        this.score = score;
        this.reason = reason;
    }

    public Movie getMovie() {
        return movie;
    }

    public int getScore() {
        return score;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return movie.toString() + "\nScore: " + score + "\nMotivo: " + reason;
    }
}