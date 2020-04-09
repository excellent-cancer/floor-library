package org.excellent.cancer.experimental.application.router;

import org.excellent.cancer.experimental.application.handler.MessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class HomepageRouter {

    @Bean
    public RouterFunction<ServerResponse> ownerIntro(MessageHandler messageHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/owner"), messageHandler::ownerMessage);
    }

}
