package excellent.cancer.gray.light.jdbc.repositories;

import excellent.cancer.gray.light.jdbc.entities.DocumentCatalog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface DocumentCatalogRepository {

    DocumentCatalog save(DocumentCatalog rootCatalog);

    Optional<DocumentCatalog> findById(@Param("id") Long id);

    boolean saveIfMatchedEmptyDocsProject(DocumentCatalog documentCatalog);

    boolean saveIfMatchedCatalogAndExcludeFolder(DocumentCatalog catalog, @Param("exclude") DocumentCatalog.Folder exclude);

    boolean updateFolder(@Param("id") long id, @Param("folder") DocumentCatalog.Folder folder);
}
