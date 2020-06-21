package gray.light.step;

import excellent.cancer.floor.repository.LocalRepositoryDatabase;
import excellent.cancer.floor.repository.RepositoryDatabase;
import excellent.cancer.floor.repository.RepositoryOptions;
import gray.light.document.entity.Document;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.*;
import perishing.constraint.treasure.chest.converter.Converters;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@CommonsLog
public class BatchCloneRemoteRepositoryStepTest {

    private static final int DOCS_COUNT = 20;

    private static RepositoryDatabase<Long, Long> database;

    @BeforeAll
    static void setupRepositoryDatabase() throws IOException {
        LocalRepositoryDatabase<Long> localDatabase = LocalRepositoryDatabase.ofWithTemp(Converters.LONG_STRING);
        log.info("Repository Location: " + localDatabase.getLocation());
        database = localDatabase;
    }

    @Test
    @DisplayName("测试远程克隆仓库到本地")
    @Disabled
    public void cloneRemoteTest() {
        BatchCloneRemoteRepositoryStep.Result result = cloneRepositories(database, DOCS_COUNT);

        Assertions.assertEquals(DOCS_COUNT, result.getSuccess().size());
        Assertions.assertEquals(0, result.getFailed().size());

        result.getSuccess().forEach(document -> {
            RepositoryOptions<Long, Long> options = database.repositoryOptions(document.getId());
            Assertions.assertTrue(Files.isDirectory(options.getLocation().toPath()));
        });

    }

    /**
     * 克隆指定数量的文档仓库至{@link RepositoryDatabase}中，并返回成功的文档
     *
     * @param database 仓库数据库
     * @param count    克隆数量
     * @return 克隆结果
     */
    public static BatchCloneRemoteRepositoryStep.Result cloneRepositories(RepositoryDatabase<Long, Long> database, int count) {
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
