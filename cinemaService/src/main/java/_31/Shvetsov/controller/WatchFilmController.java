package _31.Shvetsov.controller;


import _31.Shvetsov.service.FilmProcessing;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WatchFilmController {

    private final FilmProcessing filmProcessing;

    @GetMapping(value = "/film/{filmId}/video", produces = "video/mp4")
    public ResponseEntity<Resource> getFilm(@PathVariable Integer filmId) {
        Resource resource = filmProcessing.GetFilmForWatch(filmId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("video/mp4"))
                .body(resource);
    }
    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok()
                .body("ok");
    }
}