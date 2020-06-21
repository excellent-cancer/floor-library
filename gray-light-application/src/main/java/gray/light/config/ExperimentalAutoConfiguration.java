package gray.light.config;

import excellent.cancer.floor.repository.LocalRepositoryDatabase;
import excellent.cancer.floor.repository.RepositoryDatabase;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import perishing.constraint.treasure.chest.converter.Converters;

import java.io.IOException;

/**
 * 默认自动配置
 *
 * @author XyParaCrim
 */
@Configuration
@ComponentScan("gray.light.document")
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

    @Bean
    public RepositoryDatabase<Long, Long> documentRepositoryDatabase(ExcellentCancerProperties excellentCancerProperties) throws IOException {
        String documentRepositoriesLocation = excellentCancerProperties.getRunning().getDocumentRepositories();

        return LocalRepositoryDatabase.of(documentRepositoriesLocation, Converters.LONG_STRING);
    }

}
