package gray.light.book.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 文档中的一章，是具有具体文件的
 *
 * @author XyParaCrim
 */
@Data
@Builder
@AllArgsConstructor
@Alias("BookChapter")
public class BookChapter {

    /**
     * 文档ID
     */
    private String uid;

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
    private String catalogUid;

    /**
     * 文档Id
     */
    private Long ownerProjectId;


    @Builder.Default
    private String downloadLink = "";

}
