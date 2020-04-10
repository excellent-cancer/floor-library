package excellent.cancer.gray.light.handler;

import excellent.cancer.gray.light.service.OwnerService;
import excellent.cancer.gray.light.web.ServerResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class MessageHandler {

    private OwnerService ownerService;

    @Autowired
    public MessageHandler(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    public Mono<ServerResponse> ownerMessage(ServerRequest request) {
        return ServerResponses.allRightFromValue(ownerService.getOwner());
    }


}
