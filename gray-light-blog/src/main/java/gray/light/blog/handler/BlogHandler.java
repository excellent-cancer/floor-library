package gray.light.blog.handler;

import gray.light.blog.business.BlogFo;
import gray.light.blog.customizer.BlogCustomizer;
import gray.light.blog.entity.Blog;
import gray.light.blog.service.BlogService;
import gray.light.owner.handler.OwnerHandler;
import gray.light.support.error.NormalizingFormException;
import gray.light.support.web.RequestSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import perishing.constraint.jdbc.Page;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static gray.light.support.web.ResponseToClient.allRightFromValue;
import static gray.light.support.web.ResponseToClient.failWithMessage;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BlogHandler {

    private final OwnerHandler ownerHandler;

    private final BlogService blogService;

    public Mono<ServerResponse> queryBlogs(ServerRequest request) {
        return ownerHandler.extractOwnerId(request, ownerId -> {
            Page page = RequestSupport.extract(request);

            return allRightFromValue(blogService.findBlogs(ownerId, page));
        });
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
