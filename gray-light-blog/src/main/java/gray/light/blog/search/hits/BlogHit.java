package gray.light.blog.search.hits;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlogHit {

    public static final String ID_FIELD = "id";

    private Long id;

}
