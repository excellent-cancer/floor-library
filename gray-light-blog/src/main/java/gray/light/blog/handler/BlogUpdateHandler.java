package gray.light.blog.handler;

import gray.light.blog.business.BlogFo;
import gray.light.blog.customizer.BlogCustomizer;
import gray.light.blog.entity.Blog;
import gray.light.blog.service.ReadableBlogService;
import gray.light.blog.service.WritableBlogService;
import gray.light.support.error.NormalizingFormException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static gray.light.support.web.ResponseToClient.allRightFromValue;
import static gray.light.support.web.ResponseToClient.failWithMessage;

/**
 * 提供修改处理的handler
 *
 * @author XyParaCrim
 */
@Slf4j
@RequiredArgsConstructor
public class BlogUpdateHandler {

    private final ReadableBlogService readableBlogService;

    private final WritableBlogService writableBlogService;

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

                    if (writableBlogService.addBlog(blog, content)) {
                        Optional<Blog> savedBlog = readableBlogService.findBlog(blog.getId());
                        if (savedBlog.isPresent()) {
                            return allRightFromValue(savedBlog.get());
                        }
                    }
                    return failWithMessage("Failed to add blog");
                });
    }

}
