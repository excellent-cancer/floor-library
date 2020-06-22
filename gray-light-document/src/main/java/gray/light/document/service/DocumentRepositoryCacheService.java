package gray.light.document.service;

import floor.repository.RepositoryDatabase;
import floor.repository.RepositoryOptions;
import gray.light.document.DocumentRepositoryVisitor;
import gray.light.document.entity.Document;
import gray.light.document.entity.DocumentStatus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

/**
 * 定义有关于文档文件的上传、下载和更改
 *
 * @author XyParaCrim
 */
@Service
@CommonsLog
@RequiredArgsConstructor
public class DocumentRepositoryCacheService {

    private final RepositoryDatabase<Long, Long> repositoryDatabase;

    /**
     * 更新本地仓库。若长时间未获得操作权，则不修改文档状态，
     * 若在更新期间发生异常，则将文档状态修改为{@code DocumentStatus.INVALID}
     *
     * @param document 指定文档
     * @throws InterruptedException 当获取操作许可时，发生中断
     */
    public void updateRepository(@NonNull Document document) throws InterruptedException {
        RepositoryOptions<Long, Long> options = repositoryDatabase.repositoryOptions(document.getId());

        Optional<Long> writeStamp = options.writePermission();
        if (writeStamp.isPresent()) {
            try {
                // 比较版本差异

                if (options.hasUpdate()) {
                    options.updateLocal();
                    document.setDocumentStatus(DocumentStatus.NEW);
                }
            } catch (GitAPIException | IOException e) {
                document.setDocumentStatus(DocumentStatus.INVALID);

                log.error("Cannot update the repository: " + document.getId(), e);
            } finally {
                options.cancelWritePermission(writeStamp.get());
            }
        } else {
            log.warn("Skip repository update since write permission is occupied");
        }
    }

    /**
     * 强制缓存文档仓库到本地，即使已存在的仓库与指定仓库一致，也会完全删除，
     * 再重新克隆
     *
     * @param document 指定文档
     */
    public void forceCacheRepository(@NonNull Document document) {
        repositoryDatabase.addRepositoryOptions(document.getId(), document.getRepoUrl());
    }

    /**
     * 浏览指定文档仓库，并返回浏览结果
     *
     * @param document 指定文档
     * @return 浏览结果
     * @throws InterruptedException 当获取读权限时
     */
    public DocumentRepositoryVisitor visitRepository(Document document) throws InterruptedException {
        // 获取文档仓库的使用权限
        RepositoryOptions<Long, Long> options = repositoryDatabase.repositoryOptions(document.getId());
        Optional<Long> stamp = options.readPermission();

        if (stamp.isEmpty()) {
            return DocumentRepositoryVisitor.failedVisitor(document, new IllegalStateException("Failed to acquire document repository semaphore"));
        }

        try {
            DocumentRepositoryVisitor visitor;
            try {
                visitor = new DocumentRepositoryVisitor(document);
                Files.walkFileTree(options.getLocation().toPath(), visitor);

            } catch (IOException e) {
                return DocumentRepositoryVisitor.failedVisitor(document, e);
            }

            return visitor;
        } finally {
            options.cancelReadPermission(stamp.get());
        }
    }

    public boolean availableRepository(Document document) {
        RepositoryOptions<Long, Long> options;
        return (options = repositoryDatabase.repositoryOptions(document.getId())) != null &&
                Files.isDirectory(options.getLocation().toPath());
    }

}