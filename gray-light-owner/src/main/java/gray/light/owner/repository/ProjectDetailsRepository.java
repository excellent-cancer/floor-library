package gray.light.owner.repository;

import gray.light.owner.entity.ProjectDetails;
import gray.light.owner.entity.ProjectStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import perishing.constraint.jdbc.Page;

import java.util.List;

/**
 * 表project_details的映射器
 *
 * @author XyParaCrim
 */
@Mapper
public interface ProjectDetailsRepository {

    /**
     * 保存指定projectDetails
     *
     * @param projectDetails 指定projectDetails
     * @return 是否保存成功
     */
    boolean save(ProjectDetails projectDetails);

    boolean batchUpdateProjectDetailsStatus(@Param("docs") List<ProjectDetails> docs);

    List<ProjectDetails> findByStatusAndScopeAndType(@Param("status") ProjectStatus status,
                                                     @Param("scope") String scope,
                                                     @Param("type") String type,
                                                     @Param("page") Page page);
}
