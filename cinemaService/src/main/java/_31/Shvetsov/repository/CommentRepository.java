package _31.Shvetsov.repository;

import _31.Shvetsov.document.CommentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CommentRepository extends MongoRepository<CommentDocument, Integer> {
    @Query("{ 'movie_id' : ?0 }")
    List<CommentDocument> findByMovieId(Integer movie_id);

    @Query("{ 'movie_id' : ?0, 'status' : ?1 }")
    List<CommentDocument> findByMovieIdAndStatus(Integer movie_id, String status);
}
