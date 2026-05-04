package _31.Shvetsov.controller;

import _31.Shvetsov.service.CommentAnalyzeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AnalyzeController {

    private final CommentAnalyzeService commentAnalyzeService;

    @PostMapping("/comments/{commentId}/moderate")
    public ResponseEntity<?> moderate(@PathVariable Integer commentId) {
        return ResponseEntity.ok(commentAnalyzeService.moderate(commentId));
    }

    @PostMapping("/comments/{commentId}/approve")
    public ResponseEntity<?> approve(@PathVariable Integer commentId) {
        return ResponseEntity.ok(commentAnalyzeService.approve(commentId));
    }

    @PostMapping("/comments/{commentId}/reject")
    public ResponseEntity<?> reject(@PathVariable Integer commentId, @RequestBody(required = false) Map<String, String> body) {
        return ResponseEntity.ok(commentAnalyzeService.reject(commentId, body == null ? null : body.get("reason")));
    }

    @GetMapping("/comments/pending")
    public ResponseEntity<?> pending() {
        return ResponseEntity.ok(commentAnalyzeService.pending());
    }

    @GetMapping("/movies/{movieId}/comments")
    public ResponseEntity<?> byMovie(@PathVariable Integer movieId) {
        return ResponseEntity.ok(commentAnalyzeService.byMovie(movieId));
    }
}
