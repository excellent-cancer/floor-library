package floor.repository.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 启动git仓库管理的配置
 *
 * @author XyParaCrim
 */
@Data
@ConfigurationProperties(prefix = FloorRepositoryProperties.REPOSITORY_PREFIX)
public class FloorRepositoryProperties {

    public static final String REPOSITORY_PREFIX = "floor.repository.details";

    private String databaseLocation;

}
