package gray.light.definition.web;

import gray.light.definition.entity.Scope;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;

/**
 * 自定义请求验证器
 *
 * @author XyParaCrim
 */
public final class CustomizedRequestPredicates {

    public static RequestPredicate worksScope() {
        return RequestPredicates.queryParam("scope", Scope.WORKS.getName());
    }

}
