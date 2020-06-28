package gray.light.blog.config;

import floor.file.storage.FileStorage;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(FileStorage.class)
@MapperScan("gray.light.blog.repository")
@ComponentScan({"gray.light.blog.handler", "gray.light.blog.router", "gray.light.blog.service"})
public class BlogAutoConfiguration {
}
