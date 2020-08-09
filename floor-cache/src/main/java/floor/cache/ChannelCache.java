package floor.cache;

import java.util.List;
import java.util.function.Consumer;

/**
 * 缓存搜索频道
 *
 * @author XyParaCrim
 */
public interface ChannelCache<K, T> {

    /**
     * 缓存指定key下的所有搜索频道，完成缓存后，会将已缓存的和淘汰频道Id
     * 传入callback方法中
     *
     * @param key 频道集的key
     * @param channelId 频道Id
     * @param action 将已缓存的和淘汰频道Id传入callback方法中
     */
    void cache(K key, T channelId, Consumer<List<T>> action);


    /**
     * 移除指定key下的所有搜索频道
     *
     * @param key 频道集的key
     * @param channelId 频道Id
     */
    void remove(K key, T channelId);

}