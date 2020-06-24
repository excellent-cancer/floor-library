package gray.light.owner.entity;


/**
 * 表示Git项目的状态
 *
 * @author XyParaCrim
 */
public enum ProjectStatus {

    /**
     * 仓库无效状态，可能在上传是发生了错误
     */
    INVALID,

    /**
     * 仓库此时为空，即只更新了信息，未上传文档文件
     */
    INIT,

    /**
     * 仓库已经上传到文件服务器
     */
    SYNC,

    /**
     * 有新的更新需要同步
     */
    PENDING,

    /**
     * 标记为删除
     */
    UNAVAILABLE
}
