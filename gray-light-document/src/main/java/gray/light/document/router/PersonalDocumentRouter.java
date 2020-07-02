package gray.light.document.router;

import gray.light.document.handler.DocumentHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.function.Predicate;

/**
 * works文档提供的http请求
 *
 * @author XyParaCrim
 */
@RequiredArgsConstructor
public class PersonalDocumentRouter {

    private final DocumentHandler documentHandler;

    @Bean
    public RouterFunction<ServerResponse> addWorksDocument() {
        return RouterFunctions.route(RequestPredicates.POST("/owner/works/docs"),
                documentHandler::createWorksDocument);
    }

    @Bean
    public RouterFunction<ServerResponse> getWorksDocumentForOwner() {
        return RouterFunctions.route(RequestPredicates.GET("/owner/works/docs")
                        .and(RequestPredicates.queryParam("ownerId", StringUtils::hasText)),
                documentHandler::queryWorksDocument);
    }

    @Bean
    public RouterFunction<ServerResponse> getWorksDocumentForWorks() {
        return RouterFunctions.route(RequestPredicates.GET("/owner/works/docs")
                        .and(RequestPredicates.queryParam("worksId", StringUtils::hasText)),
                documentHandler::queryWorksDocumentByWorks);
    }

}
