package excellent.cancer.gray.light.router;

import excellent.cancer.gray.light.handler.DetailsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author XyParaCrim
 */
@Configuration
public class HomepageRouter {

    @Bean
    public RouterFunction<ServerResponse> getOwnerDetails(DetailsHandler detailsHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/owner"),
                detailsHandler::ownerDetails);
    }

}
