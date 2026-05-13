package es.upm.movie.model;

import java.io.Serializable;

public class UserPreference implements Serializable {

    private static final long serialVersionUID = 1L;

    private String genre;
    private int minYear;
    private double minRating;
    private int maxDuration;

    public UserPreference(String genre, int minYear, double minRating, int maxDuration) {
        this.genre = genre;
        this.minYear = minYear;
        this.minRating = minRating;
        this.maxDuration = maxDuration;
    }

    public String getGenre() {
        return genre;
    }

    public int getMinYear() {
        return minYear;
    }

    public double getMinRating() {
        return minRating;
    }

    public int getMaxDuration() {
        return maxDuration;
    }
}