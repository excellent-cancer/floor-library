package excellent.cancer.gray.light.step;

import excellent.cancer.gray.light.document.DocumentRepositoryDatabase;
import excellent.cancer.gray.light.document.RepositoryOptions;
import excellent.cancer.gray.light.jdbc.entities.Document;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@CommonsLog
public class BatchCloneRemoteRepositoryStepTest {

    private static final String TEMP_PATH = BatchCloneRemoteRepositoryStepTest.class.getSimpleName();

    private static final int DOCS_COUNT = 20;

    private static DocumentRepositoryDatabase database;

    @BeforeAll
    static void setupRepositoryDatabase() throws IOException {
        database = new DocumentRepositoryDatabase(TEMP_PATH, true);
        log.info("Repository Location: " + database.getLocation());
    }

    @Test
    @DisplayName("测试远程克隆仓库到本地")
    public void cloneRemoteTest() {
        BatchCloneRemoteRepositoryStep.Result result = cloneRepositories(database, DOCS_COUNT);

        Assertions.assertEquals(DOCS_COUNT, result.getSuccess().size());
        Assertions.assertEquals(0, result.getFailed().size());

        result.getSuccess().forEach(document -> {
            RepositoryOptions options = database.getRepositoryOptions(document.getId());
            Assertions.assertTrue(Files.isDirectory(options.getLocation()));
        });

    }

    /**
     * 克隆指定数量的文档仓库至{@link DocumentRepositoryDatabase}中，并返回成功的文档
     *
     * @param database 仓库数据库
     * @param count    克隆数量
     * @return 克隆结果
     */
    public static BatchCloneRemoteRepositoryStep.Result cloneRepositories(DocumentRepositoryDatabase database, int count) {
        List<Document> docs = LongStream.
                range(0, count).
                mapToObj(
                        i ->
                                Document.
                                        builder().
                                        id(i).
                                        title("文档库" + new Time(new Date().getTime())).
                                        description("情况可能不妙，但至少我们拥有彼此。").
                                        repoUrl("https://github.com/XyParaCrim/test-document.git").
                                        build()
                ).
                collect(Collectors.toList());


        return new BatchCloneRemoteRepositoryStep(database).execute(docs);
    }

}
