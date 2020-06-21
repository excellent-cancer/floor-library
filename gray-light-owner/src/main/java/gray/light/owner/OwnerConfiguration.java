package gray.light.owner;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("gray.light.owner.repository")
public class OwnerConfiguration {
}
