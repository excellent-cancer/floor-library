package excellent.cancer.gray.light.shared;

import excellent.cancer.gray.light.jdbc.entities.DocumentCatalog;
import excellent.cancer.gray.light.jdbc.entities.DocumentChapter;
import excellent.cancer.gray.light.shared.entities.CatalogSE;
import excellent.cancer.gray.light.shared.entities.ChapterSE;
import excellent.cancer.gray.light.shared.entities.ContainsCatalogCatalogSE;
import excellent.cancer.gray.light.shared.entities.ContainsChapterCatalogSE;
import excellent.cancer.gray.light.utils.EntitySupport;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 根据数据查询的目录列表和章节列表，转换成树形结构的se对象
 *
 * @author XyParaCrim
 */
public class CatalogsTreeWalker {

    private final Map<String, List<DocumentCatalog>> catalogTable;

    private final Map<String, List<DocumentChapter>> chapterTable;

    public CatalogsTreeWalker(List<DocumentCatalog> catalogs, List<DocumentChapter> chapters) {
        this.catalogTable = EntitySupport.entityListToMapList(catalogs, DocumentCatalog::getParentUid);
        this.chapterTable = EntitySupport.entityListToMapList(chapters, DocumentChapter::getCatalogUid);
    }

    public ContainsCatalogCatalogSE walk() {
        if (catalogTable.containsKey(DocumentCatalog.ROOT)) {
            DocumentCatalog rootCatalog = catalogTable.get(DocumentCatalog.ROOT).get(0);
            CatalogSE[] catalogs = walkCatalogs(rootCatalog.getUid());

            return new ContainsCatalogCatalogSE(rootCatalog, catalogs);
        } else {
            return new ContainsCatalogCatalogSE();
        }
    }

    private CatalogSE[] walkCatalogs(String parentUid) {
        List<CatalogSE> seList = new LinkedList<>();
        List<DocumentCatalog> catalogs = catalogTable.getOrDefault(parentUid, Collections.emptyList());
        for (DocumentCatalog c : catalogs) {
            switch (c.getFolder()) {
                case CATALOG:
                    seList.add(new ContainsCatalogCatalogSE(c, walkCatalogs(c.getUid())));
                    break;
                case CHAPTER:
                    seList.add(new ContainsChapterCatalogSE(c, walkChapters(c.getUid())));
                    break;
                default:
                    seList.add(new ContainsCatalogCatalogSE(c));
            }
        }

        return seList.toArray(new CatalogSE[0]);
    }

    private ChapterSE[] walkChapters(String parentUid) {
        return chapterTable.getOrDefault(parentUid, Collections.emptyList()).
                stream().
                map(ChapterSE::new).
                toArray(ChapterSE[]::new);
    }

    public static ContainsCatalogCatalogSE walk(List<DocumentCatalog> catalogs, List<DocumentChapter> chapters) {
        return new CatalogsTreeWalker(catalogs, chapters).walk();
    }
}
