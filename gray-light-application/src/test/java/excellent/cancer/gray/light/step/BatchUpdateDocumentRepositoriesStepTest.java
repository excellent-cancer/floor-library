package excellent.cancer.gray.light.step;

import excellent.cancer.gray.light.component.SuperOwnerRandomService;
import excellent.cancer.gray.light.document.DocumentRepositoryDatabase;
import excellent.cancer.gray.light.jdbc.entities.Document;
import excellent.cancer.gray.light.jdbc.entities.DocumentStatus;
import excellent.cancer.gray.light.jdbc.entities.OwnerProject;
import excellent.cancer.gray.light.service.DocumentRelationService;
import excellent.cancer.gray.light.service.RepositoryService;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.sql.Time;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@CommonsLog
@SpringBootTest(value = {"excellent.cancer.jdbc.enabled=true", "excellent.cancer.fastdfs.enabled=true"})
public class BatchUpdateDocumentRepositoriesStepTest {

    @Autowired
    private SuperOwnerRandomService superOwnerRandomService;

    @Autowired
    private DocumentRelationService documentRelationService;

    @Autowired
    private RepositoryService repositoryService;

    private static final String TEMP_PATH = BatchCloneRemoteRepositoryStepTest.class.getSimpleName();

    private static DocumentRepositoryDatabase database;

    @BeforeAll
    static void setupRepositoryDatabase() throws IOException {
        database = new DocumentRepositoryDatabase(TEMP_PATH, true);
        log.info("Repository Location: " + database.getLocation());
    }

    @Test
    @DisplayName("遍历更新文档、目录、文章")
    public void updateTest() {
        OwnerProject savedProject = superOwnerRandomService.saveOwnerProjectOptionally();
        Assertions.assertNotNull(savedProject.getId());

    }

    @Test
    @DisplayName("添加新文档")
    public void addNewDocumentRepoWithoutUpload() {
        Document document = Document.builder().
                title("文档库" + new Time(new Date().getTime())).
                projectId(28L).
                description("情况可能不妙，但至少我们拥有彼此。").
                documentStatus(DocumentStatus.EMPTY).
                repoUrl("https://github.com/XyParaCrim/test-document.git").
                build();

        repositoryService.document().save(document);

        Assertions.assertNotNull(document.getId());

        List<Document> documents = new BatchCloneRemoteRepositoryStep(database).execute(Collections.singletonList(document)).getSuccess();

        Assertions.assertEquals(1, documents.size());

        VisitDocumentRepositoryStep step = new VisitDocumentRepositoryStep(database);
        VisitDocumentRepositoryStep.Result result = step.execute(documents);

        BatchUpdateDocumentRepositoriesStep updateDocumentRepositoriesStep = new BatchUpdateDocumentRepositoriesStep(documentRelationService);

        document.setDocumentStatus(DocumentStatus.SYNC);

        updateDocumentRepositoriesStep.execute(result.getVisitors(), documents);
    }

}
