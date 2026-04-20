package _31.Shvetsov.repository;

import _31.Shvetsov.document.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserDocument, Integer> {
}
