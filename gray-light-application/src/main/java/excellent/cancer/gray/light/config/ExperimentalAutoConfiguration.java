package excellent.cancer.gray.light.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 默认自动配置
 *
 * @author XyParaCrim
 */
@Configuration
@MapperScan("excellent.cancer.gray.light.jdbc.repositories")
public class ExperimentalAutoConfiguration {

    @Bean
    public ExcellentCancerProperties excellentCancerProperties() {
        return new ExcellentCancerProperties();
    }

}
