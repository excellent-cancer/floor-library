package gray.light.book.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * book结构项目中的目录
 *
 * @author XyParaCrim
 */
@Data
@AllArgsConstructor
@Alias("BookCatalog")
@Builder
public class BookCatalog {

    /**
     * 目录ID
     */
    private String uid;

    /**
     * 所属者项目Id，包括文档项目、笔记项目
     */
    private Long ownerProjectId;

    /**
     * 父目录Id
     */
    private String parentUid;

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
    @Builder.Default
    private String title = "";

    /**
     * 是否是一个包含文档的
     */
    @Builder.Default
    private BookCatalogFolder folder = BookCatalogFolder.EMPTY;

}
