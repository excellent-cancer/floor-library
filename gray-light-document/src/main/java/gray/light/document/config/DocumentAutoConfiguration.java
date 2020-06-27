package gray.light.document.config;

import gray.light.book.annotation.BookSupport;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author XyParaCrim
 */
@BookSupport
@Configuration
@MapperScan("gray.light.document.repository")
@ComponentScan({"gray.light.document.handler", "gray.light.document.router"})
public class DocumentAutoConfiguration {



}
