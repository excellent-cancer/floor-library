package excellent.cancer.gray.light.web;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public final class ServerResponses {

    public static Mono<ServerResponse> allRightFromValue(Object value) {
        return ServerResponse.ok().body(BodyInserters.fromValue(
                ResponseMessage.success(value)
        ));
    }

}
