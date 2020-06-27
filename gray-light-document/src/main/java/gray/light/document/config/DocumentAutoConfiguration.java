package gray.light.document.config;

import floor.file.storage.FileStorage;
import floor.repository.RepositoryDatabase;
import gray.light.book.annotation.BookSupport;
import gray.light.document.service.DocumentRepositoryCacheService;
import gray.light.document.service.DocumentSourceService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author XyParaCrim
 */
@BookSupport
@Configuration
@MapperScan("gray.light.document.repository")
@ComponentScan({"gray.light.document.handler", "gray.light.document.router"})
public class DocumentAutoConfiguration {

    @Configuration
    @ConditionalOnBean(FileStorage.class)
    @Import(DocumentSourceService.class)
    public static class DocumentSourceConfiguration {
    }

    @Configuration
    @ConditionalOnBean(RepositoryDatabase.class)
    @Import(DocumentRepositoryCacheService.class)
    public static class DocumentRepositoryCacheConfiguration {
    }

}
