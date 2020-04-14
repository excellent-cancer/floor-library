package excellent.cancer.gray.light.jdbc.entities;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

/**
 * 表示存在过的项目，可以git项目，或者svn项目等
 *
 * @author XyParaCrim
 */
@EqualsAndHashCode(callSuper = true)
@Data
//@Builder
@Entity
public class OwnerProject extends AbstractTimestampPersistable<Long> {

    private Long ownerId;

    private String name;

    private String description;

    /**
     * 返回一个只包含Id的项目
     *
     * @param id 项目ID
     * @return 只包含Id的项目
     */
/*    public static OwnerProject justIdProject(Long id) {
        OwnerProject project = builder().build();
        project.setId(id);

        return project;
    }*/
}
