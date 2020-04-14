package excellent.cancer.gray.light.jdbc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

import java.util.Date;

/**
 * 文档中的一章，是具有具体文件的
 *
 * @author XyParaCrim
 */
@Data
@AllArgsConstructor
public class Document {

    /**
     * 文档ID
     */
    @Id
    private Long id;

    /**
     * 创建日期
     */
    private Date createdDate;

    /**
     * 最近修改日期
     */
    private Date updatedDate;

    /**
     * 目录标题
     */
    private String title;

    /**
     * 隶属的目录Id
     */
    private Long catalogId;

    /**
     * 一组关于具体资源的属性
     */
    @Embedded.Empty
    private DocumentResource resource;

    @Data
    @AllArgsConstructor
    private static class DocumentResource {

        private boolean isEmpty;

        private String downloadLink;

        private String uploadLink;

    }

    /**
     * 通用空资源属性
     */
    private static final DocumentResource EMPTY_RESOURCE = new DocumentResource(false, "", "");

}
