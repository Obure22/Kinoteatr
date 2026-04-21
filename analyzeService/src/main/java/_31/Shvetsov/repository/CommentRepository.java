package _31.Shvetsov.repository;

import _31.Shvetsov.document.CommentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<CommentDocument, Integer> {

}
