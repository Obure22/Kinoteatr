package _31.Shvetsov.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "movies")
@Getter
@Setter
public class MovieDocument {
    @Id
    private Integer movie_id;

    private String title;
    private String description;
    private Integer release_date;
    private Integer duration_min;
    private Integer age_rating;
    private String poster_url;
    private String movie_url;
}