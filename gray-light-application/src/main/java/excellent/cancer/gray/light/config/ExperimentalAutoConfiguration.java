package excellent.cancer.gray.light.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

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


    @Configuration
    @EnableWebFlux
    public static class GlobCorsConf implements WebFluxConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowCredentials(true)
                    .allowedOrigins("*")
                    .allowedHeaders("*")
                    .allowedMethods("*")
                    .exposedHeaders(HttpHeaders.SET_COOKIE);
        }
    }

}
