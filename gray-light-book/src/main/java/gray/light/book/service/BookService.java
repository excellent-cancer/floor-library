package gray.light.book.service;

import gray.light.book.entity.BookCatalog;
import gray.light.book.entity.BookChapter;
import gray.light.book.repository.BookCatalogRepository;
import gray.light.book.repository.BookChapterRepository;
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
}
