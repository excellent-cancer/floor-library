package excellent.cancer.gray.light.jdbc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 项目中的文档目录
 *
 * @author XyParaCrim
 */
@Data
@Builder
@AllArgsConstructor
@Alias("DocumentCatalog")
public class DocumentCatalog {

    /**
     * 目录ID
     */
    private Long id;

    private String uid;

    /**
     * 隶属项目ID：例如项目Id等等
     */
    private Long projectId;

    /**
     * 隶属文档ID：例如项目Id等等
     */
    private Long documentId;

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
    private DocumentCatalogFolder folder = DocumentCatalogFolder.EMPTY;


    /**
     * 返回一个此时新建的builder，即创建时间和最近更新时间是此时
     *
     * @return 返回一个此时新建的builder
     */
    public static DocumentCatalogBuilder builderOnCreate() {
        Date date = new Date();
        return builder()
                .createdDate(date)
                .updatedDate(date);
    }

    /**
     * 返回一个此时新建的builder，即创建时间和最近更新时间是此时，特别地，输出参数包含了
     * 所有没有默认参数的成员变量，即这么函数生成的DocumentCatalog不会报错
     *
     * @param parentUid 父目录Id
     * @param projectId 隶属组的ID：例如项目Id等等
     * @return 返回一个此时新建的builder
     */
    public static DocumentCatalogBuilder builderOnCreateWithNoDefault(String parentUid, Long projectId) {
        return builderOnCreate().
                parentUid(parentUid).
                projectId(projectId);
    }

    /**
     * 默认将根目录的{@link DocumentCatalog#parentUid}设为0
     */
    public static final String ROOT = "*";

}
