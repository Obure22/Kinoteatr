package _31.Shvetsov.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "movies_genres")
@Getter
@Setter
public class Movie_GenreDocument {

    Integer movie_id;
    Integer genre_id;
}