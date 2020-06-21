package gray.light.job;

import excellent.cancer.floor.repository.RepositoryDatabase;
import gray.light.DocumentRepositoryVisitor;
import gray.light.document.entity.Document;
import gray.light.document.entity.DocumentStatus;
import gray.light.service.DocumentRelationService;
import gray.light.step.BatchCloneRemoteRepositoryStep;
import gray.light.step.BatchUpdateDocumentRepositoriesStep;
import gray.light.step.UploadDocumentStep;
import gray.light.step.VisitDocumentRepositoryStep;
import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;
import org.csource.fastdfs.TrackerClient;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

/**
 * 从远程仓库克隆文档仓库，并且将其上传到文件服务器，最后更新数据库
 *
 * @author XyParaCrim
 */
@CommonsLog
public class UploadDocumentRepositoryJob extends QuartzJobBean {

    @Setter
    private DocumentRelationService documentService;

    @Setter
    private TrackerClient trackerClient;

    @Setter
    private RepositoryDatabase<Long, Long> repositoryDatabase;

    @Override
    protected void executeInternal(JobExecutionContext context) {

        log.error("更新任务");

        List<Document> emptyDocument = documentService.allEmptyDocument();

        // 克隆远程仓库到本地
        BatchCloneRemoteRepositoryStep.Result cloneResult = batchCloneRemoteRepository(emptyDocument);

        // 浏览仓库文件
        VisitDocumentRepositoryStep.Result visitResult = batchWalkGitTree(cloneResult.getSuccess());

        // 上传文件到文件服务器
        UploadDocumentStep.Result uploadResult = batchUploadDocument(visitResult.getVisitors());

        // 批量更新数据到数据库
        updateRepositories(uploadResult.getVisitors(), emptyDocument);
    }

    /**
     * 根据状态为空文档的仓库地址，克隆文档仓库到本地，如果其中有文档在克隆期间发生
     * 异常，则将文档状态设置为{@link DocumentStatus#INVALID}无效状态，并将其
     * 从文档迭代器中移除
     *
     * @param emptyDocument 状态为空的文档
     */
    private BatchCloneRemoteRepositoryStep.Result batchCloneRemoteRepository(List<Document> emptyDocument) {
        BatchCloneRemoteRepositoryStep batchCloneRemoteRepository = new BatchCloneRemoteRepositoryStep(repositoryDatabase);

        return batchCloneRemoteRepository.execute(emptyDocument);
    }

    /**
     * 遍历本地文档仓库，获取目录、章节等信息，并将其储存在{@link DocumentRepositoryVisitor}里返回。
     * 如果其中有遍历期间发生异常，则将文档状态设置为{@link DocumentStatus#INVALID}无效状态，并将其
     * 从文档迭代器中移除
     *
     * @param emptyDocument 状态为空的文档
     * @return 文档仓库遍历的结果
     */
    private VisitDocumentRepositoryStep.Result batchWalkGitTree(List<Document> emptyDocument) {
        VisitDocumentRepositoryStep batchWalkGitTree = new VisitDocumentRepositoryStep(repositoryDatabase);

        return batchWalkGitTree.execute(emptyDocument);
    }

    /**
     * 将文档仓库遍历的结果上传到文件服务器
     *
     * @param visitors 文件遍历器
     * @return 文档上传的结果
     */
    private UploadDocumentStep.Result batchUploadDocument(List<DocumentRepositoryVisitor> visitors) {
        UploadDocumentStep batchUploadDocument = new UploadDocumentStep(trackerClient);

        return batchUploadDocument.execute(visitors);
    }

    /**
     * 更新文档状态
     *
     * @param visitors      文件遍历器
     * @param emptyDocument 文档
     */
    private void updateRepositories(List<DocumentRepositoryVisitor> visitors, List<Document> emptyDocument) {
        BatchUpdateDocumentRepositoriesStep updateRepositories = new BatchUpdateDocumentRepositoriesStep(documentService);

        updateRepositories.execute(visitors, emptyDocument);
    }

}
