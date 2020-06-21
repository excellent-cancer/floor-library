package gray.light.step;

import excellent.cancer.floor.repository.LocalRepositoryDatabase;
import excellent.cancer.floor.repository.RepositoryDatabase;
import gray.light.DocumentRepositoryVisitor;
import gray.light.document.entity.Document;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.*;
import perishing.constraint.treasure.chest.converter.Converters;

import java.io.IOException;
import java.util.List;

@CommonsLog
public class BatchVisitDocumentRepositoryTest {

    private static RepositoryDatabase<Long, Long> database;

    @BeforeAll
    static void setupRepositoryDatabase() throws IOException {
        LocalRepositoryDatabase<Long> localDatabase = LocalRepositoryDatabase.ofWithTemp(Converters.LONG_STRING);
        log.info("Repository Location: " + localDatabase.getLocation());
        database = localDatabase;
    }

    @Test
    @DisplayName("浏览-遍历文档仓库")
    @Disabled
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
     * 克隆指定数量的文档仓库至{@link RepositoryDatabase}中，并返回成功的文档
     *
     * @param database 仓库数据库
     * @param count    克隆数量
     * @return 成功克隆的文档
     */
    public static List<Document> cloneRemoteRepositories(RepositoryDatabase<Long, Long> database, int count) {
        return BatchCloneRemoteRepositoryStepTest.cloneRepositories(database, count).getSuccess();
    }

}
