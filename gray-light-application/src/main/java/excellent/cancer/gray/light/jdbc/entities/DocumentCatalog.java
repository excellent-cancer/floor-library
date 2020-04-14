package excellent.cancer.gray.light.jdbc.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

/**
 * 项目中的文档目录
 *
 * @author XyParaCrim
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class DocumentCatalog extends AbstractTimestampPersistable<Long> {

    /**
     * 目录Id，它有如下特征：
     * 低 1～32位表示当前目录组的位置
     * 高33～47位表示当前目录的父目录
     * 高48～64位表示当前目录的深度
     */


    /**
     * 隶属组的ID：例如项目Id等等
     */
    private Long projectId;

    /**
     * 父目录Id
     */
    private Long parentId;

    /**
     * 目录标题
     */
    private String title;

    /**
     * 是否是一个包含文档的
     */
    private Boolean hasDocs;
    /*
     *//**
     * 返回一个此时新建的builder，即创建时间和最近更新时间是此时
     *
     * @return 返回一个此时新建的builder
     *//*
    public static DocumentCatalogBuilder builderOnCreate() {
        Date date = new Date();
        return builder()
                .createdDate(date)
                .updatedDate(date);
    }

    *//**
     * 返回一个此时新建的builder，即创建时间和最近更新时间是此时，特别地，输出参数包含了
     * 所有没有默认参数的成员变量，即这么函数生成的DocumentCatalog不会报错
     *
     * @param parentId  父目录Id
     * @param projectId 隶属组的ID：例如项目Id等等
     * @return 返回一个此时新建的builder
     *//*
    public static DocumentCatalogBuilder builderOnCreateWithNoDefault(Long parentId, Long projectId) {
        return builderOnCreate().
                parentId(parentId).
                projectId(projectId);
    }

    *//**
     * 默认将根目录的{@link DocumentCatalog#parentId}设为0
     *//*
    public static final Long ROOT = 0L;*/

}
