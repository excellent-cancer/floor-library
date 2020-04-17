package excellent.cancer.gray.light.jdbc.repositories;

import excellent.cancer.gray.light.jdbc.entities.Document;
import excellent.cancer.gray.light.jdbc.entities.DocumentCatalog;
import excellent.cancer.gray.light.jdbc.entities.DocumentCatalogFolder;
import excellent.cancer.gray.light.jdbc.entities.DocumentChapter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 项目文档repository，提供文档、文档目录、文档章节等的查询更新功能
 *
 * @author XyParaCrim
 */
@Mapper
public interface DocumentRepository {

    // query for document

    boolean saveIfMatchedProject(Document document);

    // query for catalog

    DocumentCatalog saveCatalog(DocumentCatalog rootCatalog);

    Optional<DocumentCatalog> findCatalogById(@Param("id") Long id);

    boolean saveCatalogIfMatchedProject(DocumentCatalog documentCatalog);

    boolean saveCatalogIfMatchedParentAndExcludeFolder(DocumentCatalog catalog, @Param("exclude") DocumentCatalogFolder exclude);

    boolean updateCatalogFolder(@Param("id") long id, @Param("folder") DocumentCatalogFolder folder);

    // query for chapter

    List<DocumentChapter> findChapterAll();

    boolean saveChapter(DocumentChapter documentChapter);

    boolean saveChapterIfMatchedDocumentCatalogAndExcludeFolder(DocumentChapter documentChapter, @Param("exclude") DocumentCatalogFolder exclude);
}
