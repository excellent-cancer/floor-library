package excellent.cancer.gray.light.handler;

import excellent.cancer.gray.light.service.OwnerService;
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
@SuppressWarnings("unused")
public class DetailsHandler {

    private final OwnerService ownerService;

    @Autowired
    public DetailsHandler(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    // 导出的handlers

    public Mono<ServerResponse> ownerDetails(ServerRequest request) {
        return allRightFromValue(ownerService.getOwner());
    }


}
