package excellent.cancer.gray.light.jdbc.repositories;

import excellent.cancer.gray.light.jdbc.entities.*;
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

    boolean batchSave(List<Document> documents);

    boolean batchUpdateDocumentStatus(List<Document> documents);

    List<Document> findByStatus(@Param("status") DocumentStatus status);

    // query for catalog

    DocumentCatalog saveCatalog(DocumentCatalog rootCatalog);

    Optional<DocumentCatalog> findCatalogById(@Param("id") Long id);

    boolean batchSaveCatalog(List<DocumentCatalog> catalogs);

    boolean saveCatalogIfMatchedProject(DocumentCatalog documentCatalog);

    boolean saveCatalogIfMatchedParentAndExcludeFolder(DocumentCatalog catalog, @Param("exclude") DocumentCatalogFolder exclude);

    boolean updateCatalogFolder(@Param("uid") String uid, @Param("folder") DocumentCatalogFolder folder);

    // query for chapter

    List<DocumentChapter> findChapterAll();

    boolean batchSaveChapter(List<DocumentChapter> chapters);

    boolean saveChapter(DocumentChapter documentChapter);

    boolean saveChapterIfMatchedDocumentCatalogAndExcludeFolder(DocumentChapter documentChapter, @Param("exclude") DocumentCatalogFolder exclude);
}
