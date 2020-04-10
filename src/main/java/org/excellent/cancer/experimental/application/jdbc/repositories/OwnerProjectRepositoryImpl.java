package org.excellent.cancer.experimental.application.jdbc.repositories;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.excellent.cancer.experimental.application.jdbc.entities.OwnerProject;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class OwnerProjectRepositoryImpl implements OwnerProjectRepository {

/*    @NonNull
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @NonNull
    private final JdbcAggregateOperations jdbcAggregateOperations;


    @Async
    @Query("SELECT id, name, description from owner_project where owner_project.id = :id")
    public CompletableFuture<List<OwnerProject>> findProjects(@Param("id") Long ownerId) {

    }*/
}
