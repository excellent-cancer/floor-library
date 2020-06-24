package gray.light.document.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 项目文档，包括文档仓库地址、标题、描述等等
 *
 * @author XyParaCrim
 */
@Data
@Alias("Document")
@Builder
@AllArgsConstructor
public class Document {

    private Long id;

    private Long projectId;

    private String title;

    private String description;

    private Date createdDate;

    private Date updatedDate;

    private String repoUrl;

    private String version;

    @Builder.Default
    private DocumentStatus documentStatus = DocumentStatus.EMPTY;

}
