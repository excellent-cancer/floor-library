package excellent.cancer.floor.repository;

import lombok.Getter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import perishing.constraint.io.FileSupport;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

class LocalRepositoryOptions<K> implements RepositoryOptions<K, Long> {

    private static final long DEFAULT_TIMEOUT = 30L;

    private static final TimeUnit DEFAULT_UNIT = TimeUnit.SECONDS;

    private final StampedLock stampedLock = new StampedLock();

    @Getter
    private final K id;

    @Getter
    private final File location;

    @Getter
    private Git git;

    LocalRepositoryOptions(K id, Path path) throws IOException {
        this.id = id;
        this.location = path.toFile();
        this.git = Git.open(this.location);
    }

    LocalRepositoryOptions(K id, Path path, String remote) throws IOException, GitAPIException {
        this.id = id;
        this.location = path.toFile();
        resetRemote(remote);
    }

    void resetRemote(String remote) throws IOException, GitAPIException {
        // 首先清除本地旧的仓库，然后再克隆
        FileSupport.deleteFile(location, true);
        this.git = GitSupport.cloneRemote(location.toPath(), remote);
    }

    @Override
    public Optional<Long> readPermission() throws InterruptedException {
        long stamp = stampedLock.tryReadLock(DEFAULT_TIMEOUT, DEFAULT_UNIT);
        return stampToOptional(stamp);
    }

    @Override
    public Optional<Long> writePermission() throws InterruptedException {
        long stamp = stampedLock.tryReadLock(DEFAULT_TIMEOUT, DEFAULT_UNIT);
        return stampToOptional(stamp);
    }

    @Override
    public void cancelReadPermission(Long permission) {
        stampedLock.unlockRead(permission);
    }

    @Override
    public void cancelWritePermission(Long permission) {
        stampedLock.unlockWrite(permission);
    }

    private static Optional<Long> stampToOptional(long stamp) {
        return stamp == 0L ? Optional.empty() : Optional.of(stamp);
    }
}
