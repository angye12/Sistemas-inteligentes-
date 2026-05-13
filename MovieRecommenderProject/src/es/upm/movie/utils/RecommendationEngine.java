package es.upm.movie.utils;

import es.upm.movie.model.Movie;
import es.upm.movie.model.RecommendationResult;
import es.upm.movie.model.UserPreference;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RecommendationEngine {

    public static List<RecommendationResult> recommend(List<Movie> movies, UserPreference preference) {
        List<RecommendationResult> results = new ArrayList<>();

        for (Movie movie : movies) {
            int score = 0;
            StringBuilder reason = new StringBuilder();

            if (movie.getGenre().equalsIgnoreCase(preference.getGenre())) {
                score += 40;
                reason.append("Coincide con el género preferido. ");
            }

            if (movie.getRating() >= preference.getMinRating()) {
                score += 30;
                reason.append("Tiene una valoración suficiente. ");
            }

            if (movie.getYear() >= preference.getMinYear()) {
                score += 20;
                reason.append("Cumple con el año mínimo indicado. ");
            }

            if (movie.getDuration() <= preference.getMaxDuration()) {
                score += 10;
                reason.append("Tiene una duración adecuada. ");
            }

            if (score > 0) {
                results.add(new RecommendationResult(movie, score, reason.toString()));
            }
        }

        results.sort(Comparator.comparingInt(RecommendationResult::getScore).reversed());

        if (results.size() > 5) {
            return new ArrayList<>(results.subList(0, 5));
        }

        return results;
    }
}