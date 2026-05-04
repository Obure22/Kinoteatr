package _31.Shvetsov.dto;

import java.util.List;

public record MovieResponse(
        Integer movie_id,
        String title,
        String description,
        Integer release_date,
        Integer duration_min,
        Integer age_rating,
        String poster_url,
        String movie_url,
        List<String> genres
) {
}
