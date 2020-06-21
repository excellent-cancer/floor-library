package gray.light.document.repository;

import gray.light.document.entity.DocumentCatalog;
import gray.light.document.entity.DocumentCatalogFolder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface DocumentCatalogsRepository {

    DocumentCatalog save(DocumentCatalog rootCatalog);

    Optional<DocumentCatalog> findById(@Param("id") Long id);

    List<DocumentCatalog> findByDocumentId(@Param("documentId") long documentId);

    boolean batchSave(@Param("catalogs") List<DocumentCatalog> catalogs);

    boolean saveIfMatchedProject(DocumentCatalog documentCatalog);

    boolean saveIfMatchedParentAndExcludeFolder(@Param("catalog") DocumentCatalog catalog, @Param("exclude") DocumentCatalogFolder exclude);

    boolean updateByFolder(@Param("uid") String uid, @Param("folder") DocumentCatalogFolder folder);

}
