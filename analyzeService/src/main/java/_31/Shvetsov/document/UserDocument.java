package _31.Shvetsov.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter
@Setter
public class UserDocument {
    @Id
    private Integer user_id;

    @Indexed(unique = true)
    private String email;
    private String password;
    private String role;
}