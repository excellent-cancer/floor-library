package gray.light.blog.config;

import floor.file.storage.FileStorage;
import gray.light.blog.handler.BlogHandler;
import gray.light.blog.handler.TagHandler;
import gray.light.blog.router.PersonalBlogRouter;
import gray.light.blog.service.BlogService;
import gray.light.blog.service.BlogSourceService;
import gray.light.blog.service.TagService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnBean(FileStorage.class)
@MapperScan("gray.light.blog.repository")
@ConditionalOnProperty(value = "gray.light.blog.enabled", matchIfMissing = true)
@Import({PersonalBlogRouter.class, BlogHandler.class, TagHandler.class, BlogService.class, BlogSourceService.class, TagService.class })
public class BlogAutoConfiguration {
}
