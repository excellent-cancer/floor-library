package excellent.cancer.gray.light.jdbc.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.springframework.data.annotation.Id;

/**
 * 表示存在过的项目，可以git项目，或者svn项目等
 *
 * @author XyParaCrim
 */
@Data
@AllArgsConstructor
public class OwnerProject {

    @Id @With
    private final Long id;

    private String name;

    private String description;

    // private Set<ProjectLink> links;

    // private Set<ProjectDocumentCatalog> docs;

}
