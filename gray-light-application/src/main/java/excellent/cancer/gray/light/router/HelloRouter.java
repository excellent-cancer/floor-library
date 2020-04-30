package excellent.cancer.gray.light.router;

import excellent.cancer.gray.light.handler.OwnerFavoritesHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * 测试函数式编程模型创建响应式restful服务
 *
 * @author XyParaCrim
 */
@Configuration
public class HelloRouter {

/*    @Bean
    public RouterFunction<ServerResponse> hello(HelloHandler helloHandler) {
        return RouterFunctions.
                route(RequestPredicates.GET("/"), helloHandler::hello);
    }*/

    @Bean
    public RouterFunction<ServerResponse> helloWhat(OwnerFavoritesHandler helloHandler) {
        return RouterFunctions.
                route(RequestPredicates.POST("/"), helloHandler::addFavoriteProject);
    }

    @Bean
    public RouterFunction<ServerResponse> helloWhathh(OwnerFavoritesHandler helloHandler) {
        return RouterFunctions.
                route(RequestPredicates.POST("/add-document"), helloHandler::addFavoriteDocument);
    }


}
