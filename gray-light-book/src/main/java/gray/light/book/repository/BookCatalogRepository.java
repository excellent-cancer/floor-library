package gray.light.book.repository;

import gray.light.book.entity.BookCatalog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import perishing.constraint.jdbc.Page;

import java.util.List;

@Mapper
public interface BookCatalogRepository {

    List<BookCatalog> findByOwnerProjectId(@Param("ownerProjectId") Long ownerProjectId, @Param("page") Page page);

    boolean batchSave(@Param("catalogs") List<BookCatalog> catalogs);
}
