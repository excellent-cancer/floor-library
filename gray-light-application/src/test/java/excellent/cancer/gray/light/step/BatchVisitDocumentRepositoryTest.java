package excellent.cancer.gray.light.step;

import excellent.cancer.gray.light.document.DocumentRepositoryDatabase;
import excellent.cancer.gray.light.document.DocumentRepositoryVisitor;
import excellent.cancer.gray.light.jdbc.entities.Document;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static excellent.cancer.gray.light.step.BatchCloneRemoteRepositoryStepTest.cloneRepositories;

@CommonsLog
public class BatchVisitDocumentRepositoryTest {

    private static final String TEMP_PATH = BatchCloneRemoteRepositoryStepTest.class.getSimpleName();

    private static DocumentRepositoryDatabase database;

    @BeforeAll
    static void setupRepositoryDatabase() throws IOException {
        database = new DocumentRepositoryDatabase(TEMP_PATH, true);
        log.info("Repository Location: " + database.getLocation());
    }

    @Test
    @DisplayName("浏览-遍历文档仓库")
    public void visitTest() {
        List<Document> documents = cloneRemoteRepositories(database, 1);

        VisitDocumentRepositoryStep step = new VisitDocumentRepositoryStep(database);

        step.setFailed((log::error));
        step.setSuccess(log::info);

        VisitDocumentRepositoryStep.Result result = step.execute(documents);

        Assertions.assertEquals(documents.size(), result.getVisitors().size());

        for (DocumentRepositoryVisitor visitor : result.getVisitors()) {

            visitor.getChapters().forEach(log::info);
            visitor.getCatalogs().forEach(log::info);
        }
    }


    /**
     * 克隆指定数量的文档仓库至{@link DocumentRepositoryDatabase}中，并返回成功的文档
     *
     * @param database 仓库数据库
     * @param count    克隆数量
     * @return 成功克隆的文档
     */
    public static List<Document> cloneRemoteRepositories(DocumentRepositoryDatabase database, int count) {
        return cloneRepositories(database, count).getSuccess();
    }

}
