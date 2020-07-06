package floor.repository;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Git仓库操作
 *
 * @author XyParaCrim
 */
public interface RepositoryOptions<K, P> {

    Optional<P> readPermission() throws InterruptedException;

    void cancelReadPermission(P permission);

    Optional<P> writePermission() throws InterruptedException;

    void cancelWritePermission(P permission);

    K getId();

    Git getGit();

    File getLocation();

    boolean equalsVersion(String version) throws IOException;

    String version();

    /**
     * 比较origin/master和master是否存在版本差异
     *
     * @return 是否存在版本差异
     * @throws IOException 获取git文件时，可能发生的错误
     * @throws GitAPIException 执行命令发生错误
     */
    default boolean hasUpdate() throws IOException, GitAPIException {
        // 比较版本差异
        Git git = getGit();
        GitSupport.fetchRemote(git);

        return GitSupport.hasUpdate(git);
    }

    /**
     * 更新版本与远程仓库一致
     *
     * @throws GitAPIException 执行命令发生错误
     */
    default void updateLocal() throws GitAPIException {
        GitSupport.updateLocal(getGit());
    }
}
