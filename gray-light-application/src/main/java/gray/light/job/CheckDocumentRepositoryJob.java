package gray.light.job;

import gray.light.document.entity.Document;
import gray.light.document.service.DocumentRelationService;
import gray.light.document.service.DocumentRepositoryCacheService;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;
import java.util.ListIterator;

/**
 * 检查文档仓库是否更新，自动拉取最新版本，并存储到数据库
 *
 * @author XyParaCrim
 */
@CommonsLog
public class CheckDocumentRepositoryJob extends QuartzJobBean {

    @Setter
    private DocumentRelationService documentService;

    @Setter
    DocumentRepositoryCacheService documentRepositoryCacheService;

    @Override
    @SneakyThrows
    protected void executeInternal(JobExecutionContext context) {
        List<Document> syncDocument = documentService.allSyncDocument();
        ListIterator<Document> iterator = syncDocument.listIterator();

        while (iterator.hasNext()) {
            Document document = iterator.next();

            documentRepositoryCacheService.updateRepository(document);

            switch (document.getDocumentStatus()) {
                case SYNC:
                case INVALID:
                    iterator.remove();
                    break;
                case NEW:
                    break;
                default:
                    throw new IllegalStateException("Unrecognized document status: " + document.getDocumentStatus());
            }
        }

        if (syncDocument.size() > 0) {
            documentService.batchUpdateDocumentStatus(syncDocument);
        }
    }

}
