package gray.light.source.router;

import gray.light.source.handler.SourceHandler;
import gray.light.support.web.RequestSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class SourceRouter {

    private final SourceHandler sourceHandler;

    @Bean
    public RouterFunction<ServerResponse> upload() {
        RequestPredicate requestPredicate = RequestPredicates.
                GET("/local/upload").
                and(RequestPredicates.queryParam("path", StringUtils::hasText)).
                and(RequestPredicates.queryParam("suffix", StringUtils::hasText));
        HandlerFunction<ServerResponse> handler = this::uploadHandler;

        return RouterFunctions.route(requestPredicate, handler);
    }

    @Bean
    public RouterFunction<ServerResponse> delete() {
        RequestPredicate requestPredicate = RequestPredicates.
                GET("/local/delete").
                and(RequestPredicates.queryParam("link", StringUtils::hasText));
        HandlerFunction<ServerResponse> handler = this::deleteHandler;

        return RouterFunctions.route(requestPredicate, handler);
    }

    private Mono<ServerResponse> uploadHandler(ServerRequest request) {
        return RequestSupport.extract(request, sourceHandler::uploadLocalFile, SourceHandler.LOCAL_PATH, SourceHandler.SOURCE_SUFFIX);
    }

    private Mono<ServerResponse> deleteHandler(ServerRequest request) {
        return RequestSupport.extract(request, sourceHandler::deleteLocalFile, SourceHandler.LINK_SUFFIX);
    }


}
