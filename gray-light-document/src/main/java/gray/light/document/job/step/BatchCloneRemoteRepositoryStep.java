package gray.light.document.job.step;

import gray.light.document.service.DocumentRepositoryCacheService;
import gray.light.owner.entity.ProjectDetails;
import gray.light.owner.entity.ProjectStatus;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

/**
 * 批量将克隆远程仓库到仓库缓存中，默认使用{@link java.util.concurrent.ForkJoinPool}进行
 * 异步更新，但是该步骤会阻塞直至所有克隆任务全部完成
 *
 * @author XyParaCrim
 */
@CommonsLog
@RequiredArgsConstructor
public class BatchCloneRemoteRepositoryStep extends AbstractExecuteStep<ProjectDetails> {

    /**
     * 批量克隆步骤执行结果，包括成功与失败的文档
     *
     * @author XyParaCrim
     */
    public static class Result {

        @Getter
        private final List<ProjectDetails> success = new LinkedList<>();

        @Getter
        private final List<ProjectDetails> failed = new LinkedList<>();

    }

    @NonNull
    private final DocumentRepositoryCacheService documentRepositoryCacheService;

    /**
     * 根据一组文档实体，克隆其文档仓库至缓存中
     *
     * @param docs 文档
     * @return 执行结果
     */
    public Result execute(@NonNull List<ProjectDetails> docs) {
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
     * 特别地，每个异步任务会返回其所要处理的文档，文档的{@link ProjectDetails}
     * 表明了该文档的执行是否是真正成功，但是，若请求的文档本身的状态为无效
     * 的，则同样会将其加入到失败结果中
     *
     * @param completedFutures 所有克隆异步任务
     * @param docs             请求文档
     * @return 步骤执行结果
     */
    protected Result result(CompletableFuture<?>[] completedFutures, List<ProjectDetails> docs) {
        Result result = new Result();
        try {
            CompletableFuture.allOf(completedFutures).get();
        } catch (InterruptedException e) {
            log.error("The thread waiting for the cloning task to complete receives the interrupt signal", e);
            docs.forEach(document -> {
                document.setStatus(ProjectStatus.INVALID);
                result.failed.add(document);
            });

            return result;
        } catch (ExecutionException e) {
            log.error("Failed to clone document repository", e);
            docs.forEach(document -> {
                document.setStatus(ProjectStatus.INVALID);
                result.failed.add(document);
            });

            return result;
        }


        for (CompletableFuture<?> future : completedFutures) {
            ProjectDetails document = null;

            // 因为上一步全部克隆任务都已经完成，理论上，这一步不可能发生异常
            // 因此忽略这个地方的失败
            try {
                document = (ProjectDetails) future.get();
            } catch (InterruptedException | ExecutionException e) {
                assert false;
            }

            if (document.getStatus() == ProjectStatus.INVALID) {
                result.failed.add(document);
            } else {
                result.success.add(document);
            }
        }

        return result;
    }

    // 帮助方法

    @Override
    protected void failed(ProjectDetails document, Throwable t) {
        super.failed(document, t);
        document.setStatus(ProjectStatus.INVALID);
    }

    @Override
    protected void complete(ProjectDetails document, Throwable t) {
        if (t != null) {
            failed(document, t);
        }
    }

    /**
     * 异步克隆任务简单封装
     */
    @RequiredArgsConstructor
    private class AsyncCloneRemoteTask implements Supplier<ProjectDetails> {

        private final ProjectDetails document;

        @Override
        public ProjectDetails get() {
            try {
                documentRepositoryCacheService.forceCacheRepository(document);
                success(document);
            } catch (Exception e) {
                failed(document, e);
            }

            return document;
        }
    }

}
