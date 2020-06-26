package gray.light.document.service;

import gray.light.book.entity.BookCatalog;
import gray.light.book.entity.BookChapter;
import gray.light.book.service.BookService;
import gray.light.document.customizer.WorksDocumentCustomizer;
import gray.light.document.entity.*;
import gray.light.document.repository.DocumentCatalogsRepository;
import gray.light.document.repository.DocumentChapterRepository;
import gray.light.document.repository.DocumentRepository;
import gray.light.document.repository.WorksDocumentRepository;
import gray.light.owner.customizer.ProjectDetailsCustomizer;
import gray.light.owner.entity.OwnerProject;
import gray.light.owner.entity.ProjectDetails;
import gray.light.owner.service.OverallOwnerService;
import gray.light.owner.service.ProjectDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perishing.constraint.jdbc.Page;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;

/**
 * 提供对于文档之间的关系功能，例如：文件树、查询、删除等等
 *
 * @author XyParaCrim
 */
@Service
@CommonsLog
@RequiredArgsConstructor
public class DocumentRelationService {

    private final DocumentRepository documentRepository;

    private final DocumentCatalogsRepository catalogsRepository;

    private final DocumentChapterRepository chapterRepository;

    private final OverallOwnerService overallOwnerService;

    private final WorksDocumentRepository worksDocumentRepository;

    private final ProjectDetailsService projectDetailsService;

    private final BookService bookService;

    /**
     * 为works添加新文档
     *
     * @param documentOwnerProject 添加的新文档项目
     * @param projectId 要添加的项目Id
     * @return 是否创建并保存成功
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean addDocumentToWorks(OwnerProject documentOwnerProject, Long projectId) {
        if (!overallOwnerService.addProject(documentOwnerProject)) {
            throw new RuntimeException("Failed to add owner-project of document: " + documentOwnerProject);
        }

        WorksDocument document = WorksDocumentCustomizer.generate(documentOwnerProject, projectId);
        if (!worksDocumentRepository.save(document)) {
            throw new RuntimeException("Failed to apply relation between works and document: " + documentOwnerProject);
        }

        ProjectDetails projectDetails = ProjectDetailsCustomizer.
                ofNewBookFromOwner(documentOwnerProject.getId(), "");
        if (!projectDetailsService.addProjectDetails(projectDetails)) {
            throw new RuntimeException("Failed to add project details: " + documentOwnerProject);
        }

        return true;
    }

    /**
     * 根据文档仓库Id，返回属于此仓库的所有目录与章节
     *
     * @param documentId 文档仓库ID
     * @return 属于此仓库的所有目录与章节
     */
    @Transactional(readOnly = true)
    public Tuple2<List<BookCatalog>, List<BookChapter>> documentRepositoryCatalogAndChapter(long documentId) {
        List<BookCatalog> catalogs = bookService.bookCatalogs(documentId, Page.unlimited());
        List<BookChapter> chapters = bookService.bookChapters(documentId, Page.unlimited());

        return Tuples.of(catalogs, chapters);
    }


 /*   *//**
     * 为项目文档创建新根目录
     *
     * @param rootCatalog 需要创建的文档目录
     * @return 是否创建成功
     *//*
    @Transactional(rollbackFor = Exception.class)
    public boolean createRootCatalogForDocument(DocumentCatalog rootCatalog) {
        return catalogsRepository.saveIfMatchedProject(rootCatalog);
    }

    *//**
     * 为项目文档添加子目录
     *
     * @param catalog 子目录
     * @return 是否添加成功
     *//*
    @Transactional(rollbackFor = Exception.class)
    public boolean addCatalogForDocument(DocumentCatalog catalog) {
        return catalogsRepository.saveIfMatchedParentAndExcludeFolder(catalog, DocumentCatalogFolder.CHAPTER) &&
                catalogsRepository.updateByFolder(catalog.getParentUid(), DocumentCatalogFolder.CATALOG);
    }

    *//**
     * 为项目文档添加文档
     *
     * @param documentChapter 添加的文档
     * @return 是否添加成功
     *//*
    @Transactional(rollbackFor = Exception.class)
    public boolean addDocumentForCatalog(DocumentChapter documentChapter) {
        return chapterRepository.saveIfMatchedDocumentCatalogAndExcludeFolder(documentChapter, DocumentCatalogFolder.CATALOG) &&
                catalogsRepository.updateByFolder(documentChapter.getCatalogUid(), DocumentCatalogFolder.CHAPTER);
    }

    *//**
     * 获取所有状态为{@link DocumentStatus#SYNC}的文档
     *
     * @return 返回已同步的文档
     *//*
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public List<Document> allSyncDocument() {
        return documentRepository.findByStatus(DocumentStatus.SYNC);
    }

    *//**
     * 获取所有状态为{@link DocumentStatus#EMPTY}的文档
     *
     * @return 返回空文档
     *//*
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public List<Document> allEmptyDocument() {
        return documentRepository.findByStatus(DocumentStatus.EMPTY);
    }

    *//**
     * 批量更新文档，以及文档包含的目录、文章
     *
     * @param documents 文档
     * @param catalogs  目录
     * @param chapters  文章
     * @return 是否更新成功
     *//*
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateDocumentRepositories(List<Document> documents, List<DocumentCatalog> catalogs, List<DocumentChapter> chapters) {
        if (documents.isEmpty()) {
            return true;
        }

        if (!documentRepository.batchUpdateDocumentStatus(documents)) {
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

    *//**
     * 批量更新文档状态
     *
     * @param documents 文档
     * @return 是否更新成功
     *//*
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateDocumentStatus(List<Document> documents) {
        return documentRepository.batchUpdateDocumentStatus(documents);
    }*/

}
