package _31.Shvetsov.repository;

import _31.Shvetsov.document.Movie_GenreDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;
import java.util.List;

public interface Movie_Genre_Repository extends MongoRepository<Movie_GenreDocument, String> {
    @Query("{ 'movie_id' : ?0 }")
    List<Movie_GenreDocument> findByMovieId(Integer movie_id);

    @Query("{ 'genre_id' : ?0 }")
    List<Movie_GenreDocument> findByGenreId(Integer genre_id);

    @Query("{ 'genre_id' : { '$in' : ?0 } }")
    List<Movie_GenreDocument> findByGenreIdIn(Collection<Integer> genre_ids);
}
