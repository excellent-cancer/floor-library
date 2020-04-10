package org.excellent.cancer.experimental.application.jdbc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

import java.util.Date;

/**
 * 项目中的文档目录
 *
 * @author XyParaCrim
 */
@Data
@AllArgsConstructor
public class ProjectDocumentCatalog {

    /**
     * 目录Id，它有如下特征：
     *   低 1～32位表示当前目录组的位置
     *   高33～47位表示当前目录的父目录
     *   高48～64位表示当前目录的深度
     */
    @Id @With
    private final Long id;

    /**
     * 创建日期
     */
    @With
    private final Date createdDate;

    /**
     * 最近修改日期
     */
    private Date updatedDate;

    /**
     * 目录标题
     */
    private String title;

    /**
     * 表示该目录的资源（不使用另外一张表：叶子结点比例比较大，空缺的目录占少数）
     */
    @Embedded.Empty
    private CatalogResource catalogResource;

    @Data
    @AllArgsConstructor
    public static class CatalogResource {

        private boolean resource;

        private String downloadUrl;

    }
}
