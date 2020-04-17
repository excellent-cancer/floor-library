package excellent.cancer.gray.light.jdbc.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 表示存在过的项目，可以git项目，或者svn项目等
 *
 * @author XyParaCrim
 */
@Data
@Builder
@AllArgsConstructor
@Alias("OwnerProject")
public class OwnerProject {

    private Long id;

    private Long ownerId;

    private String name;

    private String description;

    private Date createdDate;

    private Date updatedDate;

    /**
     * 返回一个只包含Id的项目
     *
     * @param id 项目ID
     * @return 只包含Id的项目
     */
    public static OwnerProject justIdProject(Long id) {
        return builder().id(id).build();
    }
}
