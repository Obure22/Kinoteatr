package _31.Shvetsov.controller;

import _31.Shvetsov.dto.CommentRequest;
import _31.Shvetsov.dto.MovieRequest;
import _31.Shvetsov.service.CinemaCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CinemaController {

    private final CinemaCatalogService cinemaCatalogService;

    @GetMapping("/movies")
    public ResponseEntity<?> catalog() {
        return ResponseEntity.ok(cinemaCatalogService.getCatalog());
    }

    @GetMapping("/movies/{movieId}")
    public ResponseEntity<?> movie(@PathVariable Integer movieId) {
        return ResponseEntity.ok(cinemaCatalogService.getMovie(movieId));
    }

    @PostMapping("/movies")
    public ResponseEntity<?> addMovie(@RequestBody MovieRequest request) {
        return ResponseEntity.ok(cinemaCatalogService.addMovie(request));
    }

    @PutMapping("/movies/{movieId}")
    public ResponseEntity<?> updateMovie(@PathVariable Integer movieId, @RequestBody MovieRequest request) {
        return ResponseEntity.ok(cinemaCatalogService.updateMovie(movieId, request));
    }

    @DeleteMapping("/movies/{movieId}")
    public ResponseEntity<?> deleteMovie(@PathVariable Integer movieId) {
        cinemaCatalogService.deleteMovie(movieId);
        return ResponseEntity.ok(Map.of("deleted", movieId));
    }

    @PostMapping("/movies/{movieId}/comments")
    public ResponseEntity<?> addComment(@PathVariable Integer movieId, @RequestBody CommentRequest request) {
        return ResponseEntity.ok(cinemaCatalogService.addComment(movieId, request));
    }

    @GetMapping("/movies/{movieId}/comments")
    public ResponseEntity<?> comments(@PathVariable Integer movieId) {
        return ResponseEntity.ok(cinemaCatalogService.getApprovedComments(movieId));
    }

    @GetMapping("/movies/{movieId}/comments/all")
    public ResponseEntity<?> allComments(@PathVariable Integer movieId) {
        return ResponseEntity.ok(cinemaCatalogService.getAllComments(movieId));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer commentId) {
        cinemaCatalogService.deleteComment(commentId);
        return ResponseEntity.ok(Map.of("deleted", commentId));
    }
}
