package excellent.cancer.gray.light.jdbc.entities;

import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * 项目文档，包括文档仓库地址、标题、描述等等
 *
 * @author XyParaCrim
 */
@Data
@Alias("Document")
public class Document {

    private Long id;

    private Long projectId;

    private String title;

    private String description;

    private String repoUrl;

}
