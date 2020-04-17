package excellent.cancer.gray.light.jdbc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
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
     * 目录Id，它有如下特征：
     * 低 1～32位表示当前目录组的位置
     * 高33～47位表示当前目录的父目录
     * 高48～64位表示当前目录的深度
     */
    private Long id;

    /**
     * 隶属组的ID：例如项目Id等等
     */
    @NonNull
    private Long projectId;

    /**
     * 父目录Id
     */
    @NonNull
    private Long parentId;

    /**
     * 创建日期
     */
    @NonNull
    private Date createdDate;

    /**
     * 最近修改日期
     */
    @NonNull
    private Date updatedDate;

    /**
     * 目录标题
     */
    @NonNull
    @Builder.Default
    private String title = "";

    /**
     * 是否是一个包含文档的
     */
    @NonNull
    @Builder.Default
    private Folder folder = Folder.EMPTY;

    public enum Folder {
        EMPTY,

        CATALOG,

        DOCS,
    }

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
     * @param parentId  父目录Id
     * @param projectId 隶属组的ID：例如项目Id等等
     * @return 返回一个此时新建的builder
     */
    public static DocumentCatalogBuilder builderOnCreateWithNoDefault(Long parentId, Long projectId) {
        return builderOnCreate().
                parentId(parentId).
                projectId(projectId);
    }

    /**
     * 默认将根目录的{@link DocumentCatalog#parentId}设为0
     */
    public static final Long ROOT = 0L;

}
