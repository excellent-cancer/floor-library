package gray.light.owner.annotation;

import gray.light.owner.OwnerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自动配置owner领域
 *
 * @author XyParaCrim
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(OwnerAutoConfiguration.class)
public @interface OwnerDomain {
}
