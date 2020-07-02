package gray.light.document.config;

import gray.light.book.config.BookAutoConfiguration;
import gray.light.document.handler.DocumentBookHandler;
import gray.light.document.handler.DocumentHandler;
import gray.light.document.router.PersonalDocumentBookRouter;
import gray.light.document.router.PersonalDocumentRouter;
import gray.light.document.service.DocumentRelationService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author XyParaCrim
 */
@Configuration
@ConditionalOnProperty(value = "gray.light.document.enabled", matchIfMissing = true)
@MapperScan("gray.light.document.repository")
@Import({DocumentRelationService.class, PersonalDocumentRouter.class, DocumentHandler.class})
public class DocumentAutoConfiguration {


    @ConditionalOnBean(BookAutoConfiguration.class)
    @Configuration
    @Import({PersonalDocumentBookRouter.class, DocumentBookHandler.class})
    public static class OptionalBookConfiguration {

    }

}
