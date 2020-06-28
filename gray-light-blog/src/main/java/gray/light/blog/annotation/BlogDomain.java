package gray.light.blog.annotation;

import gray.light.blog.config.BlogAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 关于blog领域的自动配置
 *
 * @author XyParaCrim
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(BlogAutoConfiguration.class)
public @interface BlogDomain {
}
