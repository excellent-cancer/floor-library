package gray.light.owner.repository;

import gray.light.owner.entity.Owner;
import gray.light.owner.entity.Privilege;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import perishing.constraint.jdbc.Page;

import java.util.List;
import java.util.Optional;

/**
 * 所属者聚集对象，提供关于所属者的功能
 *
 * @author XyParaCrim
 */
@Mapper
public interface OwnerRepository {

    // 单查询

    /**
     * 查询指定所属者Id的所属者
     *
     * @param id 指定所属者Id
     * @return 指定所属者Id的所属者
     */
    Optional<Owner> findById(Long id);

    // 多查询

    /**
     * 查询所有所属者
     *
     * @param page 分页
     * @return 所有所属者
     */
    List<Owner> findAll(@Param("page") Page page);

    /**
     * 查询指定超级权限的所属者
     *
     * @param privilege 指定超级权限
     * @param page 分页
     * @return 查询指定超级权限的所属者
     */
    List<Owner> findBySuperPrivilege(@Param("privilege") Privilege privilege, @Param("page") Page page);

    // 检测

    /**
     * 检测是否存在指定Id的所属者
     *
     * @param id 指定Id的所属者
     * @return 检测是否存在指定Id的所属者
     */
    boolean existsById(long id);

    // 保存

    /**
     * 保存一个新的所属者
     *
     * @param owner 新的所属者
     * @return 保存一个新的所属者
     */
    boolean save(Owner owner);

}
