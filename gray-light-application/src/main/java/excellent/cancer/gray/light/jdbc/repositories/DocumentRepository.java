package excellent.cancer.gray.light.jdbc.repositories;

import excellent.cancer.gray.light.jdbc.entities.Document;
import excellent.cancer.gray.light.jdbc.entities.DocumentStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目文档repository，提供文档、文档目录、文档章节等的查询更新功能
 *
 * @author XyParaCrim
 */
@Mapper
public interface DocumentRepository {

    // query for document

    boolean saveIfMatchedProject(Document document);

    boolean batchSave(@Param("documents") List<Document> documents);

    boolean batchUpdateDocumentStatus(@Param("documents") List<Document> documents);

    List<Document> findByStatus(@Param("status") DocumentStatus status);

    List<Document> findAll();
}
