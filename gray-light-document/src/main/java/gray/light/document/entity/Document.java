package gray.light.document.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

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

    private Long projectId;

    private Long documentId;

}
