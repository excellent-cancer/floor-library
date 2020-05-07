package excellent.cancer.gray.light.step;

import excellent.cancer.gray.light.document.DocumentRepositoryDatabase;
import excellent.cancer.gray.light.document.RepositoryOptions;
import excellent.cancer.gray.light.jdbc.entities.Document;
import excellent.cancer.gray.light.jdbc.entities.DocumentStatus;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

/**
 * 批量将克隆远程仓库到{@link DocumentRepositoryDatabase}中，默认使用{@link java.util.concurrent.ForkJoinPool}进行
 * 异步更新，但是该步骤会阻塞直至所有克隆任务全部完成
 *
 * @author XyParaCrim
 */
@CommonsLog
@RequiredArgsConstructor
public class BatchCloneRemoteRepositoryStep extends AbstractExecuteStep<Document> {

    /**
     * 批量克隆步骤执行结果，包括成功与失败的文档
     *
     * @author XyParaCrim
     */
    public static class Result {

        @Getter
        private final List<Document> success = new LinkedList<>();

        @Getter
        private final List<Document> failed = new LinkedList<>();

    }


    @NonNull
    private final DocumentRepositoryDatabase repositoryDatabase;

    /**
     * 根据一组文档实体，克隆其文档仓库至{@link DocumentRepositoryDatabase}
     *
     * @param docs 文档
     * @return 执行结果
     */
    public Result execute(@NonNull List<Document> docs) {
        CompletableFuture<?>[] asyncTasks = docs.
                stream().
                map(document ->
                        CompletableFuture.
                                supplyAsync(new AsyncCloneRemoteTask(document)).
                                whenCompleteAsync(this::complete)).
                toArray(CompletableFuture[]::new);

        return result(asyncTasks, docs);
    }

    /**
     * 通过所有异步任务构造步骤执行结果。这里会阻塞直至所有的异步任务完成，
     * 特别地，每个异步任务会返回其所要处理的文档，文档的{@link DocumentStatus}
     * 表明了该文档的执行是否是真正成功，但是，若请求的文档本身的状态为无效
     * 的，则同样会将其加入到失败结果中
     *
     * @param completedFutures 所有克隆异步任务
     * @param docs             请求文档
     * @return 步骤执行结果
     */
    protected Result result(CompletableFuture<?>[] completedFutures, List<Document> docs) {
        Result result = new Result();
        try {
            CompletableFuture.allOf(completedFutures).get();
        } catch (InterruptedException e) {
            log.error("The thread waiting for the cloning task to complete receives the interrupt signal", e);
            docs.forEach(document -> {
                document.setDocumentStatus(DocumentStatus.INVALID);
                result.failed.add(document);
            });

            return result;
        } catch (ExecutionException e) {
            log.error("Failed to clone document repository", e);
            docs.forEach(document -> {
                document.setDocumentStatus(DocumentStatus.INVALID);
                result.failed.add(document);
            });

            return result;
        }


        for (CompletableFuture<?> future : completedFutures) {
            Document document = null;

            // 因为上一步全部克隆任务都已经完成，理论上，这一步不可能发生异常
            // 因此忽略这个地方的失败
            try {
                document = (Document) future.get();
            } catch (InterruptedException | ExecutionException e) {
                assert false;
            }

            if (document.getDocumentStatus() == DocumentStatus.INVALID) {
                result.failed.add(document);
            } else {
                result.success.add(document);
            }
        }

        return result;
    }

    // 帮助方法

    private void cloneRemote(Path location, Document document) throws GitAPIException {
        try (Git ignored = Git.cloneRepository().
                setURI(document.getRepoUrl()).
                setDirectory(location.toFile()).
                call()) {
            log.info("clone document repository: " + document);
        }
    }

    @Override
    protected void failed(Document document, Throwable t) {
        super.failed(document, t);
        document.setDocumentStatus(DocumentStatus.INVALID);
    }

    @Override
    protected void complete(Document document, Throwable t) {
        if (t != null) {
            failed(document, t);
        }
    }

    /**
     * 异步克隆任务简单封装
     */
    @RequiredArgsConstructor
    private class AsyncCloneRemoteTask implements Supplier<Document> {

        private final Document document;

        @Override
        public Document get() {
            // 获取文档仓库的使用权限
            RepositoryOptions options = repositoryDatabase.getRepositoryOptions(document.getId());

            Optional<Long> stamp;
            try {
                stamp = options.getWriteLockWithTimeout();
            } catch (InterruptedException e) {
                // 执行线程若中断，则立即退出
                failed(document, new IllegalStateException("Failed to acquire document repository semaphore"));
                return document;
            }

            if (stamp.isPresent()) {
                // 首先清除本地旧的仓库，然后再克隆
                try {
                    Path location = options.cleanLocation();

                    try {
                        cloneRemote(location, document);
                        success(document);
                    } catch (GitAPIException e) {
                        failed(document, e);
                    }
                } catch (IOException e) {
                    failed(document, e);
                } finally {
                    options.unlockWrite(stamp.get());
                }

            } else {
                Throwable illegalState = new IllegalStateException("Failed to acquire document repository semaphore");
                failed(document, illegalState);
            }

            return document;
        }
    }

}
