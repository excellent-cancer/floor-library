package gray.light.book.job;

import gray.light.book.entity.BookCatalog;
import gray.light.book.entity.BookChapter;
import gray.light.book.service.BookRepositoryCacheService;
import gray.light.book.service.BookService;
import gray.light.book.service.BookSourceService;
import gray.light.owner.entity.ProjectDetails;
import gray.light.owner.entity.ProjectStatus;
import gray.light.owner.service.ProjectDetailsService;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import perishing.constraint.jdbc.Page;

import java.util.List;
import java.util.ListIterator;
import java.util.function.Supplier;

/**
 * 检查文档仓库是否更新，自动拉取最新版本，并存储到数据库
 *
 * @author XyParaCrim
 */
@CommonsLog
public class CheckDocumentRepositoryJob extends QuartzJobBean {

    @Setter
    private ProjectDetailsService projectDetailsService;

    @Setter
    private BookRepositoryCacheService bookRepositoryCacheService;

    @Setter
    private BookService bookService;

    @Setter
    private BookSourceService bookSourceService;

    @Setter
    private Supplier<List<ProjectDetails>> syncStatusProjectDetails;

    @Override
    @SneakyThrows
    protected void executeInternal(JobExecutionContext context) {
        List<ProjectDetails> syncDocument = syncStatusProjectDetails.get();
        ListIterator<ProjectDetails> iterator = syncDocument.listIterator();

        while (iterator.hasNext()) {
            ProjectDetails document = iterator.next();

            bookRepositoryCacheService.updateRepository(document);

            switch (document.getStatus()) {
                case SYNC:
                case INVALID:
                    iterator.remove();
                    break;
                case PENDING:
                    break;
                default:
                    throw new IllegalStateException("Unrecognized document status: " + document.getStatus());
            }
        }

        if (syncDocument.size() > 0) {
            for (ProjectDetails details : syncDocument) {
                List<BookChapter> chapters = bookService.bookChapters(details.getOriginId(), Page.unlimited());
                chapters.forEach(chapter -> bookSourceService.deleteChapter(chapter.getDownloadLink()));

                bookService.deleleAllFromOwnerProject(details.getOriginId());
                details.setVersion("");

                // TODO 应该为Pending，为了触发Update，则暂时为INIT
                details.setStatus(ProjectStatus.INIT);
            }

            projectDetailsService.batchUpdateStatusAndVersion(syncDocument);
        }
    }

}
