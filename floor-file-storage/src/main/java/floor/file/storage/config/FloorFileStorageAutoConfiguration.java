package floor.file.storage.config;

import floor.file.storage.FileStorage;
import floor.file.storage.FileStorageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import perishing.constraint.treasure.chest.ReflectTreasureChest;
import perishing.constraint.treasure.chest.StringTreasureChest;

/**
 * 使用{@link FloorFileStorageProperties}自动配置生成{@link FileStorage}Bean。
 *
 * @author XyParaCrim
 */
@RequiredArgsConstructor
@ConditionalOnProperty(name = "floor.file.storage.enabled", matchIfMissing = true)
@EnableConfigurationProperties(FloorFileStorageProperties.class)
public class FloorFileStorageAutoConfiguration implements InitializingBean {

    private final ResourceLoader resourceLoader;

    private final FloorFileStorageProperties properties;

    @Override
    public void afterPropertiesSet() {
        if (properties.isCheckConfigLocation() && StringTreasureChest.hasText(properties.getConfigLocation())) {
            Resource resource = resourceLoader.getResource(properties.getConfigLocation());
            Assert.state(resource.exists(), "Cannot find config location: " + resource + " (please add config file or check your floor-file-storage configuration)");
        }
    }

    /**
     * 暂时只使用providerClass作为生成依据
     *
     * @return 文件储藏
     */
    @Bean
    public FileStorage fileStorage() {
        FileStorageProvider provider = ReflectTreasureChest.newInstance(properties.getProviderClass());
        return provider.fileStorage(properties);
    }
}
