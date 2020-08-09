package floor.coordination;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

import javax.annotation.PostConstruct;
import java.io.Closeable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
public class FloorConfigWatch implements TreeCacheListener, Closeable {

    private final AtomicBoolean running = new AtomicBoolean(false);

    private final String context;

    private final CuratorFramework client;

    private final Predicate<TreeCacheEvent> predicate;

    private final Consumer<TreeCacheEvent> consumer;

    private TreeCache cache;

    @PostConstruct
    public FloorConfigWatch start() {
        if (running.compareAndSet(false, true)) {
            String normalizeContext = normalizeContext();
            try {
                cache = TreeCache.newBuilder(client, normalizeContext).build();
                cache.getListenable().addListener(this);
                cache.start();
            }  catch (Exception e) {
                log.error("Failed to watch zookeeper{{}} for context: {}", connectString(), normalizeContext);
            }
        }
        return this;
    }

    @Override
    public void childEvent(CuratorFramework client, TreeCacheEvent event) {
        if (predicate.test(event)) {
            consumer.accept(event);
        }
    }

    @Override
    public void close() {
        if (this.running.compareAndSet(true, false)) {
            cache.close();
            this.cache = null;
        }
    }

    private String normalizeContext() {
        return context.startsWith("/") ? context : "/" + context;
    }

    private String connectString() {
        return client.getZookeeperClient().getCurrentConnectionString();
    }

}
