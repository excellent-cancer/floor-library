package excellent.cancer.gray.light.shared.entities;

import excellent.cancer.gray.light.jdbc.entities.DocumentCatalog;
import lombok.Getter;

public class ContainsChapterCatalogSE extends CatalogSE {

    @Getter
    private final ChapterSE[] chapters;

    public ContainsChapterCatalogSE(DocumentCatalog catalog, ChapterSE[] chapters) {
        super(catalog);
        this.chapters = chapters;
    }

    public ContainsChapterCatalogSE() {
        super(null);
        this.chapters = new ChapterSE[0];
    }
}
