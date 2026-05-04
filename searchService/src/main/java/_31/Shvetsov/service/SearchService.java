package _31.Shvetsov.service;

import _31.Shvetsov.document.GenreDocument;
import _31.Shvetsov.document.MovieDocument;
import _31.Shvetsov.document.Movie_GenreDocument;
import _31.Shvetsov.dto.MovieSearchResponse;
import _31.Shvetsov.repository.GenreRepository;
import _31.Shvetsov.repository.MovieRepository;
import _31.Shvetsov.repository.Movie_Genre_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final Movie_Genre_Repository movieGenreRepository;

    public List<MovieSearchResponse> search(String query, String genre, Integer year, String sort) {
        Set<Integer> genreMovieIds = null;
        if (genre != null && !genre.isBlank()) {
            List<Integer> genreIds = genreRepository.findAll().stream()
                    .filter(item -> item.getName() != null && item.getName().equalsIgnoreCase(genre))
                    .map(GenreDocument::getGenre_id)
                    .toList();
            genreMovieIds = movieGenreRepository.findByGenreIdIn(genreIds).stream()
                    .map(Movie_GenreDocument::getMovie_id)
                    .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
        }

        String normalizedQuery = query == null ? "" : query.toLowerCase(Locale.ROOT);
        Set<Integer> finalGenreMovieIds = genreMovieIds;

        return movieRepository.findAll().stream()
                .filter(movie -> normalizedQuery.isBlank()
                        || contains(movie.getTitle(), normalizedQuery)
                        || contains(movie.getDescription(), normalizedQuery))
                .filter(movie -> year == null || year.equals(movie.getRelease_date()))
                .filter(movie -> finalGenreMovieIds == null || finalGenreMovieIds.contains(movie.getMovie_id()))
                .sorted(comparator(sort))
                .map(this::toResponse)
                .toList();
    }

    public List<MovieSearchResponse> random(Integer limit) {
        int count = limit == null || limit < 1 ? 5 : limit;
        return movieRepository.findAll().stream()
                .sorted((left, right) -> Integer.compare(Math.abs(left.getMovie_id() * 31 % 97), Math.abs(right.getMovie_id() * 31 % 97)))
                .limit(count)
                .map(this::toResponse)
                .toList();
    }

    public List<MovieSearchResponse> newest(Integer limit) {
        int count = limit == null || limit < 1 ? 10 : limit;
        return movieRepository.findAll().stream()
                .sorted(Comparator.comparing(MovieDocument::getRelease_date, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(count)
                .map(this::toResponse)
                .toList();
    }

    private Comparator<MovieDocument> comparator(String sort) {
        if ("year_desc".equalsIgnoreCase(sort) || "newest".equalsIgnoreCase(sort)) {
            return Comparator.comparing(MovieDocument::getRelease_date, Comparator.nullsLast(Comparator.reverseOrder()));
        }
        if ("year_asc".equalsIgnoreCase(sort)) {
            return Comparator.comparing(MovieDocument::getRelease_date, Comparator.nullsLast(Comparator.naturalOrder()));
        }
        if ("title_desc".equalsIgnoreCase(sort)) {
            return Comparator.comparing(MovieDocument::getTitle, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)).reversed();
        }
        return Comparator.comparing(MovieDocument::getTitle, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
    }

    private boolean contains(String value, String query) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(query);
    }

    private MovieSearchResponse toResponse(MovieDocument movie) {
        List<String> genres = movieGenreRepository.findByMovieId(movie.getMovie_id()).stream()
                .map(Movie_GenreDocument::getGenre_id)
                .map(genreRepository::findById)
                .flatMap(Optional::stream)
                .map(GenreDocument::getName)
                .toList();
        return new MovieSearchResponse(movie.getMovie_id(), movie.getTitle(), movie.getDescription(), movie.getRelease_date(),
                movie.getDuration_min(), movie.getAge_rating(), movie.getPoster_url(), movie.getMovie_url(), genres);
    }
}
