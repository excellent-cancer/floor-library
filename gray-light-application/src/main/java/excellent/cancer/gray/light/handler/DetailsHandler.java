package excellent.cancer.gray.light.handler;

import excellent.cancer.gray.light.service.SuperOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static excellent.cancer.gray.light.web.ResponseToClient.allRightFromValue;

/**
 * @author XyParaCrim
 */
@Component
public class DetailsHandler {

    private final SuperOwnerService superOwnerService;

    @Autowired
    public DetailsHandler(SuperOwnerService superOwnerService) {
        this.superOwnerService = superOwnerService;
    }

    // 导出的handlers

    @SuppressWarnings("unused")
    public Mono<ServerResponse> ownerDetails(ServerRequest request) {
        return allRightFromValue(superOwnerService.superOwner());
    }

}
