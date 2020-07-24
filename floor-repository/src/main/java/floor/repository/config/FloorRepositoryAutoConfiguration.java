package floor.repository.config;

import floor.repository.LocalRepositoryDatabase;
import floor.repository.RepositoryDatabase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import perishing.constraint.treasure.chest.converter.Converters;

import java.io.IOException;

/**
 * 本地仓库的自动配置
 *
 * @author XyParaCrim
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(FloorRepositoryProperties.class)
public class FloorRepositoryAutoConfiguration {

    private final FloorRepositoryProperties properties;

    /**
     * 返回一个repositoryDatabase，用于管理Git仓库
     *
     * @return 返回一个repositoryDatabase，用于管理Git仓库
     * @throws IOException 操作本地文件发生错误
     */
    @Bean
    public RepositoryDatabase<Long, Long> repositoryDatabase() throws IOException {
        // TODO：关于数据的范型：如何从配置中获取呢？
        // TODO: 如何选择base的类型
        return LocalRepositoryDatabase.of(properties.getDatabaseLocation(), Converters.LONG_STRING);
    }

}
