package floor.file.storage.simple;

import floor.file.storage.FileStorage;
import floor.file.storage.FileStorageProvider;
import floor.file.storage.FileStorageProviderException;
import floor.file.storage.config.FloorFileStorageProperties;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerClient;

/**
 * 初始化fastdfs的tacker-client对象，用于与fastdfs的tracker-server通讯
 *
 * @author XyParaCrim
 */
public class SimpleFileStorageProvider implements FileStorageProvider {

    /**
     * 初始化fastdfs的全局配置，另外，初始化全局{@link org.csource.fastdfs.TrackerGroup}实例，
     * 从而获取{@link TrackerClient}与文件服务器通讯的客户端
     *
     * @param properties spring属性
     * @return 文件服务器通讯的客户端
     */
    @Override
    public FileStorage fileStorage(FloorFileStorageProperties properties) {
        try {
            ClientGlobal.initByProperties(properties.getConfigLocation());

            return new SimpleFileStorage(new TrackerClient());
        } catch (Exception e) {
            throw new FileStorageProviderException("Failed to initialize client of fastdfs", e);
        }
    }

    public static String storageType() {
        return "simple";
    }
}
