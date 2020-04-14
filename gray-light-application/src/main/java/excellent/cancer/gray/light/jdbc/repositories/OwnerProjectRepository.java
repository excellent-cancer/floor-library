package excellent.cancer.gray.light.jdbc.repositories;

import excellent.cancer.gray.light.jdbc.entities.OwnerProject;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface OwnerProjectRepository extends CrudRepository<OwnerProject, Long> {

    /**
     * 通过OwnerId获取owner的所有project
     *
     * @param ownerId 项目所属者Id
     * @return 异步的项目列表
     */
    @Async
    @Query("SELECT * FROM owner_project WHERE owner_project.owner_id = :ownerId")
    CompletableFuture<List<OwnerProject>> findByOwnerId(@Param("ownerId") Long ownerId);

    /**
     * 通过所属者Id和项目Id查询出是否此项目
     *
     * @param ownerId 项目所属者ID
     * @param id      项目ID
     * @return 异步项目
     */
    @Query("SELECT * FROM owner_project WHERE owner_project.owner_id = :ownerId and owner_project.id = :id")
    Optional<OwnerProject> findByOwnerIdAndId(@Param("ownerId") Long ownerId, @Param("id") Long id);

}