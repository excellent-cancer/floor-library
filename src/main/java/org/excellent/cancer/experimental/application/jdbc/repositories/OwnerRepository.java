package org.excellent.cancer.experimental.application.jdbc.repositories;

import org.excellent.cancer.experimental.application.jdbc.entities.Owner;
import org.springframework.data.repository.CrudRepository;

public interface OwnerRepository extends CrudRepository<Owner, Long> {
}
