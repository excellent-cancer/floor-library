package gray.light.search.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class SearchScrollCache {

    private static final int SCROLL_ID_COUNT = 5;

    private static final long TIMEOUT = 5 * 1000 * 60L;

    private final SetOperations<String, String> operations;

    public SearchScrollCache(RedisTemplate<String, String> template) {
        this.operations = template.opsForSet();
    }

    public void cacheScrollId(String id, String scrollId, Consumer<List<String>> action) {
        String key = key(id);
        Boolean hasKey =  operations.getOperations().hasKey(key);

        if (hasKey != null && hasKey) {
            Boolean isMember = operations.isMember(id, scrollId);
            if (isMember != null && isMember) {
                Long size = operations.size(key);

                if (size != null && size > SCROLL_ID_COUNT) {
                    action.accept(operations.pop(key, size - SCROLL_ID_COUNT));
                }
            }
        }

        operations.add(key, scrollId);
        operations.getOperations().expire(key, TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public void clearScrollId(String id, String scrollId) {
        operations.remove(key(id), scrollId);
    }

    private static String key(String id) {
        return "gray-light:scroll:" + id;
    }

}
