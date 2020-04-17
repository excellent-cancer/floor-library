package excellent.cancer.gray.light.jdbc.repositories;

import excellent.cancer.gray.light.jdbc.entities.OwnerProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface OwnerProjectRepository {

    /**
     * 通过OwnerId获取owner的所有project
     *
     * @param ownerId 项目所属者Id
     * @return 通过OwnerId获取owner的所有project
     */
    List<OwnerProject> findByOwnerId(@Param("ownerId") Long ownerId);

    /**
     * 通过所属者Id和项目Id查询出是否此项目
     *
     * @param ownerId 项目所属者ID
     * @param id      项目ID
     * @return 所属者Id和项目Id查询出是否此项目
     */
    Optional<OwnerProject> findByOwnerIdAndId(@Param("ownerId") Long ownerId, @Param("id") Long id);

    Iterable<OwnerProject> findAll();

    boolean existsById(@Param("id") long id);

    boolean existsByIdAndOwnerId(@Param("id") long id, @Param("ownerId") long ownerId);

    boolean save(OwnerProject project);

    boolean delete(OwnerProject project);

}
