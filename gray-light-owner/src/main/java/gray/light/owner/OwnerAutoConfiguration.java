package gray.light.owner;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * owner领域自动配置
 *
 * @author XyParaCrim
 */
@Configuration
@MapperScan("gray.light.owner.repository")
@ComponentScan({
        "gray.light.owner.handler",
        "gray.light.owner.service",
        "gray.light.owner.router"
})
public class OwnerAutoConfiguration {
}
