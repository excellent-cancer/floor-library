package gray.light.document.repository;

import gray.light.document.entity.WorksDocument;
import org.apache.ibatis.annotations.Mapper;

/**
 * works文档的映射器
 *
 * @author XyParaCrim
 */
@Mapper
public interface WorksDocumentRepository {

    boolean save(WorksDocument worksDocument);

}
