package org.excellent.cancer.experimental.application.jdbc.repositories;

import org.excellent.cancer.experimental.application.jdbc.entities.Owner;
import org.springframework.data.repository.CrudRepository;

/**
 * 所属者聚集对象，提供关于所属者的功能
 *
 * @author XyParaCrim
 */
public interface OwnerRepository extends CrudRepository<Owner, Long> {

}
