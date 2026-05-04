package _31.Shvetsov.controller;

import _31.Shvetsov.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/movies")
    public ResponseEntity<?> movies(@RequestParam(required = false) String query,
                                    @RequestParam(required = false) String genre,
                                    @RequestParam(required = false) Integer year,
                                    @RequestParam(required = false) String sort) {
        return ResponseEntity.ok(searchService.search(query, genre, year, sort));
    }

    @GetMapping("/movies/random")
    public ResponseEntity<?> random(@RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(searchService.random(limit));
    }

    @GetMapping("/movies/newest")
    public ResponseEntity<?> newest(@RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(searchService.newest(limit));
    }
}
