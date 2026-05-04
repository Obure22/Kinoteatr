package _31.Shvetsov.service;

import _31.Shvetsov.document.CommentDocument;
import _31.Shvetsov.document.GenreDocument;
import _31.Shvetsov.document.MovieDocument;
import _31.Shvetsov.document.Movie_GenreDocument;
import _31.Shvetsov.dto.CommentRequest;
import _31.Shvetsov.dto.CommentResponse;
import _31.Shvetsov.dto.MovieRequest;
import _31.Shvetsov.dto.MovieResponse;
import _31.Shvetsov.repository.CommentRepository;
import _31.Shvetsov.repository.GenreRepository;
import _31.Shvetsov.repository.MovieRepository;
import _31.Shvetsov.repository.Movie_Genre_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CinemaCatalogService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final Movie_Genre_Repository movieGenreRepository;
    private final CommentRepository commentRepository;
    private final CommentModerationPublisher commentModerationPublisher;

    public List<MovieResponse> getCatalog() {
        return movieRepository.findAll().stream()
                .map(this::toMovieResponse)
                .toList();
    }

    public MovieResponse getMovie(Integer movieId) {
        return toMovieResponse(findMovie(movieId));
    }

    public MovieResponse addMovie(MovieRequest request) {
        MovieDocument movie = new MovieDocument();
        movie.setMovie_id(nextMovieId());
        fillMovie(movie, request);
        movieRepository.save(movie);
        replaceGenres(movie.getMovie_id(), request.genres());
        return getMovie(movie.getMovie_id());
    }

    public MovieResponse updateMovie(Integer movieId, MovieRequest request) {
        MovieDocument movie = findMovie(movieId);
        fillMovie(movie, request);
        movieRepository.save(movie);
        replaceGenres(movieId, request.genres());
        return getMovie(movieId);
    }

    public void deleteMovie(Integer movieId) {
        findMovie(movieId);
        movieGenreRepository.deleteByMovieId(movieId);
        commentRepository.findByMovieId(movieId).forEach(commentRepository::delete);
        movieRepository.deleteById(movieId);
    }

    public CommentResponse addComment(Integer movieId, CommentRequest request) {
        findMovie(movieId);
        if (request.user_id() == null || request.message_text() == null || request.message_text().isBlank()) {
            throw new IllegalArgumentException("user_id and message_text are required");
        }

        CommentDocument comment = new CommentDocument();
        comment.setComment_id(nextCommentId());
        comment.setMovie_id(movieId);
        comment.setUser_id(request.user_id());
        comment.setMessage_text(request.message_text());
        comment.setScore(request.score());
        comment.setStatus("PENDING");
        commentRepository.save(comment);

        if (!commentModerationPublisher.publish(comment.getComment_id())) {
            comment.setModeration_reason("RabbitMQ unavailable");
            commentRepository.save(comment);
        }

        return toCommentResponse(commentRepository.findById(comment.getComment_id()).orElse(comment));
    }

    public List<CommentResponse> getApprovedComments(Integer movieId) {
        findMovie(movieId);
        return commentRepository.findByMovieIdAndStatus(movieId, "APPROVED").stream()
                .map(this::toCommentResponse)
                .toList();
    }

    public List<CommentResponse> getAllComments(Integer movieId) {
        findMovie(movieId);
        return commentRepository.findByMovieId(movieId).stream()
                .map(this::toCommentResponse)
                .toList();
    }

    public void deleteComment(Integer commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new IllegalArgumentException("comment not found");
        }
        commentRepository.deleteById(commentId);
    }

    private void fillMovie(MovieDocument movie, MovieRequest request) {
        movie.setTitle(request.title());
        movie.setDescription(request.description());
        movie.setRelease_date(request.release_date());
        movie.setDuration_min(request.duration_min());
        movie.setAge_rating(request.age_rating());
        movie.setPoster_url(request.poster_url());
        movie.setMovie_url(request.movie_url());
    }

    private void replaceGenres(Integer movieId, List<String> names) {
        movieGenreRepository.deleteByMovieId(movieId);
        if (names == null) {
            return;
        }
        for (String name : names.stream().filter(value -> value != null && !value.isBlank()).distinct().toList()) {
            GenreDocument genre = genreRepository.findByNameIgnoreCase(name)
                    .orElseGet(() -> {
                        GenreDocument created = new GenreDocument();
                        created.setGenre_id(nextGenreId());
                        created.setName(name);
                        return genreRepository.save(created);
                    });
            Movie_GenreDocument link = new Movie_GenreDocument();
            link.setId(movieId + "_" + genre.getGenre_id());
            link.setMovie_id(movieId);
            link.setGenre_id(genre.getGenre_id());
            movieGenreRepository.save(link);
        }
    }

    private MovieResponse toMovieResponse(MovieDocument movie) {
        List<String> genres = movieGenreRepository.findByMovieId(movie.getMovie_id()).stream()
                .map(Movie_GenreDocument::getGenre_id)
                .map(genreRepository::findById)
                .flatMap(java.util.Optional::stream)
                .map(GenreDocument::getName)
                .toList();
        return new MovieResponse(movie.getMovie_id(), movie.getTitle(), movie.getDescription(), movie.getRelease_date(),
                movie.getDuration_min(), movie.getAge_rating(), movie.getPoster_url(), movie.getMovie_url(), genres);
    }

    private CommentResponse toCommentResponse(CommentDocument comment) {
        return new CommentResponse(comment.getComment_id(), comment.getMovie_id(), comment.getUser_id(),
                comment.getMessage_text(), comment.getScore(), comment.getStatus(), comment.getModeration_reason());
    }

    private MovieDocument findMovie(Integer movieId) {
        return movieRepository.findById(movieId).orElseThrow(() -> new IllegalArgumentException("movie not found"));
    }

    private Integer nextMovieId() {
        return movieRepository.findAll().stream().map(MovieDocument::getMovie_id).max(Comparator.naturalOrder()).orElse(0) + 1;
    }

    private Integer nextGenreId() {
        return genreRepository.findAll().stream().map(GenreDocument::getGenre_id).max(Comparator.naturalOrder()).orElse(0) + 1;
    }

    private Integer nextCommentId() {
        return commentRepository.findAll().stream().map(CommentDocument::getComment_id).max(Comparator.naturalOrder()).orElse(0) + 1;
    }
}
