package _31.Shvetsov.repository;

import _31.Shvetsov.document.MovieDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieRepository extends MongoRepository<MovieDocument, Integer> {
}