package excellent.cancer.floor.repository;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author XyParaCrim
 */
public final class GitSupport {

    // 默认使用git ref名

    private static final String REMOTE_REF = "refs/remotes/origin/master";

    private static final String LOCAL_REF = "refs/heads/master";

    /**
     * 比较origin/master和master是否存在版本差异
     *
     * @param git git实例
     * @return 是否存在版本差异
     * @throws IOException 获取git文件时，可能发生的错误
     */
    public static boolean hasUpdate(Git git) throws IOException {
        // 比较版本差异
        Repository repository = git.getRepository();

        Ref local = repository.exactRef(LOCAL_REF);
        Ref remote = repository.exactRef(REMOTE_REF);

        return local.getObjectId().compareTo(remote.getObjectId().toObjectId()) != 0;
    }

    /**
     * 执行远程仓库拉取
     *
     * @param git git实例
     * @throws GitAPIException 执行命令发生错误
     */
    public static void fetchRemote(Git git) throws GitAPIException {
        git.fetch().setCheckFetchedObjects(true).call();
    }

    /**
     * 更新版本与远程仓库一致
     *
     * @param git git实例
     * @throws GitAPIException 执行命令发生错误
     */
    public static void updateLocal(Git git) throws GitAPIException {
        git.reset().setRef(REMOTE_REF).setMode(ResetCommand.ResetType.HARD).call();
    }

    /**
     * 克隆远程仓库
     *
     * @param location 克隆到本地的位置
     * @param remote 远程仓库地址
     * @return git实例
     * @throws GitAPIException 克隆过程中发生错误
     */
    public static Git cloneRemote(Path location, String remote) throws GitAPIException {
        return Git.cloneRepository().
                setURI(remote).
                setDirectory(location.toFile()).
                call();
    }


}
