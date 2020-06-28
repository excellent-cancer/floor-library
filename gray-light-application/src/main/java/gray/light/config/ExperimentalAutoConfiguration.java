package gray.light.config;

import floor.file.storage.annotation.FloorFileStorage;
import floor.repository.annotation.FloorRepository;
import gray.light.blog.annotation.BlogDomain;
import gray.light.document.annotation.DocumentDomain;
import gray.light.owner.annotation.OwnerDomain;
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
@FloorFileStorage
@FloorRepository
@OwnerDomain
@DocumentDomain
@BlogDomain
public class ExperimentalAutoConfiguration {

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
