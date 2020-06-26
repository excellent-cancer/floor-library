package gray.light.owner.repository;

import gray.light.owner.entity.ProjectDetails;
import org.apache.ibatis.annotations.Mapper;

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

}
