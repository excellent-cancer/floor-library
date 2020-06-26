package gray.light.owner.router;

import gray.light.owner.handler.OwnerHandler;
import gray.light.owner.handler.WorksHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

/**
 * 个人主页请求路由
 *
 * @author XyParaCrim
 */
@Configuration
public class PersonalPageRouter {

    /**
     * 获取所属者详细信息
     *
     * @param handler 处理
     * @return 路由方法
     */
    @Bean
    public RouterFunction<ServerResponse> getOwnerDetails(OwnerHandler handler) {
        RequestPredicate predicate = RequestPredicates.GET("/owner");

        return RouterFunctions.route(predicate, handler::ownerDetails);
    }

    /**
     * 获取指定所属者的所有所属者项目
     *
     * @param handler 处理
     * @return 路由方法
     */
    @Bean
    public RouterFunction<ServerResponse> queryOwnerProject(OwnerHandler handler) {
        RequestPredicate predicate = RequestPredicates.GET("/owner/owner-project");

        return RouterFunctions.route(predicate, handler::queryOwnerProject);
    }

    /**
     * 查询指定所属者的works项目
     *
     * @param handler works处理
     * @return 路由方法
     */
    @Bean
    public RouterFunction<ServerResponse> queryWorks(WorksHandler handler) {
        RequestPredicate predicate = RequestPredicates.GET("/owner/works");

        return RouterFunctions.route(predicate, handler::queryWorks);
    }

    /**
     * 为指定所属者添加一个works
     *
     * @param handler 关于owner-project的请求与回复转换操作
     * @return 路由方法
     */
    @Bean
    public RouterFunction<ServerResponse> newWorks(WorksHandler handler) {
        RequestPredicate predicate = RequestPredicates.POST("/owner/works");

        return RouterFunctions.route(predicate, handler::addWorksToOwnerProject);
    }

}
