package excellent.cancer.gray.light.document;

import excellent.cancer.gray.light.jdbc.entities.Document;
import excellent.cancer.gray.light.jdbc.entities.DocumentCatalog;
import excellent.cancer.gray.light.jdbc.entities.DocumentCatalogFolder;
import excellent.cancer.gray.light.jdbc.entities.DocumentChapter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * 浏览文档仓库的树形结构，将树形结构信息提取出来
 *
 * @author XyParaCrim
 */
@CommonsLog
@RequiredArgsConstructor
public class DocumentRepositoryVisitor implements FileVisitor<Path> {

    @Getter
    private final Document document;

    @Getter
    private final List<DocumentCatalog> catalogs = new LinkedList<>();

    @Getter
    private final List<Tuple2<DocumentChapter, Path>> chapters = new LinkedList<>();

    @Getter
    private Throwable failed = null;

    private final Map<Path, DocumentCatalog> catalogTable = new HashMap<>();

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        String name = filename(dir);
        if (ignored(name)) {
            return FileVisitResult.SKIP_SUBTREE;
        }

        String parentUid = parentUid(dir, DocumentCatalogFolder.CATALOG);
        String title = parentUid.equals(DocumentCatalog.ROOT) ? document.getTitle() : name;

        DocumentCatalog catalog = DocumentCatalog.builder().
                title(title).
                folder(DocumentCatalogFolder.EMPTY).
                documentId(document.getId()).
                projectId(document.getProjectId()).
                parentUid(parentUid).
                uid(UUID.randomUUID().toString()).
                build();

        catalogTable.put(dir, catalog);
        catalogs.add(catalog);

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        String name = filename(file);
        if (ignored(name) || notMarkdown(name)) {
            return FileVisitResult.SKIP_SUBTREE;
        }

        DocumentChapter chapter = DocumentChapter.builder().
                catalogUid(parentUid(file, DocumentCatalogFolder.CHAPTER)).
                documentId(document.getId()).
                title(trimSuffix(name)).
                build();

        chapters.add(Tuples.of(chapter, file));

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        String name = filename(file);
        if (ignored(name) || notMarkdown(name)) {
            return FileVisitResult.CONTINUE;
        }

        this.failed = exc;
        return FileVisitResult.TERMINATE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

    private String parentUid(Path path, DocumentCatalogFolder folder) {
        if (catalogTable.containsKey(path.getParent())) {
            DocumentCatalog catalog = catalogTable.get((path.getParent()));
            catalog.setFolder(folder);
            return catalog.getUid();
        } else {
            return DocumentCatalog.ROOT;
        }
    }

    // 公共帮助方法

    private static boolean ignored(String filename) {
        return filename.isEmpty() || filename.startsWith(".");
    }

    private static boolean notMarkdown(String filename) {
        int index = filename.lastIndexOf(".");
        return index == -1 || !"md".equals(filename.substring(index + 1));
    }

    private static String filename(Path path) {
        return path.getFileName().toString();
    }

    private static String trimSuffix(String filename) {
        return filename.substring(0, filename.lastIndexOf("."));
    }

    public static DocumentRepositoryVisitor failedVisitor(Document document, Throwable e) {
        DocumentRepositoryVisitor visitor = new DocumentRepositoryVisitor(document);
        visitor.failed = e;
        return visitor;
    }

}
