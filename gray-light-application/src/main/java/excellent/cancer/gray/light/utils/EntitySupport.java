package excellent.cancer.gray.light.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author XyParaCrim
 */
public final class EntitySupport {

    public static <T, V> Map<T, V> entityListToMap(List<V> entities, Function<V, T> extract) {
        Map<T, V> table = new HashMap<>(entities.size());
        for (V e : entities) {
            table.put(extract.apply(e), e);
        }
        return table;
    }

    public static <T, V> Map<T, List<V>> entityListToMapList(List<V> entities, Function<V, T> extract) {
        Map<T, List<V>> table = new HashMap<>(entities.size());
        for (V e : entities) {
            table.computeIfAbsent(extract.apply(e), k -> new LinkedList<>()).add(e);
        }
        return table;
    }

}
