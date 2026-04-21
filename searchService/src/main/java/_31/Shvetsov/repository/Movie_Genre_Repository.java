package _31.Shvetsov.repository;

import _31.Shvetsov.document.Movie_GenreDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Movie_Genre_Repository extends MongoRepository<Movie_GenreDocument, Integer> {
}
