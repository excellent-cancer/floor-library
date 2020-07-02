package gray.light.owner.handler;

import gray.light.owner.business.OwnerDetailsBo;
import gray.light.owner.entity.Owner;
import gray.light.owner.service.OverallOwnerService;
import gray.light.support.web.RequestSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static gray.light.support.web.ResponseToClient.allRightFromValue;
import static gray.light.support.web.ResponseToClient.failWithMessage;

/**
 * 关于所属者相关基本资料
 *
 * @author XyParaCrim
 */
@CommonsLog
@RequiredArgsConstructor
public class OwnerHandler {

    private final OwnerProjectHandler ownerProjectHandler;

    private final OverallOwnerService overallOwnerService;

    /**
     * 获取所属者详细
     *
     * @param request 请求
     * @return 回复
     */
    public Mono<ServerResponse> ownerDetails(ServerRequest request) {
        return RequestSupport.extractOwnerId(request, ownerId -> {
            Optional<Owner> queryResult = overallOwnerService.findOwner(ownerId);

            return queryResult.isEmpty() ?
                    failWithMessage("The user does not exist: " + ownerId) :
                    allRightFromValue(OwnerDetailsBo.of(queryResult.get()));
        });
    }

    /**
     * 查询指定所属者项目
     *
     * @param request 请求
     * @return 回复
     */
    public Mono<ServerResponse> queryOwnerProject(ServerRequest request) {
        return RequestSupport.extractOwnerId(request, ownerId ->
                ownerProjectHandler.queryOwnerProject(ownerId, RequestSupport.extractPage(request)));
    }


}
