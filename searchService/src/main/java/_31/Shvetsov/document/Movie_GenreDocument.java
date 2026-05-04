package _31.Shvetsov.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "movies_genres")
@Getter
@Setter
public class Movie_GenreDocument {

    @Id
    private String id;

    private Integer movie_id;
    private Integer genre_id;
}
