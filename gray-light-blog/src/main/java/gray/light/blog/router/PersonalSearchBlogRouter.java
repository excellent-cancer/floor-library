package gray.light.blog.router;

import gray.light.blog.handler.BlogSearchHandler;
import gray.light.support.web.RequestParamTables;
import gray.light.support.web.RequestSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.*;

/**
 * 提供搜索的路由
 *
 * @author XyParaCrim
 */
@CommonsLog
@RequiredArgsConstructor
public class PersonalSearchBlogRouter {

    private final BlogSearchHandler blogSearchHandler;

    private static final RequestPredicate SEARCH_PATH_PREDICATE = RequestPredicates.GET("/owner/blog/search");

    private static final RequestPredicate REQUIRED_WORD = RequestPredicates.queryParam("word", StringUtils::hasText);

    private static final RequestPredicate REQUIRED_SCROLL_ID = RequestPredicates.queryParam("scroll", StringUtils::hasText);

    private static final RequestPredicate START_MOD = SEARCH_PATH_PREDICATE.and(RequestPredicates.queryParam("mode", "start"));

    private static final RequestPredicate KEEP_MOD = SEARCH_PATH_PREDICATE.and(RequestPredicates.queryParam("mode", "keep"));

    private static final RequestPredicate END_MOD = SEARCH_PATH_PREDICATE.and(RequestPredicates.queryParam("mode", "end"));

    @Bean
    public RouterFunction<ServerResponse> searchBlogs() {
        RequestPredicate predicate = START_MOD.and(REQUIRED_WORD);
        HandlerFunction<ServerResponse> handler = request -> RequestSupport.extract(
                request,
                blogSearchHandler::search,
                RequestParamTables.page(),
                BlogSearchHandler.SEARCH_PARAM
        );

        return RouterFunctions.route(predicate, handler);
    }

    @Bean
    public RouterFunction<ServerResponse> keepSearchBlogs() {
        RequestPredicate predicate = KEEP_MOD.and(REQUIRED_WORD).and(REQUIRED_SCROLL_ID);
        HandlerFunction<ServerResponse> handler = request -> RequestSupport.extract(
                request,
                blogSearchHandler::keepSearch,
                RequestParamTables.page(),
                BlogSearchHandler.SCROLL_ID,
                BlogSearchHandler.SEARCH_PARAM
        );

        return RouterFunctions.route(predicate, handler);
    }

    @Bean
    public RouterFunction<ServerResponse> endSearchBlogs() {
        RequestPredicate predicate = END_MOD.and(REQUIRED_SCROLL_ID);
        HandlerFunction<ServerResponse> handler = request -> RequestSupport.extract(
                request,
                blogSearchHandler::finishSearch,
                BlogSearchHandler.SCROLL_ID
        );

        return RouterFunctions.route(predicate, handler);
    }


}
