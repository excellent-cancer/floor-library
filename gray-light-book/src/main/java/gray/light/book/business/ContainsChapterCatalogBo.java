package gray.light.book.business;

import gray.light.book.entity.BookCatalog;
import lombok.Getter;

public class ContainsChapterCatalogBo extends CatalogBo {

    @Getter
    private final ChapterBo[] chapters;

    public ContainsChapterCatalogBo(BookCatalog catalog, ChapterBo[] chapters) {
        super(catalog);
        this.chapters = chapters;
    }

    public ContainsChapterCatalogBo() {
        super(null);
        this.chapters = new ChapterBo[0];
    }
}
