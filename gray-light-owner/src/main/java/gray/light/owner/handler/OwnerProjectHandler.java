package gray.light.owner.handler;

import gray.light.definition.entity.Scope;
import gray.light.owner.business.OwnerProjectFo;
import gray.light.owner.customizer.OwnerProjectCustomizer;
import gray.light.owner.entity.OwnerProject;
import gray.light.owner.service.OverallOwnerService;
import gray.light.support.error.NormalizingFormException;
import gray.light.support.web.RequestSupport;
import gray.light.support.web.ResponseToClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import perishing.constraint.jdbc.Page;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static gray.light.support.web.ResponseToClient.allRightFromValue;
import static gray.light.support.web.ResponseToClient.failWithMessage;

/**
 * 此handler提供与个人相关的操作，例如项目
 *
 * @author XyParaCrim
 */
@CommonsLog
@Component
@RequiredArgsConstructor
class OwnerProjectHandler {

    private final OverallOwnerService overallOwnerService;

    /**
     * 查询指定所属者项目
     *
     * @param ownerId 指定所属者
     * @param page 分页
     * @return 回复
     */
    Mono<ServerResponse> queryOwnerProject(Long ownerId, Page page) {
        return allRightFromValue(overallOwnerService.projects(ownerId, page));
    }

    /**
     * 查询所属者的指定scope的项目
     *
     * @param ownerId 所属者Id
     * @param scope 范围
     * @param page 分页
     * @return Response of Publisher
     */
    Mono<ServerResponse> queryOwnerProject(Long ownerId, Scope scope, Page page) {
        return allRightFromValue(overallOwnerService.projects(ownerId, scope, page));
    }

    /**
     * 通过指定owner-project表单，添加新owner-project
     *
     * @param ownerProjectForm 通过指定owner-project表单
     * @param scope 添加到范围
     * @return 回复发布者
     */
    Mono<ServerResponse> addOwnerProjectWithNormalize(OwnerProjectFo ownerProjectForm, Scope scope) {
        try {
            ownerProjectForm.normalize();
        } catch (NormalizingFormException e) {
            log.error(e);
            return failWithMessage(e.getMessage());
        }

        OwnerProject project = OwnerProjectCustomizer.fromForm(ownerProjectForm, scope);

        return addOwnerProject(project);
    }

    /**
     * 添加指定owner-project
     *
     * @param project 指定owner-project
     * @return 回复发布者
     */
    private Mono<ServerResponse> addOwnerProject(OwnerProject project) {
        CompletableFuture<Optional<OwnerProject>> addProcessing = addOwnerProjectProcessing(project);

        return Mono.fromFuture(addProcessing).
                flatMap(
                        addedProject ->
                                addedProject.
                                        map(ResponseToClient::allRightFromValue).
                                        orElseGet(() -> failWithMessage("Failed to add favorite " + project.getScope() + " project."))
                );
    }

    private CompletableFuture<Optional<OwnerProject>> addOwnerProjectProcessing(OwnerProject project) {
        return CompletableFuture.supplyAsync(
                () ->
                        overallOwnerService.addProject(project) ?
                                overallOwnerService.findProject(project.getId()) :
                                Optional.empty()
        );
    }

}
