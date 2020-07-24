package gray.light.config;

import floor.file.storage.annotation.FloorFileStorage;
import floor.repository.RepositoryDatabase;
import gray.light.blog.annotation.DomainBlog;
import gray.light.owner.annotation.DomainOwner;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

/**
 * 统一配置所有模块依赖
 *
 * @author XyParaCrim
 */
@Configuration
public class ModuleDependenciesConfiguration {

    @Configuration
    //@FloorRepository
    //@FloorFileStorage
    public static class FloorComponentConfiguration {

    }

    @DomainOwner
    @DomainBlog
    @AutoConfigureAfter(FloorComponentConfiguration.class)
    @Configuration
    @RequiredArgsConstructor
    public static class GrayLightDomainConfiguration {

    }

}
