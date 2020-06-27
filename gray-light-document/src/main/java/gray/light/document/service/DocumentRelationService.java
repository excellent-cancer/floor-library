package gray.light.document.service;

import gray.light.book.entity.BookCatalog;
import gray.light.book.entity.BookChapter;
import gray.light.book.service.BookService;
import gray.light.document.business.DocumentFo;
import gray.light.document.customizer.WorksDocumentCustomizer;
import gray.light.document.entity.WorksDocument;
import gray.light.document.repository.WorksDocumentRepository;
import gray.light.owner.entity.OwnerProject;
import gray.light.owner.entity.ProjectDetails;
import gray.light.owner.entity.ProjectStatus;
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

    private final OverallOwnerService overallOwnerService;

    private final WorksDocumentRepository worksDocumentRepository;

    private final ProjectDetailsService projectDetailsService;

    private final BookService bookService;

    /**
     * 为works添加新文档
     *
     * @param documentOwnerProject 添加的新文档项目
     * @param projectId            要添加的项目Id
     * @param documentFo            文档表单
     * @return 是否创建并保存成功
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean addDocumentToWorks(OwnerProject documentOwnerProject, Long projectId, DocumentFo documentFo) {
        if (!overallOwnerService.addProject(documentOwnerProject)) {
            throw new RuntimeException("Failed to add owner-project of document: " + documentOwnerProject);
        }

        WorksDocument document = WorksDocumentCustomizer.generate(documentOwnerProject, projectId);
        if (!worksDocumentRepository.save(document)) {
            throw new RuntimeException("Failed to apply relation between works and document: " + documentOwnerProject);
        }

        ProjectDetails projectDetails = documentFo.generate(documentOwnerProject);
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

    /**
     * 获取指定项目状态的详细
     *
     * @param status 项目状态
     * @param page   分页
     * @return 获取指定项目状态的详细
     */
    @Transactional(readOnly = true)
    public List<ProjectDetails> findProjectDetailsByStatus(ProjectStatus status, Page page) {
        return worksDocumentRepository.findProjectDetailsByStatus(status, page.nullable());
    }

    /**
     * 批量更新文档状态
     *
     * @param documents 文档
     * @return 是否更新成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateDocumentStatus(List<ProjectDetails> documents) {
        return worksDocumentRepository.batchUpdateProjectDetailsStatus(documents);
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
    public boolean batchSyncDocumentFromPending(List<ProjectDetails> documents, List<BookCatalog> catalogs, List<BookChapter> chapters) {
        if (documents.isEmpty()) {
            return true;
        }

        if (!batchUpdateDocumentStatus(documents)) {
            return false;
        }

        if (catalogs.isEmpty()) {
            return true;
        }

        if (!bookService.batchSaveBookCatalogs(catalogs)) {
            return false;
        }

        if (chapters.isEmpty()) {
            return true;
        }

        return bookService.batchSaveBookChapters(chapters);
    }

}
