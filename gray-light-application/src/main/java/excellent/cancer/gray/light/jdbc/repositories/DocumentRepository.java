package excellent.cancer.gray.light.jdbc.repositories;

import excellent.cancer.gray.light.jdbc.entities.Document;
import excellent.cancer.gray.light.jdbc.entities.DocumentCatalog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DocumentRepository {

    Iterable<Document> findAll();

    boolean save(Document document);

    boolean saveIfMatchedDocumentCatalogAndExcludeFolder(Document document, @Param("exclude") DocumentCatalog.Folder exclude);
}
