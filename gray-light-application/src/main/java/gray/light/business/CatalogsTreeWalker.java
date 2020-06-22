package gray.light.business;

import gray.light.document.entity.DocumentCatalog;
import gray.light.document.entity.DocumentChapter;
import perishing.constraint.treasure.chest.CollectionsTreasureChest;

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
        this.catalogTable = CollectionsTreasureChest.asFlatMapByList(catalogs, DocumentCatalog::getParentUid);
        this.chapterTable = CollectionsTreasureChest.asFlatMapByList(chapters, DocumentChapter::getCatalogUid);
    }

    public ContainsCatalogCatalogBo walk() {
        if (catalogTable.containsKey(DocumentCatalog.ROOT)) {
            DocumentCatalog rootCatalog = catalogTable.get(DocumentCatalog.ROOT).get(0);
            CatalogBo[] catalogs = walkCatalogs(rootCatalog.getUid());

            return new ContainsCatalogCatalogBo(rootCatalog, catalogs);
        } else {
            return new ContainsCatalogCatalogBo();
        }
    }

    private CatalogBo[] walkCatalogs(String parentUid) {
        List<CatalogBo> seList = new LinkedList<>();
        List<DocumentCatalog> catalogs = catalogTable.getOrDefault(parentUid, Collections.emptyList());
        for (DocumentCatalog c : catalogs) {
            switch (c.getFolder()) {
                case CATALOG:
                    seList.add(new ContainsCatalogCatalogBo(c, walkCatalogs(c.getUid())));
                    break;
                case CHAPTER:
                    seList.add(new ContainsChapterCatalogBo(c, walkChapters(c.getUid())));
                    break;
                default:
                    seList.add(new ContainsCatalogCatalogBo(c));
            }
        }

        return seList.toArray(new CatalogBo[0]);
    }

    private ChapterBo[] walkChapters(String parentUid) {
        return chapterTable.getOrDefault(parentUid, Collections.emptyList()).
                stream().
                map(ChapterBo::new).
                toArray(ChapterBo[]::new);
    }

    public static ContainsCatalogCatalogBo walk(List<DocumentCatalog> catalogs, List<DocumentChapter> chapters) {
        return new CatalogsTreeWalker(catalogs, chapters).walk();
    }
}
