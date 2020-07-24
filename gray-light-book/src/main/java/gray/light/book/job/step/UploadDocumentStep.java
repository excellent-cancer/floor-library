package gray.light.book.job.step;

import gray.light.book.DocumentRepositoryVisitor;
import gray.light.book.entity.BookChapter;
import gray.light.book.service.BookSourceService;
import gray.light.owner.entity.ProjectDetails;
import gray.light.owner.entity.ProjectStatus;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.util.function.Tuple2;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 将文档仓库遍历的结果上传到文件服务器
 *
 * @author XyParaCrim
 */
@Slf4j
@RequiredArgsConstructor
public class UploadDocumentStep {

    @RequiredArgsConstructor
    public static class Result {

        @Getter
        private final List<DocumentRepositoryVisitor> visitors;

    }

    @NonNull
    private final BookSourceService bookSourceService;

    /**
     * 上传文档章节到服务器。执行上传使用了{@link ForkJoinTask}的迭代任务，其中
     * 如果任意文件上传失败，则将其移除
     *
     * @param visitors 文件浏览器
     * @return 成功的文件浏览器
     */
    public Result execute(@NonNull List<DocumentRepositoryVisitor> visitors) {

        final ConcurrentLinkedQueue<DocumentRepositoryVisitor> resultVisitors = new ConcurrentLinkedQueue<>();

        ForkJoinTask.invokeAll(
                visitors.
                        stream().
                        map(visitor -> new UploadedDocumentTask(bookSourceService, visitor, resultVisitors)).
                        toArray(UploadedDocumentTask[]::new)
        );


        return new Result(new ArrayList<>(resultVisitors));
    }

    /**
     * 一个文档的上传任务，执行时，会将任务拆分成单章节上传
     */
    @RequiredArgsConstructor
    private static class UploadedDocumentTask extends RecursiveAction {

        private final BookSourceService bookSourceService;

        private final DocumentRepositoryVisitor visitor;

        private final Collection<DocumentRepositoryVisitor> visitors;

        private final AtomicBoolean failed = new AtomicBoolean();

        @Override
        protected void compute() {
            invokeAll(
                    visitor.getChapters().
                            stream().
                            map(tuple -> new UploadedChaptersTask(this, tuple)).
                            toArray(UploadedChaptersTask[]::new)
            );
            if (failed.get()) {
                ProjectDetails uploadFailedDoc = visitor.getDocument();
                uploadFailedDoc.setStatus(ProjectStatus.INVALID);
                log.error("Failed to upload document repository: {}", uploadFailedDoc);
            } else {
                visitor.getDocument().setStatus(ProjectStatus.SYNC);
                visitors.add(visitor);
            }
        }
    }

    /**
     * 单章节上任务
     */
    @RequiredArgsConstructor
    private static class UploadedChaptersTask extends RecursiveAction {

        private final UploadedDocumentTask task;

        private final Tuple2<BookChapter, Path> chapterPair;

        @Override
        protected void compute() {
            if (!task.failed.get()) {
                BookChapter chapter = chapterPair.getT1();
                try {
                    String url = task.bookSourceService.updateChapter(chapterPair.getT2());

                    log.info("Successfully uploaded a chapter file: { name: {}, downloadLink: {} }", chapter.getTitle(), url);

                    chapter.setDownloadLink(url);
                } catch (Exception e) {
                    // 出现其中一个章节上传文件失败，需要将之前成功上传的事件加入失败队列
                    log.error("Failed to upload document to server and try to store uploaded event to failed queue: " + task.visitor.getDocument(), e);
                    task.failed.compareAndSet(false, true);
                }
            }
        }
    }
}
