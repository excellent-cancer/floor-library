package gray.light.document.router;

import gray.light.document.handler.DocumentBookHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.function.Predicate;

@RequiredArgsConstructor
public class PersonalDocumentBookRouter {

    private final DocumentBookHandler documentBookHandler;

    @Bean
    public RouterFunction<ServerResponse> getFavoriteDocumentRepositoryTree() {
        return RouterFunctions.route(
                RequestPredicates.GET("/owner/works/docs/tree").
                        and(RequestPredicates.queryParam("id", Predicate.not(StringUtils::isEmpty))),
                documentBookHandler::queryDocumentRepositoryTree
        );
    }
}
