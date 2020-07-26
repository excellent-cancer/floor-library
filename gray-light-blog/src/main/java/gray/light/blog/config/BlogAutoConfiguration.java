package gray.light.blog.config;

import floor.file.storage.FileStorage;
import gray.light.blog.handler.BlogQueryHandler;
import gray.light.blog.handler.BlogSearchHandler;
import gray.light.blog.handler.BlogUpdateHandler;
import gray.light.blog.handler.TagHandler;
import gray.light.blog.router.ManualRouter;
import gray.light.blog.router.PersonalBlogRouter;
import gray.light.blog.router.PersonalSearchBlogRouter;
import gray.light.blog.search.BlogSearchOptions;
import gray.light.blog.service.*;
import gray.light.search.cache.SearchScrollCache;
import org.elasticsearch.search.SearchService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
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
        BlogAutoConfiguration.WritableConfiguration.class,
        BlogAutoConfiguration.SearchConfiguration.class,
        ManualRouter.class
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
    public static class WritableConfiguration {

    }

    @Import({
            SearchScrollCache.class,
            BlogSearchHandler.class,
            SearchBlogService.class,
            BlogSearchOptions.class,
            PersonalSearchBlogRouter.class
    })
    public static class SearchConfiguration {

    }

}
