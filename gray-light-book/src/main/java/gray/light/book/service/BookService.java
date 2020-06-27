package gray.light.book.service;

import gray.light.book.entity.BookCatalog;
import gray.light.book.entity.BookChapter;
import gray.light.book.repository.BookCatalogRepository;
import gray.light.book.repository.BookChapterRepository;
import gray.light.owner.entity.ProjectDetails;
import gray.light.owner.service.ProjectDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perishing.constraint.jdbc.Page;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;

@Service
@CommonsLog
@RequiredArgsConstructor
public class BookService {

    private final BookCatalogRepository bookCatalogRepository;

    private final BookChapterRepository bookChapterRepository;

    private final ProjectDetailsService projectDetailsService;

    public List<BookCatalog> bookCatalogs(Long ownerProjectId, Page page) {
        return bookCatalogRepository.findByOwnerProjectId(ownerProjectId, page.nullable());
    }

    public List<BookChapter> bookChapters(Long ownerProjectId, Page page) {
        return bookChapterRepository.findByOwnerProjectId(ownerProjectId, page.nullable());
    }

    /**
     * 根据文档仓库Id，返回属于此仓库的所有目录与章节
     *
     * @param documentId 文档仓库ID
     * @return 属于此仓库的所有目录与章节
     */
    @Transactional(readOnly = true)
    public Tuple2<List<BookCatalog>, List<BookChapter>> catalogAndChapter(long documentId) {
        List<BookCatalog> catalogs = bookCatalogs(documentId, Page.unlimited());
        List<BookChapter> chapters = bookChapters(documentId, Page.unlimited());

        return Tuples.of(catalogs, chapters);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean batchSaveBookCatalogs(List<BookCatalog> catalogs) {
        return bookCatalogRepository.batchSave(catalogs);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean batchSaveBookChapters(List<BookChapter> chapters) {
        return bookChapterRepository.batchSave(chapters);
    }

    /**
     * 批量更新文档，以及文档包含的目录、文章
     *
     * @param projectDetails 文档
     * @param catalogs  目录
     * @param chapters  文章
     * @return 是否更新成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSyncBookFromPending(List<ProjectDetails> projectDetails, List<BookCatalog> catalogs, List<BookChapter> chapters) {
        if (projectDetails.isEmpty()) {
            return true;
        }

        if (!projectDetailsService.batchUpdateStatus(projectDetails)) {
            return false;
        }

        if (catalogs.isEmpty()) {
            return true;
        }

        if (!batchSaveBookCatalogs(catalogs)) {
            return false;
        }

        if (chapters.isEmpty()) {
            return true;
        }

        return batchSaveBookChapters(chapters);
    }

}
