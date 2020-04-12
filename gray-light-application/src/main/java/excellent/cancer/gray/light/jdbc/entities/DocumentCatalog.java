package excellent.cancer.gray.light.jdbc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * 项目中的文档目录
 *
 * @author XyParaCrim
 */
@Data
@Builder
@AllArgsConstructor
public class DocumentCatalog {

    /**
     * 目录Id，它有如下特征：
     * 低 1～32位表示当前目录组的位置
     * 高33～47位表示当前目录的父目录
     * 高48～64位表示当前目录的深度
     */
    @Id
    private Long id;

    /**
     * 隶属组的ID：例如项目Id等等
     */
    private Long groupId;

    /**
     * 父目录Id
     */
    private Long parentId;

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
     * 返回一个此时新建的builder，即创建时间和最近更新时间是此时
     *
     * @return 返回一个此时新建的builder
     */
    public static DocumentCatalogBuilder builderWithCreate() {
        Date date = new Date();
        return builder()
                .createdDate(date)
                .updatedDate(date);
    }

    /**
     * 默认将根目录的{@link DocumentCatalog#parentId}设为0
     */
    public static final Long ROOT = 0L;

    /**
     * 表示该目录的资源（不使用另外一张表：叶子结点比例比较大，空缺的目录占少数）
     */
/*    @Embedded.Empty
    private CatalogResource catalogResource;

    @Data
    @AllArgsConstructor
    public static class CatalogResource {

        private boolean resource;

        private String downloadUrl;

    }*/
}
