package gray.light.blog.router;

import floor.file.storage.FileStorage;
import gray.light.blog.handler.BlogHandler;
import gray.light.blog.handler.TagHandler;
import gray.light.blog.service.BlogService;
import gray.light.blog.service.BlogSourceService;
import gray.light.support.web.RequestParamTables;
import gray.light.support.web.RequestSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.server.*;
import perishing.constraint.jdbc.Page;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import static gray.light.support.web.ResponseToClient.allRightFromValue;

/**
 * 关于Blog的http请求
 *
 * @author XyParaCrim
 */
@CommonsLog
@RequiredArgsConstructor
public class PersonalBlogRouter {

    private final BlogHandler blogHandler;

    private final TagHandler tagHandler;

    @Bean
    public RouterFunction<ServerResponse> test1(FileStorage fileStorage) {
        return RouterFunctions.route(RequestPredicates.GET("/upload"), request -> {
            Optional<String> path = request.queryParam("path");
            Optional<String> suffix = request.queryParam("suffix");

            if (path.isEmpty() || suffix.isEmpty()) {
                return ServerResponse.badRequest().build();
            }

            return allRightFromValue(fileStorage.upload( Path.of(path.get()), suffix.get()));
        });
    }

    @Bean
    public RouterFunction<ServerResponse> test2(FileStorage fileStorage) {
        return RouterFunctions.route(RequestPredicates.POST("/upload"), request -> {


            Optional<String> path = request.queryParam("path");
            Optional<String> suffix = request.queryParam("suffix");

            if (path.isEmpty() || suffix.isEmpty()) {
                return ServerResponse.badRequest().build();
            }

            return allRightFromValue(fileStorage.upload(Path.of(path.get()), suffix.get()));
        });
    }

    @Bean
    public RouterFunction<ServerResponse> test(BlogService blogService) {
        return RouterFunctions.route(RequestPredicates.GET("/owner/blogs"), request -> RequestSupport.extract(
                request,
                variables -> allRightFromValue(blogService.findBlogsPro(RequestParamTables.ownerId().get(variables), RequestParamTables.page().get(variables))),
                RequestParamTables.page(),
                RequestParamTables.ownerId()
        ));
    }

    /**
     * 创建一篇博客
     *
     * @return 路由方法
     */
    @Bean
    public RouterFunction<ServerResponse> addBlog() {
        RequestPredicate predicate = RequestPredicates.POST("/owner/blog");
        HandlerFunction<ServerResponse> handler = request -> RequestSupport.extract(
                request,
                blogHandler::queryBlogs,
                // 请求参数
                RequestParamTables.page(),
                RequestParamTables.ownerId()
        );

        return RouterFunctions.route(predicate, handler);
    }

    /**
     * 获取一篇博客
     *
     * @return 路由方法
     */
    @Bean
    public RouterFunction<ServerResponse> getBlog() {
        RequestPredicate predicate = RequestPredicates.GET("/owner/blog");
        HandlerFunction<ServerResponse> handler = request -> RequestSupport.extract(
                request,
                blogHandler::queryBlogs,
                // 请求参数
                RequestParamTables.page(),
                RequestParamTables.ownerId(),
                TagHandler.TAGS_PARAM
        );

        return RouterFunctions.route(predicate, handler);
    }

    /**
     * 获取所属者所有用到的标签，指定标签的所有博客
     *
     * @return 路由方法
     */
    @Bean
    public RouterFunction<ServerResponse> getOwnerAllTags() {
        RequestPredicate predicate = RequestPredicates.
                GET("/owner/tag").
                and(RequestPredicates.queryParam("ownerId", StringUtils::hasText));
        HandlerFunction<ServerResponse> handler = request -> RequestSupport.extract(request,
                tagHandler::getOwnerAllTags,
                RequestParamTables.page(),
                RequestParamTables.ownerId());

        return RouterFunctions.route(predicate, handler);
    }

    @Bean
    public RouterFunction<ServerResponse> getBlogDetails() {
        RequestPredicate predicate = RequestPredicates.GET("/owner/blog/details")
                .and(RequestPredicates.queryParam("id", StringUtils::hasText));
        HandlerFunction<ServerResponse> handler = request -> RequestSupport.extract(request, blogHandler::blogDetails, RequestParamTables.id());


        return RouterFunctions.route(predicate, handler);
    }

}
