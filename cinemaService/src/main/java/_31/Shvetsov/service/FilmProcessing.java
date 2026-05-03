package _31.Shvetsov.service;

import _31.Shvetsov.document.MovieDocument;
import _31.Shvetsov.repository.GenreRepository;
import _31.Shvetsov.repository.MovieRepository;
import _31.Shvetsov.repository.Movie_Genre_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class FilmProcessing {

    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;
    private final Movie_Genre_Repository movieGenreRepository;

    public Resource GetFilmForWatch(Integer id) {
        MovieDocument  movie = movieRepository.findById(id).orElseThrow(()->new RuntimeException("No such film"));

        Resource resource = new FileSystemResource(movie.getMovie_url());

        if (!resource.exists()) {
            throw new RuntimeException("Video file not found");
        }
        return resource;
    }
    public void ConvertFilmToDto() {

    }
}
