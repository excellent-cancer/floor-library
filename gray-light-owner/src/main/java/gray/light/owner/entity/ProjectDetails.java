package gray.light.owner.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 只记录关于GIT项目的信息
 *
 * @author XyParaCrim
 */
@Data
@Alias("ProjectDetails")
@AllArgsConstructor
public class ProjectDetails {

    private long originId;

    private String type;

    private String http;

    private String version;

    private Date createdDate;

    private Date updatedDate;

    private ProjectStatus status;

    private ProjectStructure structure;

}
