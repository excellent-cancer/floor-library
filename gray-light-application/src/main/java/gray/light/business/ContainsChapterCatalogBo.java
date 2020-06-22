package gray.light.business;

import gray.light.document.entity.DocumentCatalog;
import lombok.Getter;

public class ContainsChapterCatalogBo extends CatalogBo {

    @Getter
    private final ChapterBo[] chapters;

    public ContainsChapterCatalogBo(DocumentCatalog catalog, ChapterBo[] chapters) {
        super(catalog);
        this.chapters = chapters;
    }

    public ContainsChapterCatalogBo() {
        super(null);
        this.chapters = new ChapterBo[0];
    }
}
