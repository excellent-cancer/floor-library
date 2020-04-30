package excellent.cancer.gray.light.jdbc.entities;

/**
 * 表示文档此时的状态：invalid，empty，sync，new
 *
 * @author XyParaCrim
 */
public enum DocumentStatus {

    /**
     * 文档无效状态，可能在上传是发生了错误
     */
    INVALID,

    /**
     * 文档此时为空，即只更新了信息，未上传文档文件
     */
    EMPTY,

    /**
     * 文档已经上传到文件服务器
     */
    SYNC,

    /**
     * 有新的更新需要同步
     */
    NEW

}
