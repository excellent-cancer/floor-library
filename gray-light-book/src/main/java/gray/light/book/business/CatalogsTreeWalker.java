package gray.light.book.business;

import gray.light.book.customizer.BookCatalogCustomizer;
import gray.light.book.entity.BookCatalog;
import gray.light.book.entity.BookChapter;
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

    private final Map<String, List<BookCatalog>> catalogTable;

    private final Map<String, List<BookChapter>> chapterTable;

    public CatalogsTreeWalker(List<BookCatalog> catalogs, List<BookChapter> chapters) {
        this.catalogTable = CollectionsTreasureChest.asFlatMapByList(catalogs, BookCatalog::getParentUid);
        this.chapterTable = CollectionsTreasureChest.asFlatMapByList(chapters, BookChapter::getCatalogUid);
    }

    public ContainsCatalogCatalogBo walk() {
        if (catalogTable.containsKey(BookCatalogCustomizer.ROOT)) {
            BookCatalog rootCatalog = catalogTable.get(BookCatalogCustomizer.ROOT).get(0);
            CatalogBo[] catalogs = walkCatalogs(rootCatalog.getUid());

            return new ContainsCatalogCatalogBo(rootCatalog, catalogs);
        } else {
            return new ContainsCatalogCatalogBo();
        }
    }

    private CatalogBo[] walkCatalogs(String parentUid) {
        List<CatalogBo> seList = new LinkedList<>();
        List<BookCatalog> catalogs = catalogTable.getOrDefault(parentUid, Collections.emptyList());
        for (BookCatalog c : catalogs) {
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

    public static ContainsCatalogCatalogBo walk(List<BookCatalog> catalogs, List<BookChapter> chapters) {
        return new CatalogsTreeWalker(catalogs, chapters).walk();
    }
}
