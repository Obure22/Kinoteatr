package _31.Shvetsov.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "genres")
@Getter
@Setter
public class GenreDocument {
    @Id
    private Integer genre_id;

    @Indexed(unique = true)
    private String name;
}