package gray.light.document.router;

import gray.light.document.handler.DocumentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.function.Predicate;

@Configuration
public class PersonalDocumentRouter {

    @Bean
    public RouterFunction<ServerResponse> addWorksDocument(DocumentHandler documentHandler) {
        return RouterFunctions.route(RequestPredicates.POST("/owner/docs"),
                documentHandler::createWorksDocument);
    }


    @Bean
    public RouterFunction<ServerResponse> getWorksDocument(DocumentHandler documentHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/owner/docs"),
                documentHandler::queryWorksDocument);
    }

    @Bean
    public RouterFunction<ServerResponse> getFavoriteDocumentRepositoryTree(DocumentHandler documentHandler) {
        return RouterFunctions.route(
                RequestPredicates.GET("/owner/docs/tree").
                        and(RequestPredicates.queryParam("id", Predicate.not(StringUtils::isEmpty))),
                documentHandler::queryDocumentRepositoryTree
        );
    }

}
