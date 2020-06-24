package gray.light.owner.entity;


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

    // 时间信息

    private Date createdDate;

    private Date updatedDate;

    // 下面提供一些基础信息

    private String name;

    private String description;

    private String scope;

    private String homePage;

}
