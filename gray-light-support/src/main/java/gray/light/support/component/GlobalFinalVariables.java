package gray.light.support.component;

import lombok.NonNull;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 添加全局不变变量，减少Spring boot中的自动注入
 *
 * @author XyParaCrim
 */
public final class GlobalFinalVariables {

    private final static ConcurrentHashMap<Class<?>, Object> variables = new ConcurrentHashMap<>();

    public static <T> void set(@NonNull Class<T> type, T variable) {
        @SuppressWarnings("unchecked")
        T previous = (T) variables.putIfAbsent(type, variable);
        if (previous != null) {
            throw new IllegalArgumentException("Cannot assign a value to final variable type '" + type.getName() + "'");
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(@NonNull Class<T> type) {
        return (T) Objects.requireNonNull(variables.get(type));
    }

}
