package floor.domain;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.lang.annotation.Annotation;

/**
 *
 * @param <T>
 */
public abstract class DomainComponentRegistrarWithPermission<T extends Annotation> extends DomainComponentRegistrar<T> {

    @Override
    protected void process(T domainAnnotation, BeanDefinitionRegistry registry) {
        EnableDomainPermission permission = enableDomainPermission(domainAnnotation);

        // 如果DomainOwner#value为true，说明支持所属者领域
        if (permission.value()) {
            registerQueryComponents(registry);

            // 根据配置，选择加载只读、可写和搜索组件

            if (!permission.onlyRead()) {
                registerUpdateComponents(registry);
            }

            if (permission.searchService()) {
                registerSearchComponents(registry);
            }

        }

    }

    /**
     * 注册只读权限的组件
     *
     * @param registry bean注册器
     */
    protected abstract void registerQueryComponents(BeanDefinitionRegistry registry);

    /**
     * 注册可写权限的组件
     *
     * @param registry bean注册器
     */
    protected abstract void registerUpdateComponents(BeanDefinitionRegistry registry);

    /**
     * 注册搜索组件
     *
     * @param registry bean注册器
     */
    protected abstract void registerSearchComponents(BeanDefinitionRegistry registry);

    /**
     * 获取权限注解
     *
     * @param domainAnnotation 领域注解
     * @return 获取权限注解
     */
    protected abstract EnableDomainPermission enableDomainPermission(T domainAnnotation);
}
