package excellent.cancer.gray.light.job;

import excellent.cancer.gray.light.document.DocumentRepositoryVisitor;
import excellent.cancer.gray.light.jdbc.entities.Document;
import excellent.cancer.gray.light.jdbc.entities.DocumentCatalog;
import excellent.cancer.gray.light.jdbc.entities.DocumentChapter;
import excellent.cancer.gray.light.jdbc.entities.DocumentStatus;
import excellent.cancer.gray.light.service.DocumentRelationService;
import excellent.cancer.gray.light.utils.FastdfsClient;
import lombok.extern.apachecommons.CommonsLog;
import org.csource.common.MyException;
import org.csource.fastdfs.TrackerClient;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import reactor.util.function.Tuple2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

/**
 * 从远程仓库克隆文档仓库，并且将其上传到文件服务器，最后更新数据库
 *
 * @author XyParaCrim
 */
@CommonsLog
@SuppressWarnings("unused")
public class UploadDocumentRepositoryJob extends QuartzJobBean {

    private DocumentRelationService documentService;

    private TrackerClient trackerClient;

    private Path documentRepositories;

    @Override
    protected void executeInternal(JobExecutionContext context) {

        // 克隆远程仓库到本地
        List<Document> emptyDocument = documentService.allEmptyDocument();
        List<Document> normalDocument = new ArrayList<>(emptyDocument);
        batchCloneRemoteRepository(normalDocument.listIterator());

        // 上传文件到文件服务器
        List<DocumentRepositoryVisitor> visitors = batchWalkGitTree(normalDocument.listIterator());
        batchUploadDocument(visitors);

        // 批量更新数据到数据库

        List<DocumentCatalog> catalogs = visitors.stream().
                flatMap(visitor -> visitor.getCatalogs().stream()).
                collect(Collectors.toUnmodifiableList());
        List<DocumentChapter> chapters = visitors.stream().
                flatMap(visitor -> visitor.getChapters().stream().map(Tuple2::getT1)).
                collect(Collectors.toUnmodifiableList());

        documentService.batchUpdateDocumentRepositories(emptyDocument, catalogs, chapters);
    }

    /**
     * 根据状态为空文档的仓库地址，克隆文档仓库到本地，如果其中有文档在克隆期间发生
     * 异常，则将文档状态设置为{@link DocumentStatus#INVALID}无效状态，并将其
     * 从文档迭代器中移除
     *
     * @param iterator 文档迭代器
     */
    private void batchCloneRemoteRepository(ListIterator<Document> iterator) {

        while (iterator.hasNext()) {
            Document document = iterator.next();

            File gitDir = getGitDir(document);
            String remoteAddress = document.getRepoUrl();

            try (Git ignored = Git.cloneRepository().
                    setURI(remoteAddress).
                    setGitDir(gitDir).
                    call()) {

                log.info("clone document repository: " + document);

            } catch (GitAPIException e) {
                processFailedError(document, iterator, "Failed to clone document repository", e);
            }
        }

    }

    /**
     * 遍历本地文档仓库，获取目录、章节等信息，并将其储存在{@link DocumentRepositoryVisitor}里返回。
     * 如果其中有遍历期间发生异常，则将文档状态设置为{@link DocumentStatus#INVALID}无效状态，并将其
     * 从文档迭代器中移除
     *
     * @param iterator 文档迭代器
     * @return 文档仓库遍历的结果
     */
    private List<DocumentRepositoryVisitor> batchWalkGitTree(ListIterator<Document> iterator) {
        List<DocumentRepositoryVisitor> visitors = new LinkedList<>();
        while (iterator.hasNext()) {
            Document document = iterator.next();
            File gitDir = getGitDir(document);
            DocumentRepositoryVisitor visitor = new DocumentRepositoryVisitor(document);

            try {
                Files.walkFileTree(gitDir.toPath(), visitor);
            } catch (IOException e) {
                processFailedError(document, iterator, "Failed to walk document repository in local", e);
                continue;
            }

            if (visitor.getFailed() == null) {
                visitors.add(visitor);
            } else {
                processFailedError(document, iterator, "Failed to walk document repository in local", visitor.getFailed());
            }
        }

        return visitors;
    }

    /**
     * 将文档仓库遍历的结果上传到文件服务器
     *
     * @param visitors 档仓库遍历的结果
     */
    private void batchUploadDocument(List<DocumentRepositoryVisitor> visitors) {
        if (!visitors.isEmpty()) {

            ListIterator<DocumentRepositoryVisitor> iterator = visitors.listIterator();

            try (FastdfsClient client = new FastdfsClient(trackerClient)) {

                do {

                    DocumentRepositoryVisitor visitor = iterator.next();
                    ListIterator<Tuple2<DocumentChapter, Path>> chapterIterator = visitor.getChapters().listIterator();

                    // 尝试上传章节文件

                    while (chapterIterator.hasNext()) {

                        Tuple2<DocumentChapter, Path> chapterPair = chapterIterator.next();

                        try {
                            String url = client.uploadMarkdown(chapterPair.getT2());
                            DocumentChapter chapter = chapterPair.getT1();

                            chapter.setEmpty(false);
                            chapter.setDownloadLink(url);
                        } catch (Exception e) {
                            // 出现其中一个章节上传文件失败，需要将之前成功上传的事件加入失败队列
                            processFailedError(visitor.getDocument(), iterator,
                                    "Failed to upload document to server and try to store uploaded event to failed queue", e);
                            processUploadUnfinishedChapter(visitor.getChapters(), chapterIterator.previousIndex());

                            break;
                        }

                    }


                } while (iterator.hasNext());

            } catch (MyException | IOException e) {

                log.error("Failed to connect file server due to failed to upload document chapter");

                visitors.clear();
            }

        }

    }

    // 帮助方法

    private File getGitDir(Document document) {
        return documentRepositories.resolve(document.getId().toString()).toFile();
    }

    private void processFailedError(Document document, ListIterator<?> iterator, String message, Throwable e) {
        processFailedError(document, message, e);
        iterator.remove();
    }

    private void processFailedError(Document document, String message, Throwable e) {
        log.error(message + ": " + document, e);
        document.setStatus(DocumentStatus.INVALID);
    }

    private void processUploadUnfinishedChapter(List<Tuple2<DocumentChapter, Path>> chapters, int cursor) {

        // TODO 需要将之前成功上传的事件加入失败队列

    }

}
