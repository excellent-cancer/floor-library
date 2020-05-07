package excellent.cancer.gray.light.document;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.apachecommons.CommonsLog;
import perishing.constraint.io.FileSupport;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地文档仓库数据库，即一个git仓库的管理器
 *
 * @author XyParaCrim
 */
@CommonsLog
public class DocumentRepositoryDatabase {

    private static final String TEMP = "document-repositories-database";

    private static final int COUNT_BIT = 4;

    private static final int REPOSITORIES_COUNT = 2 << COUNT_BIT;

    @Getter
    private final File location;

    private final boolean requireRemove;

    private final ConcurrentHashMap<String, RepositoryOptions> options = new ConcurrentHashMap<>();

    public DocumentRepositoryDatabase(String path, boolean requireRemove) throws IOException {
        this.requireRemove = requireRemove;
        this.location = checkOrCreateTemp(Paths.get(path));
        initSemaphores();
        addShutdownHook();
    }

    private void initSemaphores() throws IOException {
        Files.walk(this.location.toPath()).
                forEach(cellPath -> options.put(cellPath.getFileName().toString(), new RepositoryOptions(cellPath)));
    }

    // 导出部分

    public RepositoryOptions getRepositoryOptions(@NonNull Object id) {
        return options.computeIfAbsent(id.toString(), k -> new RepositoryOptions(location.toPath(), k));
    }

    private void addShutdownHook() {
        if (requireRemove) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.debug("delete document repository database...");
                try {
                    FileSupport.deleteFile(location, true);
                    log.debug("delete document repository database - success");
                } catch (IOException e) {
                    log.error("delete document repository database - failed", e);
                }
            }));
        }
    }

    private static File checkOrCreateTemp(Path dir) throws IOException {
        if (Files.isDirectory(dir)) {
            return dir.toFile();
        }

        try {
            return Files.createDirectory(dir).toFile();
        } catch (IOException e) {
            log.error("Failed to create directory of document repository, and use temp folder instead", e);
        }

        // 使用临时文件夹代替

        return Files.createTempDirectory(TEMP).toFile();
    }

    private File repositoryLocation(String key) {
        return new File(location, key);
    }

}
