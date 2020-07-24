package gray.light.owner;

import gray.light.owner.handler.OwnerHandler;
import gray.light.owner.handler.OwnerProjectHandler;
import gray.light.owner.handler.WorksHandler;
import gray.light.owner.router.PersonalPageRouter;
import gray.light.owner.service.OverallOwnerService;
import gray.light.owner.service.ProjectDetailsService;
import gray.light.owner.service.SuperOwnerService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * owner领域自动配置
 *
 * @author XyParaCrim
 */
@Import({PersonalPageRouter.class,
        WorksHandler.class,
        OwnerHandler.class,
        OwnerProjectHandler.class,
//        SuperOwnerService.class,
        ProjectDetailsService.class,
        OverallOwnerService.class})
@MapperScan(OwnerAutoConfiguration.MAPPER_PACKAGE)
public class OwnerAutoConfiguration {

    public static final String MAPPER_PACKAGE = "gray.light.owner.repository";

}
