package gray.light.book;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"gray.light.book.handler", "gray.light.book.service"})
@MapperScan("gray.light.book.repository")
public class BookAutoConfiguration {
}
