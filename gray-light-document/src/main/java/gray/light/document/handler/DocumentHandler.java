package gray.light.document.handler;

import gray.light.book.handler.BookHandler;
import gray.light.definition.entity.Scope;
import gray.light.document.business.DocumentFo;
import gray.light.document.service.DocumentRelationService;
import gray.light.owner.customizer.OwnerProjectCustomizer;
import gray.light.owner.entity.OwnerProject;
import gray.light.owner.handler.OwnerHandler;
import gray.light.owner.service.OverallOwnerService;
import gray.light.support.error.NormalizingFormException;
import gray.light.support.web.RequestSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import perishing.constraint.jdbc.Page;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static gray.light.support.web.ResponseToClient.allRightFromValue;
import static gray.light.support.web.ResponseToClient.failWithMessage;

/**
 * @author XyParaCrim
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentHandler {

    private final DocumentRelationService documentRelationService;

    private final OverallOwnerService overallOwnerService;

    private final OwnerHandler ownerHandler;

    private final BookHandler bookHandler;

    /**
     * 为所属者的项目添加一个文档
     *
     * @param request 服务请求
     * @return Response of Publisher
     */
    public Mono<ServerResponse> createWorksDocument(ServerRequest request) {
        return request.
                bodyToMono(DocumentFo.class).
                flatMap(this::createWorksDocumentWithNormalize);
    }

    /**
     * 查询指定所属者的document
     *
     * @param request 服务请求
     * @return 回复
     */
    public Mono<ServerResponse> queryWorksDocument(ServerRequest request) {
        return ownerHandler.extractOwnerId(request, ownerId -> {
            Page page = RequestSupport.extract(request);

            return allRightFromValue(overallOwnerService.projects(ownerId, Scope.DOCUMENT, page));
        });
    }

    /**
     * 查询文档仓库的结构树
     *
     * @param request 服务请求
     * @return Response of Publisher
     */
    public Mono<ServerResponse> queryDocumentRepositoryTree(ServerRequest request) {
        // TODO 验证

        return bookHandler.queryBookTree(request);
    }



    private Mono<ServerResponse> createWorksDocumentWithNormalize(DocumentFo documentFo) {
        try {
            documentFo.normalize();
        } catch (NormalizingFormException e) {
            log.error(e.getMessage());
            return failWithMessage(e.getMessage());
        }

        OwnerProject documentProject = OwnerProjectCustomizer.fromForm(documentFo.getDocument(), Scope.DOCUMENT);

        return addWorksDocument(documentProject, documentFo.getWorksId());
    }

    private Mono<ServerResponse> addWorksDocument(OwnerProject documentProject, Long worksId) {
        CompletableFuture<Boolean> addProcessing = addWorksDocumentProcessing(documentProject, worksId);

        return Mono.fromFuture(addProcessing).
                flatMap(
                        success ->
                                success ?
                                        allRightFromValue(documentProject) :
                                        failWithMessage("Failed to add works document")
                );
    }

    private CompletableFuture<Boolean> addWorksDocumentProcessing(OwnerProject documentProject, Long worksId) {
        return CompletableFuture.supplyAsync(() -> documentRelationService.addDocumentToWorks(documentProject, worksId));
    }


}
