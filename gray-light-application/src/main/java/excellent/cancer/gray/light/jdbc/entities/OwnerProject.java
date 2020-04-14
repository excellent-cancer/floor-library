package excellent.cancer.gray.light.jdbc.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.Date;
import java.util.Set;

/**
 * 表示存在过的项目，可以git项目，或者svn项目等
 *
 * @author XyParaCrim
 */
@Data
@Builder
@AllArgsConstructor
public class OwnerProject {

    @Id
    private Long id;

    private Long ownerId;

    private String name;

    private String description;

    private Date createdDate;

    private Date updatedDate;

    // private Set<ProjectLink> links;

    @MappedCollection(idColumn = "project_id")
    private Set<DocumentCatalog> docs;

}
