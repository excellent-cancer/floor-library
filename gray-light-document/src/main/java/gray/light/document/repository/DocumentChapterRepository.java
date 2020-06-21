package gray.light.document.repository;

import gray.light.document.entity.DocumentCatalogFolder;
import gray.light.document.entity.DocumentChapter;
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
