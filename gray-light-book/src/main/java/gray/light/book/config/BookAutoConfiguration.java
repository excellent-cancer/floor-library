package gray.light.book.config;

import floor.file.storage.FileStorage;
import floor.repository.RepositoryDatabase;
import gray.light.book.service.BookRepositoryCacheService;
import gray.light.book.service.BookSourceService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({"gray.light.book.handler", "gray.light.book.service"})
@MapperScan("gray.light.book.repository")
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
