package org.excellent.cancer.experimental.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 默认自动配置
 *
 * @author XyParaCrim
 */
@Configuration
public class ExperimentalAutoConfiguration {

    @Bean
    public ExcellentCancerProperties excellentCancerProperties() {
        return new ExcellentCancerProperties();
    }

}
