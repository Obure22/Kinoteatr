package _31.Shvetsov.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@CompoundIndex(def = "{'movie_id': 1, 'user_id': 1}", unique = true)
@Document(collection = "comments")
@Getter
@Setter
public class CommentDocument {
    @Id
    private Integer comment_id;

    private String movie_id;
    private String user_id;
    private String message_text;
    private String score;

}