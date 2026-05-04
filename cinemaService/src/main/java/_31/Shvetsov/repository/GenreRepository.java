package _31.Shvetsov.repository;

import _31.Shvetsov.document.GenreDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface GenreRepository extends MongoRepository<GenreDocument, Integer> {
    Optional<GenreDocument> findByNameIgnoreCase(String name);
}
