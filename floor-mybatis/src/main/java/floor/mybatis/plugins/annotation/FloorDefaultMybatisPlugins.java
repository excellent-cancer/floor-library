package floor.mybatis.plugins.annotation;

import floor.mybatis.plugins.ExecutionMonitorPlugin;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 引入所有自己编写的mybatis插件
 *
 * @author XyParaCrim
 */
@Import({ExecutionMonitorPlugin.class})
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FloorDefaultMybatisPlugins {
}
