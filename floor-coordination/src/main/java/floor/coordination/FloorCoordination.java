package floor.coordination;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

import java.util.function.Consumer;

public final class FloorCoordination {

    public static FloorConfigWatch matchNodeAdd(String context, String match, CuratorFramework client, Consumer<TreeCacheEvent> callback) {
        return new FloorConfigWatch(
                context,
                client,
                treeCacheEvent -> treeCacheEvent.getType() == TreeCacheEvent.Type.NODE_ADDED && treeCacheEvent.getData().getPath().equals(match),
                callback
        );
    }

}
