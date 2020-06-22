package gray.light.step;

import gray.light.document.DocumentRepositoryVisitor;
import gray.light.document.entity.Document;
import gray.light.document.entity.DocumentCatalog;
import gray.light.document.entity.DocumentChapter;
import gray.light.document.service.DocumentRelationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 将最新上传文档和上传文档发生错误的文档状态，新增文档目录、新增文章节
 * 更新至数据库
 *
 * @author XyParaCrim
 */
@RequiredArgsConstructor
public class BatchUpdateDocumentRepositoriesStep {

    @NonNull
    private final DocumentRelationService documentService;

    public void execute(@NonNull List<DocumentRepositoryVisitor> visitors, @NonNull List<Document> emptyDocument) {
        List<DocumentCatalog> catalogs = catalogs(visitors);
        List<DocumentChapter> chapters = chapters(visitors);

        // 更新文档状态
        documentService.batchUpdateDocumentRepositories(emptyDocument, catalogs, chapters);
    }

    private List<DocumentCatalog> catalogs(List<DocumentRepositoryVisitor> visitors) {
        return visitors.
                stream().
                flatMap(visitor -> visitor.getCatalogs().stream()).
                collect(Collectors.toList());
    }

    private List<DocumentChapter> chapters(List<DocumentRepositoryVisitor> visitors) {
        return visitors.
                stream().
                flatMap(visitor -> visitor.getChapters().stream().map(Tuple2::getT1)).
                collect(Collectors.toList());
    }
}
