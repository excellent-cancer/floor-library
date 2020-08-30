package floor.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注册领域的权限
 *
 * @author XyParaCrim
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface EnableDomainPermission {

    /**
     * 是否装载Blog所有服务
     *
     * @return 是否装载Blog所有服务
     */
    boolean value() default true;

    /**
     * 是否只提供可读服务
     *
     * @return 是否只提供可读服务
     */
    boolean onlyRead() default false;

    /**
     * 是否提供搜索服务
     *
     * @return 是否提供搜索服务
     */
    boolean searchService() default true;

}
