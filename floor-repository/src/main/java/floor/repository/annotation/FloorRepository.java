package floor.repository.annotation;

import floor.repository.config.FloorRepositoryAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 引入floor-repository配置
 *
 * @author XyParaCrim
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(FloorRepositoryAutoConfiguration.class)
public @interface FloorRepository {
}
