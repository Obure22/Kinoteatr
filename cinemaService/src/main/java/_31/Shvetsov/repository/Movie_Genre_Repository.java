package _31.Shvetsov.repository;

import _31.Shvetsov.document.Movie_GenreDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface Movie_Genre_Repository extends MongoRepository<Movie_GenreDocument, String> {
    @Query("{ 'movie_id' : ?0 }")
    List<Movie_GenreDocument> findByMovieId(Integer movie_id);

    @Query("{ 'genre_id' : ?0 }")
    List<Movie_GenreDocument> findByGenreId(Integer genre_id);

    @Query(value = "{ 'movie_id' : ?0 }", delete = true)
    void deleteByMovieId(Integer movie_id);
}
