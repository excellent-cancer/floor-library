package excellent.cancer.gray.light.jdbc.entities;

/**
 * 表示文档目录的子元素的状态，包括：空，章节或者目录
 *
 * @author XyParaCrim
 */
public enum DocumentCatalogFolder {

    /**
     * 目录没有其他子元素
     */
    EMPTY,

    /**
     * 子元素为目录
     */
    CATALOG,

    /**
     * 子元素为章节
     */
    CHAPTER

}
