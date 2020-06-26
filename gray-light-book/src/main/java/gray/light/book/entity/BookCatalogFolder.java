package gray.light.book.entity;

/**
 * 表示Book结构项目目录的子元素的状态，包括：空，章节或者目录
 *
 * @author XyParaCrim
 */
public enum  BookCatalogFolder {

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
