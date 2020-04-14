package excellent.cancer.gray.light.service;

import excellent.cancer.gray.light.error.IllegalEntryStateException;
import excellent.cancer.gray.light.jdbc.ReactiveJdbc;
import excellent.cancer.gray.light.jdbc.entities.Document;
import excellent.cancer.gray.light.jdbc.entities.DocumentCatalog;
import excellent.cancer.gray.light.jdbc.entities.OwnerProject;
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

import java.util.Optional;

/**
 * 提供对于文档之间的关系功能，例如：文件树、查询、删除等等
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
        return uniqueOwnerService
                .matchedProject(OwnerProject.justIdProject(rootCatalog.getProjectId()))
                .flatMap(project ->
                        project.getDocs().isEmpty() ?
                                ReactiveJdbc.reactive(() -> documentCatalogRepository.save(rootCatalog)) :
                                Mono.error(new IllegalEntryStateException("The OwnerProject#docs's size is not empty, expect empty size"))
                );
    }

    /**
     * 每一个目录创建一篇文档
     *
     * @param parentCatalog 创建文档的目录
     * @param document      创建的文档
     * @return publisher of Document
     */
    public Mono<Document> createDocumentForCatalog(DocumentCatalog parentCatalog, Document document) {
        return matchedDocumentCatalog(parentCatalog)
                .flatMap(catalog ->
                        catalog.getHasDocs() ?
                                ReactiveJdbc.reactive(() -> documentRepository.save(document)) :
                                Mono.error(new IllegalEntryStateException("The DocumentCatalog#hasDocs value is 'false', expect 'true'"))
                );
    }

    /**
     * 根据Id查询文档目录
     *
     * @param catalog 请求目录
     * @return publisher of optional DocumentCatalog
     */
    public Mono<Optional<DocumentCatalog>> documentCatalog(DocumentCatalog catalog) {
        return ReactiveJdbc.reactive(() -> documentCatalogRepository.findById(catalog.getId()));
    }

    /**
     * 根据Id查询文档目录，只在查询到匹配项目时发布订阅
     *
     * @param catalog 请求目录
     * @return publisher of DocumentCatalog which will publish on matched
     */
    public Mono<DocumentCatalog> matchedDocumentCatalog(DocumentCatalog catalog) {
        return documentCatalog(catalog).flatMap(ReactiveJdbc.flatMapperIfPresent(catalog));
    }

}
