package gray.light.support.component;

import lombok.NonNull;

import java.util.concurrent.ConcurrentHashMap;

public class ReppsitoryServiceTable {

    private final ConcurrentHashMap<Class<?>, Object> table = new ConcurrentHashMap<>();

    public <T> boolean registerRepositoryService(@NonNull Class<T> serviceType, T service) {
        return table.putIfAbsent(serviceType, service) == null;
    }

    @SuppressWarnings("unchecked")
    public <T> T service(@NonNull Class<T> serviceType) {
        return (T) table.get(serviceType);
    }

}
