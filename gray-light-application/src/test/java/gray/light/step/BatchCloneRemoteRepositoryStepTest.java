package gray.light.step;

import floor.repository.RepositoryDatabase;
import gray.light.document.entity.Document;
import gray.light.document.service.DocumentRepositoryCacheService;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@CommonsLog
public class BatchCloneRemoteRepositoryStepTest {

    private static final int DOCS_COUNT = 20;

    private DocumentRepositoryCacheService documentRepositoryCacheService;

/*    @BeforeAll
    static void setupRepositoryDatabase() throws IOException {
        LocalRepositoryDatabase<Long> localDatabase = LocalRepositoryDatabase.ofWithTemp(Converters.LONG_STRING);
        log.info("Repository Location: " + localDatabase.getLocation());
        database = localDatabase;
    }*/

    @Test
    @DisplayName("测试远程克隆仓库到本地")
    @Disabled
    public void cloneRemoteTest() {
        BatchCloneRemoteRepositoryStep.Result result = cloneRepositories(documentRepositoryCacheService, DOCS_COUNT);

        Assertions.assertEquals(DOCS_COUNT, result.getSuccess().size());
        Assertions.assertEquals(0, result.getFailed().size());

        result.getSuccess().forEach(document -> Assertions.assertTrue(documentRepositoryCacheService.availableRepository(document)));

    }

    /**
     * 克隆指定数量的文档仓库至{@link RepositoryDatabase}中，并返回成功的文档
     *
     * @param documentRepositoryCacheService 仓库数据库
     * @param count    克隆数量
     * @return 克隆结果
     */
    public static BatchCloneRemoteRepositoryStep.Result cloneRepositories(DocumentRepositoryCacheService documentRepositoryCacheService, int count) {
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


        return new BatchCloneRemoteRepositoryStep(documentRepositoryCacheService).execute(docs);
    }

}
