package floor.file.storage.config;


import floor.file.storage.FileStorageProvider;
import floor.file.storage.simple.SimpleFileStorageProvider;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 针对文件存储的配置属性
 *
 * @author XyParaCrim
 */
@Data
@ConfigurationProperties(prefix = FloorFileStorageProperties.FILE_STORAGE_PREFIX)
public class FloorFileStorageProperties {

    public static final String FILE_STORAGE_PREFIX = "floor.file.storage.details";

    /**
     * 存储服务器类型
     */
    private String storageType = "simple";


    /**
     * 配置文件的位置
     */
    private String configLocation;

    /**
     * 是否要检查配置路径
     */
    private boolean checkConfigLocation = false;

    /**
     * 文件储藏的默认提供类
     */
    private Class<? extends FileStorageProvider> providerClass = SimpleFileStorageProvider.class;

}
