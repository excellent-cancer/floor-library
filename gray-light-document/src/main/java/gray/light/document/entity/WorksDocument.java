package gray.light.document.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * 定义works与document之间的关系
 *
 * @author XyParaCrim
 */
@Data
@Alias("WorksDocument")
@Builder
@AllArgsConstructor
public class WorksDocument {

    private Long worksId;

    private Long documentId;

}
