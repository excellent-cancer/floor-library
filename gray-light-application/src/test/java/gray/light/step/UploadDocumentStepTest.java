package gray.light.step;

import gray.light.document.DocumentRepositoryVisitor;
import gray.light.document.entity.Document;
import gray.light.document.service.DocumentRepositoryCacheService;
import gray.light.document.service.DocumentSourceService;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Paths;
import java.util.List;

@CommonsLog
@SpringBootTest("excellent.cancer.fastdfs.enabled=true")
public class UploadDocumentStepTest {

    @Autowired
    private DocumentRepositoryCacheService documentRepositoryCacheService;

    @Autowired
    private DocumentSourceService sourceService;


    @Test
    @DisplayName("上传文件更新")
    @Disabled
    public void uploadTest() {
        UploadDocumentStep uploadStep = new UploadDocumentStep(sourceService);

        List<Document> docs = BatchVisitDocumentRepositoryTest.cloneRemoteRepositories(documentRepositoryCacheService, 1);
        List<DocumentRepositoryVisitor> visitors = new VisitDocumentRepositoryStep(documentRepositoryCacheService).execute(docs).getVisitors();

        Assertions.assertEquals(docs.size(), visitors.size());

        List<DocumentRepositoryVisitor> uploadVisitors = uploadStep.execute(visitors).getVisitors();

        Assertions.assertEquals(docs.size(), uploadVisitors.size());
    }

    @Test
    public void upload() {
        String path = sourceService.updateChapter(Paths.get("/Users/yanjiaxun/IdeaProjects/excellentcancer/floor-applications/gray-light-application/src/main/resources/database.sql"));
        log.error(path);
    }
}
