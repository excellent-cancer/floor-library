package excellent.cancer.gray.light.document;

import excellent.cancer.gray.light.jdbc.entities.Document;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * 对本地仓库操作的实现
 *
 * @author XyParaCrim
 */
public class RepositoryOptions {

    @Getter
    private final Path location;

    @Getter
    private final StampedLock stampedLock = new StampedLock();

    RepositoryOptions(Path location) {
        this.location = location;
    }

    RepositoryOptions(Path parent, String key) {
        this(parent.resolve(key));
    }

    public Path cleanLocation() throws IOException {
        Files.deleteIfExists(location);
        return Files.createDirectory(location);
    }

    public Optional<Long> getWriteLockWithTimeout() throws InterruptedException {
        long stamp = stampedLock.tryWriteLock(30L, TimeUnit.SECONDS);
        return stampToOptional(stamp);
    }

    public Optional<Long> getReadLockWithTimeout() throws InterruptedException {
        long stamp = stampedLock.tryReadLock(30L, TimeUnit.SECONDS);
        return stampToOptional(stamp);
    }

    public void unlockWrite(long stamp) {
        stampedLock.unlockWrite(stamp);
    }

    public void unlockRead(long stamp) {
        stampedLock.unlockRead(stamp);
    }

    public DocumentRepositoryVisitor visitRepository(Document document) throws IOException {
        DocumentRepositoryVisitor visitor = new DocumentRepositoryVisitor(document);
        Files.walkFileTree(location, visitor);

        return visitor;
    }

    private Optional<Long> stampToOptional(long stamp) {
        return stamp == 0L ? Optional.empty() : Optional.of(stamp);
    }

}
