package excellent.cancer.gray.light.jdbc.repositories;

import excellent.cancer.gray.light.jdbc.entities.Owner;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 所属者聚集对象，提供关于所属者的功能
 *
 * @author XyParaCrim
 */
@Mapper
public interface OwnerRepository {


    List<Owner> findAll();

    boolean existsById(long id);

    boolean save(Owner owner);

}
