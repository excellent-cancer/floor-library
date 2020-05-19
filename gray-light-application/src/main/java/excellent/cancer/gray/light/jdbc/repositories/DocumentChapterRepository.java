package excellent.cancer.gray.light.jdbc.repositories;

import excellent.cancer.gray.light.jdbc.entities.DocumentCatalogFolder;
import excellent.cancer.gray.light.jdbc.entities.DocumentChapter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DocumentChapterRepository {

    List<DocumentChapter> findAll();

    List<DocumentChapter> findByDocumentId(@Param("documentId") long documentId);

    boolean batchSave(@Param("chapters") List<DocumentChapter> chapters);

    boolean save(DocumentChapter documentChapter);

    boolean saveIfMatchedDocumentCatalogAndExcludeFolder(@Param("chapter") DocumentChapter documentChapter, @Param("exclude") DocumentCatalogFolder exclude);

}
