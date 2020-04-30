package excellent.cancer.gray.light.handler;

import excellent.cancer.gray.light.jdbc.entities.Document;
import excellent.cancer.gray.light.jdbc.entities.OwnerProject;
import excellent.cancer.gray.light.jdbc.entities.builder.DocumentBuilder;
import excellent.cancer.gray.light.service.DocumentRelationService;
import excellent.cancer.gray.light.service.SuperOwnerService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static excellent.cancer.gray.light.web.ResponseToClient.*;
import static excellent.cancer.gray.light.web.UnsatisfiedBodyExtractors.*;

/**
 * 此handler提供与个人相关的操作，例如项目、文档、博客等等
 *
 * @author XyParaCrim
 */
@CommonsLog
@Component
public class OwnerFavoritesHandler {

    private final SuperOwnerService superOwnerService;

    private final DocumentRelationService documentRelationService;

    @Autowired
    public OwnerFavoritesHandler(SuperOwnerService superOwnerService, DocumentRelationService documentRelationService) {
        this.superOwnerService = superOwnerService;
        this.documentRelationService = documentRelationService;
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
                flatMap(
                        body -> unsatisfiedChain().
                                chain("name", EXTRACT_NAME).
                                chain("description", EXTRACT_DEFAULT).
                                extractOrOther(body, () -> {
                                    OwnerProject ownerProject = OwnerProject.builder()
                                            .name((String) body.get("name"))
                                            .description((String) body.get("description"))
                                            .build();

                                    return Mono.fromFuture(CompletableFuture.supplyAsync(() -> superOwnerService.addProject(ownerProject)))
                                            .flatMap(isAdded -> isAdded ?
                                                    allRightFromValue(ownerProject) :
                                                    failWithMessage("Failed to add favorite project."));
                                })
                );
    }

    /**
     * 为超级所属者的项目添加一个文档
     *
     * @param request 服务请求
     * @return Response of Publisher
     */
    public Mono<ServerResponse> addFavoriteDocument(ServerRequest request) {
        return request.
                bodyToMono(Map.class).
                flatMap(
                        body -> unsatisfiedChain().
                                chain("title", EXTRACT_NAME).
                                chain("description", EXTRACT_DEFAULT).
                                chain("projectId", EXTRACT_LONG).
                                chain("repoUrl", EXTRACT_GIT).
                                extractOrOther(body, () -> {
                                    Document document = DocumentBuilder.
                                            buildNecessaryProperties(body).
                                            build();

                                    return Mono.fromFuture(CompletableFuture.supplyAsync(() -> documentRelationService.createDocumentForProject(document)))
                                            .flatMap(success -> success ?
                                                    allRightFromValue(document) :
                                                    failWithMessage("Failed to add favorite project.")
                                            );
                                })
                );
    }

}