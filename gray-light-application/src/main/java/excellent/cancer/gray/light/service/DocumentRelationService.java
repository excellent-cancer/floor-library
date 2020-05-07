package excellent.cancer.gray.light.service;

import excellent.cancer.gray.light.jdbc.entities.*;
import excellent.cancer.gray.light.jdbc.repositories.DocumentCatalogsRepository;
import excellent.cancer.gray.light.jdbc.repositories.DocumentChapterRepository;
import excellent.cancer.gray.light.jdbc.repositories.DocumentRepository;
import lombok.NonNull;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 提供对于文档之间的关系功能，例如：文件树、查询、删除等等
 *
 * @author XyParaCrim
 */
@Service
@CommonsLog
public class DocumentRelationService {

    /**
     * 为项目添加新文档
     *
     * @param document 添加的新文档
     * @return 是否创建并保存成功
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean createDocumentForProject(Document document) {
        return documentRepository.saveIfMatchedProject(document);
    }

    /**
     * 为项目文档创建新根目录
     *
     * @param rootCatalog 需要创建的文档目录
     * @return 是否创建成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createRootCatalogForDocument(DocumentCatalog rootCatalog) {
        return catalogsRepository.saveIfMatchedProject(rootCatalog);
    }

    /**
     * 为项目文档添加子目录
     *
     * @param catalog 子目录
     * @return 是否添加成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addCatalogForDocument(DocumentCatalog catalog) {
        return catalogsRepository.saveIfMatchedParentAndExcludeFolder(catalog, DocumentCatalogFolder.CHAPTER) &&
                catalogsRepository.updateByFolder(catalog.getParentUid(), DocumentCatalogFolder.CATALOG);
    }

    /**
     * 为项目文档添加文档
     *
     * @param documentChapter 添加的文档
     * @return 是否添加成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addDocumentForCatalog(DocumentChapter documentChapter) {
        return chapterRepository.saveIfMatchedDocumentCatalogAndExcludeFolder(documentChapter, DocumentCatalogFolder.CATALOG) &&
                catalogsRepository.updateByFolder(documentChapter.getCatalogUid(), DocumentCatalogFolder.CHAPTER);
    }

    /**
     * 获取所有状态为{@link DocumentStatus#SYNC}的文档
     *
     * @return 返回已同步的文档
     */
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public List<Document> allSyncDocument() {
        return documentRepository.findByStatus(DocumentStatus.SYNC);
    }

    /**
     * 获取所有状态为{@link DocumentStatus#EMPTY}的文档
     *
     * @return 返回空文档
     */
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public List<Document> allEmptyDocument() {
        return documentRepository.findByStatus(DocumentStatus.EMPTY);
    }

    /**
     * 批量更新文档，以及文档包含的目录、文章
     *
     * @param documents 文档
     * @param catalogs  目录
     * @param chapters  文章
     * @return 是否更新成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateDocumentRepositories(List<Document> documents, List<DocumentCatalog> catalogs, List<DocumentChapter> chapters) {
        if (documents.isEmpty()) {
            return true;
        }

        if (!documentRepository.batchSave(documents)) {
            return false;
        }

        if (catalogs.isEmpty()) {
            return true;
        }

        if (!catalogsRepository.batchSave(catalogs)) {
            return false;
        }

        if (chapters.isEmpty()) {
            return true;
        }

        return chapterRepository.batchSave(chapters);
    }

    /**
     * 批量更新文档状态
     *
     * @param documents 文档
     * @return 是否更新成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateDocumentStatus(List<Document> documents) {
        return documentRepository.batchUpdateDocumentStatus(documents);
    }

    private final DocumentRepository documentRepository;

    private final DocumentCatalogsRepository catalogsRepository;

    private final DocumentChapterRepository chapterRepository;

    @Autowired
    public DocumentRelationService(@NonNull RepositoryService repositoryService) {
        this.documentRepository = repositoryService.document();
        this.catalogsRepository = repositoryService.documentCatalogs();
        this.chapterRepository = repositoryService.documentChapter();
    }
}
