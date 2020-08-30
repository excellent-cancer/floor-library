package floor.domain;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 处理领域注解注册信息
 *
 * @param <T> 注解类型
 */
@Slf4j
public abstract class DomainComponentRegistrar<T extends Annotation> implements ImportBeanDefinitionRegistrar {

    private final AtomicBoolean configured = new AtomicBoolean();

    /**
     * 返回领域组件注解类型
     *
     * @return 返回领域组件注解类型
     */
    protected abstract Class<T> annotationType();

    /**
     * 处理领域组件注解的信息
     *
     * @param domainAnnotation 领域组件注解
     * @param registry bean注册器
     */
    protected abstract void process(T domainAnnotation, BeanDefinitionRegistry registry);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        if (configured.get() || !configured.compareAndSet(false, true)) {
            log.warn("Error DomainOwner repeat registration: {}", metadata.getClassName());
        }

        AnnotationAttributes attrs = AnnotatedElementUtils.getMergedAnnotationAttributes(
                ClassUtils.resolveClassName(metadata.getClassName(), null),
                annotationType());

        process(domainOwnerProperties(attrs, metadata.getClassName()), registry);
    }

    private T domainOwnerProperties(AnnotationAttributes attrs, String className) {
        return AnnotationUtils.synthesizeAnnotation(attrs,
                annotationType(), ClassUtils.resolveClassName(className, null));
    }

    protected static void registerComponent(Class<?> type, BeanDefinitionRegistry registry) {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(type);
        registry.registerBeanDefinition(type.getName(), rootBeanDefinition);
    }

}
