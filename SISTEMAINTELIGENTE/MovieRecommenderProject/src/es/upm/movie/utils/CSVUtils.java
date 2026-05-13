package es.upm.movie.utils;

import es.upm.movie.model.Movie;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {

    public static List<Movie> readMovies(String filePath) {
        List<Movie> movies = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {

                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length == 5) {
                    String title = parts[0];
                    String genre = parts[1];
                    int year = Integer.parseInt(parts[2]);
                    double rating = Double.parseDouble(parts[3]);
                    int duration = Integer.parseInt(parts[4]);

                    Movie movie = new Movie(title, genre, year, rating, duration);
                    movies.add(movie);
                }
            }

        } catch (Exception e) {
            System.err.println("Error leyendo el fichero CSV: " + e.getMessage());
            e.printStackTrace();
        }

        return movies;
    }
}
