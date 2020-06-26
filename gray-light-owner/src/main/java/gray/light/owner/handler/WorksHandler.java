package gray.light.owner.handler;

import gray.light.definition.entity.Scope;
import gray.light.owner.business.OwnerProjectFo;
import gray.light.support.error.ExtractRequestParamException;
import gray.light.support.web.RequestSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import perishing.constraint.jdbc.Page;
import reactor.core.publisher.Mono;

import static gray.light.support.web.ResponseToClient.failWithMessage;

/**
 * 此handler提供与个人works相关的操作
 *
 * @author XyParaCrim
 */
@CommonsLog
@Component
@RequiredArgsConstructor
public class WorksHandler {

    private static final Scope SCOPE = Scope.WORKS;

    private final OwnerHandler ownerHandler;

    private final OwnerProjectHandler ownerProjectHandler;

    /**
     * 查询所属者的works
     *
     * @param request 请求
     * @return 回复
     */
    public Mono<ServerResponse> queryWorks(ServerRequest request) {
        return ownerHandler.extractOwnerId(request, ownerId -> {
            Page page = RequestSupport.extract(request);

            return ownerProjectHandler.queryOwnerProject(ownerId, SCOPE, page);
        });
    }

    /**
     * 通过指定请求，添加works项目到owner-project中
     *
     * @param request 指定请求
     * @return 回复发布者
     */
    public Mono<ServerResponse> addWorksToOwnerProject(ServerRequest request) {
        return request.
                bodyToMono(OwnerProjectFo.class).
                flatMap(projectForm -> ownerProjectHandler.addOwnerProjectWithNormalize(projectForm, Scope.WORKS));
    }

}
