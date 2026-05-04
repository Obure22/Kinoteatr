package _31.Shvetsov.repository;

import _31.Shvetsov.document.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserDocument, Integer> {
    Optional<UserDocument> findByEmail(String email);
}
