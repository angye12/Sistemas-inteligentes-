package es.upm.movie.test;

import es.upm.movie.model.Movie;
import es.upm.movie.model.RecommendationResult;
import es.upm.movie.model.UserPreference;
import es.upm.movie.utils.CSVUtils;
import es.upm.movie.utils.RecommendationEngine;

import java.util.List;

public class TestRecommendation {

    public static void main(String[] args) {

        List<Movie> movies = CSVUtils.readMovies("data/movies.csv");

        UserPreference preference = new UserPreference("Sci-Fi", 2010, 8.0, 150);

        List<RecommendationResult> recommendations =
                RecommendationEngine.recommend(movies, preference);

        System.out.println("Recomendaciones encontradas: " + recommendations.size());
        System.out.println("-----------------------------------");

        for (RecommendationResult result : recommendations) {
            System.out.println(result);
            System.out.println("-----------------------------------");
        }
    }
}