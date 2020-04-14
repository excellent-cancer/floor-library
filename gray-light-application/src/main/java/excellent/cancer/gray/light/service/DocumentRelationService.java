package excellent.cancer.gray.light.service;

import excellent.cancer.gray.light.error.IllegalEntryStateException;
import excellent.cancer.gray.light.error.NoSuchEntityException;
import excellent.cancer.gray.light.jdbc.ReactiveJdbc;
import excellent.cancer.gray.light.jdbc.entities.Document;
import excellent.cancer.gray.light.jdbc.entities.DocumentCatalog;
import excellent.cancer.gray.light.jdbc.repositories.DocumentCatalogRepository;
import excellent.cancer.gray.light.jdbc.repositories.DocumentRepository;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 提供关于文档之间的关系功能，例如：文件树、查询、删除等等
 *
 * @author XyParaCrim
 */
@Service
@CommonsLog
@ConditionalOnBean(NamedParameterJdbcOperations.class)
public class DocumentRelationService {

    @NonNull
    private final UniqueOwnerService uniqueOwnerService;

    @NonNull
    @Getter
    private final DocumentCatalogRepository documentCatalogRepository;

    @NonNull
    @Getter
    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentRelationService(@NonNull UniqueOwnerService uniqueOwnerService, @NonNull DocumentCatalogRepository documentCatalogRepository,
                                   @NonNull DocumentRepository documentRepository) {
        this.uniqueOwnerService = uniqueOwnerService;
        this.documentCatalogRepository = documentCatalogRepository;
        this.documentRepository = documentRepository;
    }

    // Service Functions

    /**
     * 为一个项目创建一个新根文档
     *
     * @param rootCatalog 需要创建的文档目录
     * @return publisher of DocumentCatalog
     */
    public Mono<DocumentCatalog> createCatalogForProject(DocumentCatalog rootCatalog) {
        return uniqueOwnerService.
                project(rootCatalog.getProjectId()).
                flatMap(found -> {
                    if (found.isEmpty()) {
                        return Mono.error(new NoSuchEntityException("The entity was not found in repository: OwnerProject(id=" + rootCatalog.getProjectId() + ")"));
                    }

                    return found.get().getDocs().isEmpty() ?
                            ReactiveJdbc.reactive(() -> documentCatalogRepository.save(rootCatalog)) :
                            Mono.error(new IllegalEntryStateException("The OwnerProject#docs's size is not empty, expect empty size"));
                });
    }

    /**
     * 每一个目录创建一篇文档
     *
     * @param parentCatalog 创建文档的目录
     * @param document      创建的文档
     * @return publisher of Document
     */
    public Mono<Document> createDocumentForCatalog(DocumentCatalog parentCatalog, Document document) {
        return ReactiveJdbc.reactive(() ->
                documentCatalogRepository.findById(parentCatalog.getId())).
                handle((found, sink) -> {
                    if (found.isEmpty()) {
                        ReactiveJdbc.signalOnNotFoundEntity(parentCatalog, sink);
                        return;
                    }

                    if (found.get().getHasDocs()) {
                        ReactiveJdbc.transformToSink(ReactiveJdbc.reactive(() -> documentRepository.save(document)), sink);
                    } else {
                        sink.error(new IllegalEntryStateException("The DocumentCatalog#hasDocs value is 'false', expect 'true'"));
                    }
                });
    }

}
