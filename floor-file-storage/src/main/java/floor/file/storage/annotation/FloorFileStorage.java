package floor.file.storage.annotation;

import floor.file.storage.config.FloorFileStorageAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 引入File-Storage配置
 *
 * @author XyParaCrim
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(FloorFileStorageAutoConfiguration.class)
public @interface FloorFileStorage {
}
