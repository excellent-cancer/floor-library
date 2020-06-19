package excellent.cancer.gray.light.step;

import excellent.cancer.gray.light.document.DocumentRepositoryDatabase;
import excellent.cancer.gray.light.document.DocumentRepositoryVisitor;
import excellent.cancer.gray.light.jdbc.entities.Document;
import excellent.cancer.gray.light.utils.FastdfsClient;
import lombok.extern.apachecommons.CommonsLog;
import org.csource.common.MyException;
import org.csource.fastdfs.TrackerClient;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static excellent.cancer.gray.light.step.BatchVisitDocumentRepositoryTest.cloneRemoteRepositories;

@CommonsLog
@SpringBootTest("excellent.cancer.fastdfs.enabled=true")
public class UploadDocumentStepTest {

    private static final String TEMP_PATH = UploadDocumentStep.class.getSimpleName();

    private static DocumentRepositoryDatabase database;

    @Autowired
    private TrackerClient trackerClient;

    @BeforeAll
    static void setupRepositoryDatabase() throws IOException {
        database = new DocumentRepositoryDatabase(TEMP_PATH, true);
        log.info("Repository Location: " + database.getLocation());
    }

    @Test
    @DisplayName("上传文件更新")
    @Disabled
    public void uploadTest() {
        UploadDocumentStep uploadStep = new UploadDocumentStep(trackerClient);

        List<Document> docs = cloneRemoteRepositories(database, 1);
        List<DocumentRepositoryVisitor> visitors = new VisitDocumentRepositoryStep(database).execute(docs).getVisitors();

        Assertions.assertEquals(docs.size(), visitors.size());

        List<DocumentRepositoryVisitor> uploadVisitors = uploadStep.execute(visitors).getVisitors();

        Assertions.assertEquals(docs.size(), uploadVisitors.size());
    }

    @Test
    public void upload() throws IOException, MyException {

        try (FastdfsClient client = new FastdfsClient(trackerClient)) {
            String path = client.uploadMarkdown(Paths.get("/Users/yanjiaxun/IdeaProjects/excellentcancer/floor-applications/gray-light-application/src/main/resources/database.sql"));
            log.error(path);
        }

    }
}