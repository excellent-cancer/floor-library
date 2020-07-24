package gray.light.blog.config;

import floor.file.storage.FileStorage;
import gray.light.blog.handler.BlogQueryHandler;
import gray.light.blog.handler.BlogUpdateHandler;
import gray.light.blog.handler.TagHandler;
import gray.light.blog.router.PersonalBlogRouter;
import gray.light.blog.service.ReadableBlogService;
import gray.light.blog.service.BlogSourceService;
import gray.light.blog.service.TagService;
import gray.light.blog.service.WritableBlogService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Import;

/**
 * 博客相关的自动配置
 *
 * @author XyParaCrim
 */
@Import({
        PersonalBlogRouter.class,
        BlogQueryHandler.class,
        TagHandler.class,
        ReadableBlogService.class,
        TagService.class,
        BlogAutoConfiguration.WritableConfiguation.class
})
@MapperScan(BlogAutoConfiguration.MAPPER_PACKAGE)
public class BlogAutoConfiguration {

    public static final String MAPPER_PACKAGE = "gray.light.blog.repository";


    @Import({
            BlogUpdateHandler.class,
            WritableBlogService.class,
            BlogSourceService.class
    })
    @ConditionalOnBean(FileStorage.class)
    public static class WritableConfiguation {

    }

}
