package gray.light.blog.router;

import gray.light.blog.handler.BlogHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class PersonalBlogRouter {

    private final BlogHandler blogHandler;

    @Bean
    public RouterFunction<ServerResponse> addBlog() {
        return RouterFunctions.route(RequestPredicates.POST("/owner/blog"),
                blogHandler::createBlog);
    }

    @Bean
    public RouterFunction<ServerResponse> getBlog() {
        return RouterFunctions.route(RequestPredicates.GET("/owner/blog"),
                blogHandler::queryBlogs);
    }

}
