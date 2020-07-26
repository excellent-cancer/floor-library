package gray.light.blog.service;

import gray.light.blog.business.BlogBo;
import gray.light.blog.entity.Blog;
import gray.light.blog.entity.TagWithBlogId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class BlogUtils {

    static List<BlogBo> assemblyBlogAndTag(List<Blog> blogs, List<TagWithBlogId> tags) {
        Map<Long, BlogBo> blogMap = blogs.stream().collect(Collectors.toMap(Blog::getId, e -> new BlogBo(e, new ArrayList<>())));

        for (TagWithBlogId tag : tags) {
            if (blogMap.containsKey(tag.getBlogId())) {
                blogMap.get(tag.getBlogId()).getTags().add(tag);
            }
        }

        return new ArrayList<>(blogMap.values());
    }

}
