package gray.light.blog.customizer;

import gray.light.blog.business.BlogFo;
import gray.light.blog.entity.Blog;

import java.nio.file.Path;

public final class BlogCustomizer {

    public static Blog of(BlogFo blogFo) {

        return Blog.builder().
                ownerId(blogFo.getOwnerId()).
                title(blogFo.getTitle()).
                build();

    }

    public static Blog ofLocal(Path local, Long ownerId) {
        String name = local.getFileName().toString();

        return Blog.builder().
                ownerId(ownerId).
                title(name.substring(0, name.lastIndexOf("."))).
                build();
    }


}
