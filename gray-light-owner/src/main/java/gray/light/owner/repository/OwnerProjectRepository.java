package gray.light.owner.repository;

import gray.light.owner.entity.OwnerProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import perishing.constraint.jdbc.Page;

import java.util.List;
import java.util.Optional;

/**
 * 表owner_project的映射器
 *
 * @author XyParaCrim
 */
@Mapper
public interface OwnerProjectRepository {

    // 单查询

    /**
     *  根据指定Id查询所属者项目
     *
     * @param id 所属者ID
     * @return 所属者项目
     */
    Optional<OwnerProject> findById(@Param("id") Long id);

    // 多查询

    /**
     * 查询所有属所属者项目
     *
     * @param page 分页
     * @return 查询所有属所属者项目
     */
    List<OwnerProject> findAll(@Param("page") Page page);

    /**
     * 通过OwnerId获取owner的所有project
     *
     * @param ownerId 项目所属者Id
     * @param scope 范围
     * @param page 分页
     * @return 通过OwnerId获取owner的所有project
     */
    List<OwnerProject> findByOwnerIdAndScope(@Param("ownerId") Long ownerId, @Param("scope") String scope, @Param("page") Page page);

    // 检测

    /**
     * 是否存在该Id的所属者项目
     *
     * @param id 所属者项目id
     * @return 是否存在该Id的所属者项目
     */
    boolean existsById(@Param("id") Long id);

    /**
     * 是否存在该Id的所属者项目且属于此所属者
     *
     * @param id 所属者项目id
     * @param ownerId 所属者id
     * @return 是否存在该Id的所属者项目
     */
    boolean existsByIdAndOwnerId(@Param("ownerId") Long ownerId, @Param("id") Long id);

    // 保存

    /**
     * 保存一个所属者项目
     *
     * @param project 所属者项目
     * @return 是否保存成功
     */
    boolean save(OwnerProject project);

    // 删除

    /**
     * 删除一个所属者项目
     *
     * @param id 所属者项目id
     * @return 是否保存成功
     */
    boolean delete(@Param("id") Long id);

}
