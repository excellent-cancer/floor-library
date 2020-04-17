package excellent.cancer.gray.light.service;

import excellent.cancer.gray.light.jdbc.entities.Document;
import excellent.cancer.gray.light.jdbc.entities.DocumentCatalog;
import lombok.NonNull;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 提供对于文档之间的关系功能，例如：文件树、查询、删除等等
 *
 * @author XyParaCrim
 */
@Service
@CommonsLog
public class DocumentRelationService {

    /**
     * 为项目创建新根文档
     *
     * @param rootCatalog 需要创建的文档目录
     * @return 是否创建成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createRootCatalogForProject(DocumentCatalog rootCatalog) {
        return repositoryService.
                ofDocumentCatalog().
                saveIfMatchedEmptyDocsProject(rootCatalog) &&
                repositoryService.
                        ofOwnerProject().
                        updateContainsDocs(rootCatalog.getProjectId(), true);
    }

    /**
     * 为项目添加子目录
     *
     * @param catalog 子目录
     * @return 是否添加成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addCatalogForProject(DocumentCatalog catalog) {
        return repositoryService.
                ofDocumentCatalog().
                saveIfMatchedCatalogAndExcludeFolder(catalog, DocumentCatalog.Folder.DOCS) &&
                repositoryService.
                        ofDocumentCatalog().
                        updateFolder(catalog.getProjectId(), DocumentCatalog.Folder.CATALOG);
    }

    /**
     * 为目录添加文档
     *
     * @param document 添加的文档
     * @return 是否添加成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addDocumentForCatalog(Document document) {
        return repositoryService.
                ofDocument().
                saveIfMatchedDocumentCatalogAndExcludeFolder(document, DocumentCatalog.Folder.CATALOG) &&
                repositoryService.
                        ofDocumentCatalog().
                        updateFolder(document.getCatalogId(), DocumentCatalog.Folder.DOCS);
    }

    private final RepositoryService repositoryService;

    @Autowired
    public DocumentRelationService(@NonNull RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }
}
