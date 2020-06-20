package excellent.cancer.floor.repository;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import perishing.constraint.io.FileSupport;
import perishing.constraint.treasure.chest.converter.Converter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 本地仓库数据库，使用本地文件系统管理Git仓库
 *
 * @author XyParaCrim
 */
@CommonsLog
public class LocalRepositoryDatabase<K> implements RepositoryDatabase<K, Long> {

    private static final int INITIAL_CAPACITY = 128;

    @Getter
    private final File location;

    private final Converter<K, String> converter;

    private final ConcurrentHashMap<K, LocalRepositoryOptions<K>> optionsTable;

    private LocalRepositoryDatabase(Path path, Converter<K, String> converter) throws IOException {
        this.location = useOrCreateTemp(path, getClass().getSimpleName());
        this.converter = converter;
        this.optionsTable = initRepositoryOptionsTable(this.location, this.converter);
    }

    private LocalRepositoryDatabase(Converter<K, String> converter) throws IOException {
        this.location = tempLocation(getClass().getSimpleName());
        this.converter = converter;
        this.optionsTable = initRepositoryOptionsTable(this.location, this.converter);
    }


    /**
     * 返回指定key的仓库操作对象
     *
     * @param key 仓库Id
     * @return 仓库操作对象
     */
    @Override
    @SneakyThrows
    public RepositoryOptions<K, Long> repositoryOptions(K key) {
        return optionsTable.computeIfAbsent(key, k -> {
            String fileName = converter.to(k);

            return new LocalRepositoryOptions<>(key, location.toPath().resolve(fileName));
        });
    }

    /**
     * 通过指定的远程仓库地址，添加新的仓库操作。如果已经存在，则尝试获取
     * 写许可，删除仓库，然后重新克隆远程仓库。若长时间未获取写许可，则会
     * 抛出{@link OccupiedPermissionException}
     *
     * @param key 仓库Id
     * @param remote 远程仓库地址
     * @return 仓库操作对象
     * @throws OccupiedPermissionException 需要删除旧仓库的情况下，若长时间未获取写许可
     */
    @Override
    @SneakyThrows
    public RepositoryOptions<K, Long> addRepositoryOptions(K key, String remote) {
        return optionsTable.compute(key, (k, repositoryOptions) -> {
            if (repositoryOptions == null) {
                String fileName = converter.to(k);

                return new LocalRepositoryOptions<>(k, location.toPath().resolve(fileName), remote);
            } else {
                // 获取文档仓库的使用权限
                // 执行线程若中断，则立即退出
                Optional<Long> writePermission = repositoryOptions.writePermission();

                if (writePermission.isPresent()) {
                    try {
                        // 首先清除本地旧的仓库，然后再克隆
                        repositoryOptions.resetRemote(remote);

                        return repositoryOptions;
                    } finally {
                        repositoryOptions.cancelWritePermission(writePermission.get());
                    }

                } else {
                    throw new OccupiedPermissionException("Failed to acquire write permission of repository options since the permission has been occupied");
                }
            }
        });
    }


    /**
     * 返回指定目录的仓库数据库
     *
     * @param pathStr 仓库数据库位置路径字符串
     * @param converter 文件转换成仓库Id
     * @param <K> 仓库Id的类型
     * @return 本地仓库数据库实例
     * @throws IOException 操作本地文件发生错误
     */
    public static <K> LocalRepositoryDatabase<K> of(String pathStr, Converter<K, String> converter) throws IOException {
        return new LocalRepositoryDatabase<>(Paths.get(pathStr), converter);
    }

    /**
     * 使用临时目录作为仓库数据库
     *
     * @param converter 文件转换成仓库Id
     * @param <K> 仓库Id的类型
     * @return 本地仓库数据库实例
     * @throws IOException 操作本地文件发生错误
     */
    public static <K> LocalRepositoryDatabase<K> ofWithTemp(Converter<K, String> converter) throws IOException {
        return new LocalRepositoryDatabase<>(converter);
    }

    /**
     * 检查指定路径是否是文件夹，若不是或者无法使用，则使用临时文件夹，
     * 如果使用临时文件夹，则会在shutdown时，删除文件夹
     *
     * @param path 指定仓库目录
     * @param temp 临时文件夹名字
     * @return 仓库位置
     * @throws IOException 无法创建临时文件夹
     */
    private static File useOrCreateTemp(Path path, String temp) throws IOException {
        File location;
        if (Files.isDirectory(path)) {
            location = path.toFile();
        } else {
            try {
                location = Files.createDirectory(path).toFile();
            } catch (IOException e) {
                log.error("Failed to create directory of document repository, and use temp folder instead", e);

                // 使用临时文件夹代替
                location = tempLocation(temp);
            }
        }

        return location;
    }

    /**
     * 使用指定目录名创建临时目录，程序shutdown会删除目录
     *
     * @param temp 临时目录名
     * @return 临时目录位置
     * @throws IOException 无法创建临时目录
     */
    private static File tempLocation(String temp) throws IOException {
        File location = Files.createTempDirectory(temp).toFile();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.debug("delete document repository database...");
            try {
                FileSupport.deleteFile(location, true);
                log.debug("delete document repository database - success");
            } catch (IOException e) {
                log.error("delete document repository database - failed", e);
            }
        }));

        return location;
    }

    /**
     * 初始化所有仓库，返回存储所有仓库操作的ConcurrentHashMap
     *
     * @param location 仓库位置
     * @param converter 文件转换成仓库Id
     * @param <K> 仓库Id的类型
     * @return 所有仓库操作的ConcurrentHashMap
     * @throws IOException 遍历仓库位置时，发生错误
     */
    private static <K> ConcurrentHashMap<K, LocalRepositoryOptions<K>> initRepositoryOptionsTable(File location, Converter<K, String> converter) throws IOException {
        ConcurrentHashMap<K, LocalRepositoryOptions<K>> optionsTable = new ConcurrentHashMap<>(INITIAL_CAPACITY);

        List<Path> allRepo = Files.walk(location.toPath()).collect(Collectors.toList());
        for (Path repo : allRepo) {
            K id = converter.from(repo.getFileName().toString());
            LocalRepositoryOptions<K> localOptions = new LocalRepositoryOptions<>(id, repo);

            optionsTable.put(id, localOptions);

        }

        return optionsTable;
    }


}
