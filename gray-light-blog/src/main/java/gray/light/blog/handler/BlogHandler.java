package gray.light.blog.handler;

import gray.light.blog.business.BlogFo;
import gray.light.blog.customizer.BlogCustomizer;
import gray.light.blog.entity.Blog;
import gray.light.blog.entity.Tag;
import gray.light.blog.service.BlogService;
import gray.light.support.error.NormalizingFormException;
import gray.light.support.web.RequestParamTables;
import gray.light.support.web.RequestSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import perishing.constraint.jdbc.Page;
import perishing.constraint.treasure.chest.collection.FinalVariables;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static gray.light.support.web.RequestParamTables.ownerId;
import static gray.light.support.web.RequestParamTables.page;
import static gray.light.support.web.ResponseToClient.allRightFromValue;
import static gray.light.support.web.ResponseToClient.failWithMessage;

/**
 * @author XyParaCrim
 */
@Slf4j
@RequiredArgsConstructor
public class BlogHandler {

    private final BlogService blogService;

    public Mono<ServerResponse> queryBlogs(FinalVariables<String> params) {
        Page page = page().get(params);
        Long ownerId = ownerId().get(params);


        if (TagHandler.TAGS_PARAM.contains(params)) {
            List<Tag> tags = TagHandler.TAGS_PARAM.get(params);

            return allRightFromValue(blogService.findBlogsByTags(ownerId, tags, page));
        } else {
            return allRightFromValue(blogService.findBlogs(ownerId, page));
        }
    }

    public Mono<ServerResponse> createBlog(ServerRequest request) {
        return request.
                bodyToMono(BlogFo.class).
                flatMap(blogFo -> {
                    try {
                        blogFo.normalize();
                    } catch (NormalizingFormException e) {
                        log.error(e.getMessage());
                        return failWithMessage(e.getMessage());
                    }

                    byte[] content = blogFo.getContent().getBytes();
                    Blog blog = BlogCustomizer.of(blogFo);

                    if (blogService.addBlog(blog, content)) {
                        Optional<Blog> savedBlog = blogService.findBlog(blog.getId());
                        if (savedBlog.isPresent()) {
                            return allRightFromValue(savedBlog.get());
                        }
                    }
                    return failWithMessage("Failed to add blog");
                });
    }

}
