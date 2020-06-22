package gray.light.step;

import floor.repository.LocalRepositoryDatabase;
import floor.repository.RepositoryDatabase;
import gray.light.document.DocumentRepositoryVisitor;
import gray.light.document.entity.Document;
import gray.light.document.service.DocumentRepositoryCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import perishing.constraint.treasure.chest.converter.Converters;

import java.io.IOException;
import java.util.List;

@CommonsLog
@RequiredArgsConstructor
public class BatchVisitDocumentRepositoryTest {

    private final DocumentRepositoryCacheService documentRepositoryCacheService;

    @Test
    @DisplayName("浏览-遍历文档仓库")
    @Disabled
    public void visitTest() {
        List<Document> documents = cloneRemoteRepositories(documentRepositoryCacheService, 1);

        VisitDocumentRepositoryStep step = new VisitDocumentRepositoryStep(documentRepositoryCacheService);

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
     * @param documentRepositoryCacheService 仓库数据库
     * @param count    克隆数量
     * @return 成功克隆的文档
     */
    public static List<Document> cloneRemoteRepositories(DocumentRepositoryCacheService documentRepositoryCacheService, int count) {
        return BatchCloneRemoteRepositoryStepTest.cloneRepositories(documentRepositoryCacheService, count).getSuccess();
    }

}
