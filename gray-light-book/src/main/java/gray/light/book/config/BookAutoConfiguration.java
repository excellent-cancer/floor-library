package gray.light.book.config;

import floor.file.storage.FileStorage;
import floor.file.storage.config.FloorFileStorageAutoConfiguration;
import floor.repository.RepositoryDatabase;
import gray.light.book.handler.BookHandler;
import gray.light.book.service.BookRepositoryCacheService;
import gray.light.book.service.BookService;
import gray.light.book.service.BookSourceService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ConditionalOnProperty(value = "gray.light.book.enabled", matchIfMissing = true)
@Configuration
@MapperScan("gray.light.book.repository")
@Import({BookHandler.class, BookService.class})
public class BookAutoConfiguration {

    @Configuration
    @ConditionalOnBean(FileStorage.class)
    @Import(BookSourceService.class)
    public static class DocumentSourceConfiguration {

    }

    @Configuration
    @ConditionalOnBean(RepositoryDatabase.class)
    @Import(BookRepositoryCacheService.class)
    public static class DocumentRepositoryCacheConfiguration {
    }


}
