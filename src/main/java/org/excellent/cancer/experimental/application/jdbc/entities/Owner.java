package org.excellent.cancer.experimental.application.jdbc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.Set;

/**
 * 表示整个产品的拥有者
 *
 * @author XyParaCrim
 */
@Data
@AllArgsConstructor
@AccessType(AccessType.Type.PROPERTY)
public class Owner {

    @Id @With
    private final Long id;

    private String username;

    private String organization;

    @MappedCollection(idColumn = "id", keyColumn = "owner_id")
    private Set<OwnerLink> links;

    @MappedCollection(idColumn = "id", keyColumn = "owner_id")
    private Set<OwnerProject> projects;

}
