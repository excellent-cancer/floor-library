package gray.light.blog.customizer;

import gray.light.blog.business.BlogFo;
import gray.light.blog.entity.Blog;

public final class BlogCustomizer {

    public static Blog of(BlogFo blogFo) {

        return Blog.builder().
                ownerId(blogFo.getOwnerId()).
                title(blogFo.getTitle()).
                build();

    }


}
