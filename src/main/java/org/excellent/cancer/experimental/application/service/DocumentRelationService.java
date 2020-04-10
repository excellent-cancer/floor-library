package org.excellent.cancer.experimental.application.service;

import lombok.extern.apachecommons.CommonsLog;
import org.excellent.cancer.experimental.application.jdbc.entities.ProjectDocumentCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.NoSuchElementException;

/**
 * 提供关于文档之间的关系功能，例如：文件树、查询、删除等等
 *
 * @author XyParaCrim
 */
@Service
@CommonsLog
@ConditionalOnBean(NamedParameterJdbcOperations.class)
public class DocumentRelationService {

    private final OwnerService ownerService;

    @Autowired
    public DocumentRelationService(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    public Mono<ProjectDocumentCatalog> createDocumentForProject(Long projectId, String title) {
        return ownerService.
                project(projectId).
                handle((optional, sink) -> {
                    if (optional.isPresent()) {
                        // OwnerProject project = optional.get();
                        ProjectDocumentCatalog catalog = new ProjectDocumentCatalog(0L, new Date(), new Date(), title,
                                new ProjectDocumentCatalog.CatalogResource(false, ""));
                        sink.next(catalog);
                    } else {
                        sink.error(new NoSuchElementException("No owner project: { id: " + projectId + " }"));
                    }
                });
    }

}
