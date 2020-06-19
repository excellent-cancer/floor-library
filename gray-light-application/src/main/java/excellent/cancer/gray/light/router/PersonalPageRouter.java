package excellent.cancer.gray.light.router;

import excellent.cancer.gray.light.handler.DetailsHandler;
import excellent.cancer.gray.light.handler.OwnerFavoritesHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.function.Predicate;

/**
 * 个人主页请求路由
 *
 * @author XyParaCrim
 */
@Configuration
public class PersonalPageRouter {

    @Bean
    public RouterFunction<ServerResponse> getOwnerDetails(DetailsHandler detailsHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/owner"),
                detailsHandler::ownerDetails);
    }

    @Bean
    public RouterFunction<ServerResponse> postFavoriteProject(OwnerFavoritesHandler favoritesHandler) {
        return RouterFunctions.route(RequestPredicates.POST("/owner/projects/add"),
                favoritesHandler::addFavoriteProject);
    }

    @Bean
    public RouterFunction<ServerResponse> postFavoriteDocument(OwnerFavoritesHandler favoritesHandler) {
        return RouterFunctions.route(RequestPredicates.POST("/owner/docs/add"),
                favoritesHandler::addFavoriteDocument);
    }

    @Bean
    public RouterFunction<ServerResponse> getFavoriteDocumentSet(OwnerFavoritesHandler favoritesHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/owner/docs"),
                favoritesHandler::queryFavoriteDocument);
    }

    @Bean
    public RouterFunction<ServerResponse> getFavoriteDocumentRepositoryTree(OwnerFavoritesHandler favoritesHandler) {
        return RouterFunctions.route(
                RequestPredicates.GET("/owner/docs/tree").
                        and(RequestPredicates.queryParam("id", Predicate.not(StringUtils::isEmpty))),
                favoritesHandler::queryFavoriteDocumentRepositoryTree
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getFavoriteDocumentChapterContent(OwnerFavoritesHandler favoritesHandler) {
        return RouterFunctions.route(
                RequestPredicates.GET("/owner/docs/chapter-content").
                        and(RequestPredicates.queryParam("id", Predicate.not(StringUtils::isEmpty))),
                favoritesHandler::queryFavoriteDocumentRepositoryTree
        );
    }

}
