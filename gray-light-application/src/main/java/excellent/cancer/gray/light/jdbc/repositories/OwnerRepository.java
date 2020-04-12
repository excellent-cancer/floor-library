package excellent.cancer.gray.light.jdbc.repositories;

import excellent.cancer.gray.light.jdbc.entities.Owner;
import excellent.cancer.gray.light.jdbc.entities.OwnerProject;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * 所属者聚集对象，提供关于所属者的功能
 *
 * @author XyParaCrim
 */
public interface OwnerRepository extends CrudRepository<Owner, Long> {

/*    @Async
    @Modifying
    @Query("")
    CompletableFuture<Boolean> saveDocumentForProject(OwnerProject project);*/

    @Async
    @Query("SELECT id, name, description from owner_project where owner_project.owner_id = :id")
    CompletableFuture<List<OwnerProject>> findProjectsByOwnerId(@Param("id") Long ownerId);

    @Async
    @Query("SELECT id, name, description from owner_project where owner_project.owner_id = :id and owner_project.id = :project")
    CompletableFuture<Optional<OwnerProject>> findProjectByOwnerIdAndProjectId(@Param("id") Long ownerId, @Param("project") Long projectId);

}
