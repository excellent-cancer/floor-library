package gray.light.document.repository;

import gray.light.document.entity.WorksDocument;
import gray.light.owner.entity.OwnerProject;
import gray.light.owner.entity.ProjectDetails;
import gray.light.owner.entity.ProjectStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import perishing.constraint.jdbc.Page;

import java.util.List;

/**
 * works文档的映射器
 *
 * @author XyParaCrim
 */
@Mapper
public interface WorksDocumentRepository {

    boolean save(WorksDocument worksDocument);

    List<ProjectDetails> findProjectDetailsByStatus(@Param("status") ProjectStatus status, @Param("page") Page page);

    List<OwnerProject> findOwnerProjectByWorksId(@Param("worksId") Long worksId, @Param("page") Page page);

}
