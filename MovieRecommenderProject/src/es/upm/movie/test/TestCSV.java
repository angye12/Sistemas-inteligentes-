package es.upm.movie.test;

import es.upm.movie.model.Movie;
import es.upm.movie.utils.CSVUtils;

import java.util.List;

public class TestCSV {

    public static void main(String[] args) {

        List<Movie> movies = CSVUtils.readMovies("data/movies.csv");

        System.out.println("Peliculas cargadas: " + movies.size());

        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}