package org.excellent.cancer.experimental.application.router;

import org.excellent.cancer.experimental.application.handler.HelloHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

/**
 * 测试函数式编程模型创建响应式restful服务
 *
 * @author XyParaCrim
 */
@Configuration
public class HelloRouter {

    @Bean
    public RouterFunction<ServerResponse> hello(HelloHandler helloHandler) {
        return RouterFunctions.
                route(RequestPredicates.GET("/"), helloHandler::hello);
    }

}
