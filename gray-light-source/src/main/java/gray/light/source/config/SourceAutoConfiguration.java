package gray.light.source.config;

import floor.file.storage.FileStorage;
import gray.light.source.handler.SourceHandler;
import gray.light.source.router.SourceRouter;
import gray.light.source.service.SourceService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author XyParaCrim
 */
@MapperScan("gray.light.source.repository")
@Configuration
@ConditionalOnClass(FileStorage.class)
@ConditionalOnProperty(value = "gray.light.source.enabled", matchIfMissing = true)
@Import({SourceRouter.class, SourceHandler.class, SourceService.class})
public class SourceAutoConfiguration {

}
