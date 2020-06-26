package gray.light.owner.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@AllArgsConstructor
public class ProjectDetails {

    private Long originId;

    private String type;

    private String http;

    private String version;

    private Date createdDate;

    private Date updatedDate;

    @Builder.Default
    private ProjectStatus status = ProjectStatus.INIT;

    @Builder.Default
    private ProjectStructure structure = ProjectStructure.UNKNOWN;

}
