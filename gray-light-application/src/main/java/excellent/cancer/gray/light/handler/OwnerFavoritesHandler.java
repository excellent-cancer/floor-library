package excellent.cancer.gray.light.handler;

import excellent.cancer.gray.light.service.DocumentRelationService;
import excellent.cancer.gray.light.service.SuperOwnerService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static excellent.cancer.gray.light.web.ResponseToClient.unsatisfiedChain;
import static excellent.cancer.gray.light.web.UnsatisfiedBodyExtractors.EXTRACT_NAME;

/**
 * 此handler提供与个人相关的操作，例如项目、文档、博客等等
 *
 * @author XyParaCrim
 */
@CommonsLog
@Component
public class OwnerFavoritesHandler {

    private final DocumentRelationService documentRelationService;

    private final SuperOwnerService superOwnerService;

    @Autowired
    public OwnerFavoritesHandler(DocumentRelationService documentRelationService, SuperOwnerService superOwnerService) {
        this.documentRelationService = documentRelationService;
        this.superOwnerService = superOwnerService;
    }

    /**
     * 为超级所属者添加一个项目
     *
     * @param request 服务请求
     * @return Response of Publisher
     */
    public Mono<ServerResponse> addFavoriteProject(ServerRequest request) {
        return request.
                bodyToMono(Map.class).
                flatMap(body -> unsatisfiedChain().
                        chain("name", EXTRACT_NAME).
                        extractOrOther(body, () -> ServerResponse.ok().build()));
    }

}