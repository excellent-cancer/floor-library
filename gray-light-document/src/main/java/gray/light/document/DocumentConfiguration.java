package gray.light.document;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("gray.light.document.repository")
public class DocumentConfiguration {
}
