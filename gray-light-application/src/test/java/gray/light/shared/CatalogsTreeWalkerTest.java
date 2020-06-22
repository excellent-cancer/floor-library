package gray.light.shared;

import gray.light.document.DocumentRepositoryVisitor;
import gray.light.business.*;
import gray.light.document.entity.Document;
import gray.light.document.entity.DocumentCatalog;
import gray.light.document.entity.DocumentChapter;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import perishing.constraint.io.FileSupport;
import reactor.util.function.Tuple2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 对{@link CatalogsTreeWalker}进行单元测试，主要是walker是否能够
 * 通过一组目录和一组章节转换成树形se对象
 */
@CommonsLog
public class CatalogsTreeWalkerTest {

    private static final int DEPTH = 3;

    private static final int COUNT = 3;

    private static final String DOCUMENT_TITLE = "测试仓库";

    @Test
    @DisplayName("本地新建仓库 - 转换成SE对象")
    public void test() throws IOException {
        Path root = createTempDocumentRepository();
        DocumentRepositoryVisitor visitor = walkDocumentRepositoryTree(root);

        List<DocumentCatalog> catalogs = visitor.getCatalogs();
        List<DocumentChapter> chapters = visitor.getChapters().stream().map(Tuple2::getT1).collect(Collectors.toList());

        ContainsCatalogCatalogBo se = CatalogsTreeWalker.walk(catalogs, chapters);

        Assertions.assertEquals(DOCUMENT_TITLE, se.getData().getTitle());
        Assertions.assertEquals(DocumentCatalog.ROOT, se.getData().getParentUid());

        assertionLevel(1, se.getCatalogs());
    }

    private static Path createTempDocumentRepository() throws IOException {
        Path root = Files.createTempDirectory(CatalogsTreeWalkerTest.class.getSimpleName());
        createCatalogs(1, root);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                FileSupport.deleteFile(root.toFile(), true);
            } catch (Exception e) {
                log.error("失败删除文件", e);
            }
        }));

        return root;
    }

    private static DocumentRepositoryVisitor walkDocumentRepositoryTree(Path root) throws IOException {
        Document document = Document.builder().title(DOCUMENT_TITLE).id(0L).projectId(0L).build();
        DocumentRepositoryVisitor visitor = new DocumentRepositoryVisitor(document);

        Files.walkFileTree(root, visitor);

        return visitor;
    }

    private void assertionLevel(int level, CatalogBo[] se) {
        Set<String> titleSet = new HashSet<>(se.length);
        for (int i = 0; i < se.length; i++) {
            titleSet.add(name(level, i));
        }

        for (CatalogBo catalogBo : se) {
            Assertions.assertTrue(titleSet.remove(catalogBo.getData().getTitle()));
            if (level == DEPTH - 1) {
                assertionLevel(level + 1, ((ContainsChapterCatalogBo) catalogBo).getChapters());
            } else {
                assertionLevel(level + 1, ((ContainsCatalogCatalogBo) catalogBo).getCatalogs());
            }
        }

    }

    private void assertionLevel(int level, ChapterBo[] se) {
        Set<String> titleSet = new HashSet<>(se.length);
        for (int i = 0; i < se.length; i++) {
            titleSet.add(name(level, i));
        }

        for (ChapterBo chapterBo : se) {
            Assertions.assertTrue(titleSet.remove(chapterBo.getData().getTitle()));
        }
    }

    private static void createCatalogs(int level, Path parent) throws IOException {
        if (level == DEPTH) {
            for (int i = 0; i < COUNT; i++) {
                Files.createFile(parent.resolve(name(level, i) + ".md"));
            }
        } else {
            for (int i = 0; i < COUNT; i++) {
                createCatalogs(level + 1, Files.createDirectory(parent.resolve(name(level, i))));
            }
        }
    }

    private static String name(int level, int i) {
        return level + "-" + i;
    }

}
