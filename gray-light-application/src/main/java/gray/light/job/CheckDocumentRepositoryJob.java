package gray.light.job;

import excellent.cancer.floor.repository.RepositoryDatabase;
import excellent.cancer.floor.repository.RepositoryOptions;
import gray.light.document.entity.Document;
import gray.light.document.entity.DocumentStatus;
import gray.light.service.DocumentRelationService;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

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
    private RepositoryDatabase<Long, Long> repositoryDatabase;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) {
        List<Document> syncDocument = documentService.allSyncDocument();
        ListIterator<Document> iterator = syncDocument.listIterator();

        while (iterator.hasNext()) {
            Document document = iterator.next();
            RepositoryOptions<Long, Long> options = getRepositoryOptions(document);

            if (options != null) {
                Optional<Long> stamp = options.writePermission();
                if (stamp.isPresent()) {
                    try {
                        // 比较版本差异
                        if (options.hasUpdate()) {
                            options.updateLocal();
                            document.setDocumentStatus(DocumentStatus.NEW);

                            continue;
                        }

                    } finally {
                        options.cancelWritePermission(stamp.get());
                    }
                }
            }

            iterator.remove();
        }

        if (syncDocument.size() > 0) {
            documentService.batchUpdateDocumentStatus(syncDocument);
        }
    }

    private RepositoryOptions<Long, Long> getRepositoryOptions(Document document) {
        try {
            return repositoryDatabase.repositoryOptions(document.getId());
        } catch (Exception e) {
            log.error("Unable to check document repository updates: " + document, e);
        }

        return null;
    }

}
