package es.upm.movie.model;

import java.io.Serializable;

public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String genre;
    private int year;
    private double rating;
    private int duration;

    public Movie(String title, String genre, int year, double rating, int duration) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.rating = rating;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public double getRating() {
        return rating;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return title + " (" + year + ") - " + genre + " - Rating: " + rating + " - " + duration + " min";
    }
}