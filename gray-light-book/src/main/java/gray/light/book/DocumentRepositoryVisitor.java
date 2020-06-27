package gray.light.book;

import gray.light.book.customizer.BookCatalogCustomizer;
import gray.light.book.entity.BookCatalog;
import gray.light.book.entity.BookCatalogFolder;
import gray.light.book.entity.BookChapter;
import gray.light.owner.entity.ProjectDetails;
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
    private final ProjectDetails document;

    @Getter
    private final List<BookCatalog> catalogs = new LinkedList<>();

    @Getter
    private final List<Tuple2<BookChapter, Path>> chapters = new LinkedList<>();

    @Getter
    private Throwable failed = null;

    private final Map<Path, BookCatalog> catalogTable = new HashMap<>();

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        String name = filename(dir);
        if (ignored(name)) {
            return FileVisitResult.SKIP_SUBTREE;
        }

        String parentUid = parentUid(dir, BookCatalogFolder.CATALOG);
        String title = parentUid.equals(BookCatalogCustomizer.ROOT) ? document.getType() + "#" + document.getOriginId(): name;

        BookCatalog catalog = BookCatalog.builder().
                title(title).
                folder(BookCatalogFolder.EMPTY).
                ownerProjectId(document.getOriginId()).
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

        BookChapter chapter = BookChapter.builder().
                catalogUid(parentUid(file, BookCatalogFolder.CHAPTER)).
                ownerProjectId(document.getOriginId()).
                title(trimSuffix(name)).
                uid(UUID.randomUUID().toString()).
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

    private String parentUid(Path path, BookCatalogFolder folder) {
        if (catalogTable.containsKey(path.getParent())) {
            BookCatalog catalog = catalogTable.get((path.getParent()));
            catalog.setFolder(folder);
            return catalog.getUid();
        } else {
            return BookCatalogCustomizer.ROOT;
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

    public static DocumentRepositoryVisitor failedVisitor(ProjectDetails document, Throwable e) {
        DocumentRepositoryVisitor visitor = new DocumentRepositoryVisitor(document);
        visitor.failed = e;
        return visitor;
    }

}
