package excellent.cancer.gray.light.service;

import excellent.cancer.gray.light.jdbc.entities.DocumentCatalog;
import excellent.cancer.gray.light.jdbc.repositories.DocumentCatalogRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

/**
 * 提供关于文档之间的关系功能，例如：文件树、查询、删除等等
 *
 * @author XyParaCrim
 */
@Service
@CommonsLog
@ConditionalOnBean(NamedParameterJdbcOperations.class)
public class DocumentRelationService {

    private final UniqueOwnerService uniqueOwnerService;

    private final DocumentCatalogRepository documentCatalogRepository;

    @Autowired
    public DocumentRelationService(UniqueOwnerService uniqueOwnerService, DocumentCatalogRepository documentCatalogRepository) {
        this.uniqueOwnerService = uniqueOwnerService;
        this.documentCatalogRepository = documentCatalogRepository;
    }

    /**
     * 为一个项目创建一个新根文档
     *
     * @param projectId 需要创建文档的项目Id
     * @param title     项目标题
     * @return DocumentCatalog的发布者
     */
    public Mono<DocumentCatalog> createDocumentForProject(Long projectId, String title) {
        return uniqueOwnerService.
                project(projectId).
                handle((project, sink) -> {
                    if (project != null) {
                        // 创建一个根目录
                        DocumentCatalog root = DocumentCatalog.builderWithCreate().
                                title(title).
                                parentId(DocumentCatalog.ROOT).
                                groupId(projectId).
                                build();

                        CompletableFuture.
                                supplyAsync(() -> documentCatalogRepository.save(root)).
                                thenAccept(sink::next);
                    } else {
                        sink.error(new NoSuchElementException("No owner project: { id: " + projectId + " }"));
                    }
                });
    }

}
