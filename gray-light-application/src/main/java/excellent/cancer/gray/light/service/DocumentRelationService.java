package excellent.cancer.gray.light.service;

import excellent.cancer.gray.light.jdbc.entities.*;
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
        return documentRepository.saveCatalogIfMatchedProject(rootCatalog);
    }

    /**
     * 为项目文档添加子目录
     *
     * @param catalog 子目录
     * @return 是否添加成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addCatalogForDocument(DocumentCatalog catalog) {
        return documentRepository.saveCatalogIfMatchedParentAndExcludeFolder(catalog, DocumentCatalogFolder.CHAPTER) &&
                documentRepository.updateCatalogFolder(catalog.getParentUid(), DocumentCatalogFolder.CATALOG);
    }

    /**
     * 为项目文档添加文档
     *
     * @param documentChapter 添加的文档
     * @return 是否添加成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addDocumentForCatalog(DocumentChapter documentChapter) {
        return documentRepository.saveChapterIfMatchedDocumentCatalogAndExcludeFolder(documentChapter, DocumentCatalogFolder.CATALOG) &&
                documentRepository.updateCatalogFolder(documentChapter.getCatalogUid(), DocumentCatalogFolder.CHAPTER);
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public List<Document> allSyncDocument() {
        return documentRepository.findByStatus(DocumentStatus.SYNC);
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public List<Document> allEmptyDocument() {
        return documentRepository.findByStatus(DocumentStatus.EMPTY);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateDocumentRepositories(List<Document> documents, List<DocumentCatalog> catalogs, List<DocumentChapter> chapters) {
        return documentRepository.batchSave(documents) &&
                documentRepository.batchSaveCatalog(catalogs) &&
                documentRepository.batchSaveChapter(chapters);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateDocumentStatus(List<Document> documents) {
        return documentRepository.batchUpdateDocumentStatus(documents);
    }

    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentRelationService(@NonNull RepositoryService repositoryService) {
        this.documentRepository = repositoryService.document();
    }
}
