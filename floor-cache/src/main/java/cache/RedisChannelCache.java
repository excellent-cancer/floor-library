package cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 针对单个session可支持缓存的搜索scrollId
 *
 * @author XyParaCrim
 */
@RequiredArgsConstructor
public class RedisChannelCache implements StringChannelCache {

    private static final int SCROLL_ID_COUNT = 5;

    private static final long TIMEOUT = 5 * 1000 * 60L;

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void cache(String key, String channelId, Consumer<List<String>> action) {
        String redisKey = key(key);
        SetOperations<String, String> operations = redisTemplate.opsForSet();
        Boolean hasKey = operations.getOperations().hasKey(key);

        if (hasKey != null && hasKey) {
            Boolean isMember = operations.isMember(redisKey, channelId);
            if (isMember != null && isMember) {
                Long size = operations.size(key);

                if (size != null && size > SCROLL_ID_COUNT) {
                    action.accept(operations.pop(key, size - SCROLL_ID_COUNT));
                }
            }
        }

        operations.add(key, channelId);
        operations.getOperations().expire(key, TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public void remove(String key, String channelId) {
        SetOperations<String, String> operations = redisTemplate.opsForSet();
        operations.remove(key(key), channelId);
    }

    private static String key(String id) {
        return "gray-light:scroll:" + id;
    }


}
