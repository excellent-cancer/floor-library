package gray.light.blog.business;

import gray.light.blog.entity.Blog;
import gray.light.blog.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BlogBo {

    private Blog blog;

    private List<Tag> tags;

}
