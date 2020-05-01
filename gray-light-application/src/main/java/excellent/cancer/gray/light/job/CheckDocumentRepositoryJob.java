package excellent.cancer.gray.light.job;

import excellent.cancer.gray.light.document.DocumentRepositoryDatabase;
import excellent.cancer.gray.light.document.RepositoryOptions;
import excellent.cancer.gray.light.jdbc.entities.Document;
import excellent.cancer.gray.light.jdbc.entities.DocumentStatus;
import excellent.cancer.gray.light.service.DocumentRelationService;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

/**
 * 检查文档仓库是否更新，自动拉取最新版本，并存储到数据库
 *
 * @author XyParaCrim
 */
@CommonsLog
@SuppressWarnings("unused")
public class CheckDocumentRepositoryJob extends QuartzJobBean {

    // 默认使用git ref名

    private static final String REMOTE_REF = "refs/remotes/origin/master";

    private static final String LOCAL_REF = "refs/heads/master";

    private Path documentRepositories;

    private DocumentRelationService documentService;

    private DocumentRepositoryDatabase repositoryDatabase;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        List<Document> syncDocument = documentService.allSyncDocument();
        ListIterator<Document> iterator = syncDocument.listIterator();

        while (iterator.hasNext()) {
            Document document = iterator.next();
            RepositoryOptions options = repositoryDatabase.getRepositoryOptions(document.getId());
            Optional<Long> stamp = options.getReadLockWithTimeout();

            if (stamp.isPresent()) {

                try {
                    File gitDir = options.getLocation().toFile();

                    try (Git git = Git.open(gitDir)) {
                        fetchRemote(git);

                        // 比较版本差异
                        if (hasUpdate(git)) {
                            updateLocal(git);
                            document.setStatus(DocumentStatus.NEW);
                        } else {
                            iterator.remove();
                        }

                    } catch (GitAPIException | IOException e) {
                        log.error("Unable to check document repository updates: " + document, e);
                        iterator.remove();
                    }
                } finally {
                    options.unlockWrite(stamp.get());
                }

            } else {
                iterator.remove();
            }
        }

        if (syncDocument.size() > 0) {
            documentService.batchUpdateDocumentStatus(syncDocument);
        }
    }

    /**
     * 比较origin/master和master是否存在版本差异
     *
     * @param git git实例
     * @return 是否存在版本差异
     * @throws IOException 获取git文件时，可能发生的错误
     */
    private static boolean hasUpdate(Git git) throws IOException {
        // 比较版本差异
        Repository repository = git.getRepository();

        Ref local = repository.exactRef(LOCAL_REF);
        Ref remote = repository.exactRef(REMOTE_REF);

        return local.getObjectId().compareTo(remote.getObjectId().toObjectId()) != 0;
    }

    /**
     * 执行远程仓库拉取
     *
     * @param git git实例
     * @throws GitAPIException 执行命令发生错误
     */
    private static void fetchRemote(Git git) throws GitAPIException {
        git.fetch().setCheckFetchedObjects(true).call();
    }

    /**
     * 更新版本与远程仓库一致
     *
     * @param git git实例
     * @throws GitAPIException 执行命令发生错误
     */
    private static void updateLocal(Git git) throws GitAPIException {
        git.reset().setRef(REMOTE_REF).setMode(ResetCommand.ResetType.HARD).call();
    }

}
