package gray.light.document.annotation;

import gray.light.document.config.DocumentAutoConfiguration;
import gray.light.document.config.DocumentJobConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自动配置文档
 *
 * @author XyParaCrim
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({DocumentAutoConfiguration.class, DocumentJobConfiguration.class})
public @interface DocumentDomain {
}
