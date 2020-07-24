package floor.file.storage.retry;

import floor.file.storage.AbstractFileStorage;
import floor.file.storage.config.FloorFileStorageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.csource.fastdfs.*;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * 当无法连接时，会稍后重试，直至成功
 *
 * @author XyParaCrim
 */
@Slf4j
@RequiredArgsConstructor
public class RetryFileStorage extends AbstractFileStorage {

    private static final AtomicReferenceFieldUpdater<RetryFileStorage, Status> STATUS_UPDATER =
            AtomicReferenceFieldUpdater.newUpdater(RetryFileStorage.class, Status.class, "connectedStatus");

    /**
     * 表示连接状态
     */
    private enum Status {
        /**
         * 未连接或者连接不成功
         */
        UNCONNECTED,
        /**
         * 已连接成功
         */
        CONNECTED,
        /**
         * 正在连接
         */
        CONNECTING,
        /**
         * 需要重新连接
         */
        RECONNECT
    }

    private final FloorFileStorageProperties properties;

    private StorageClient1 client = null;

    private volatile Status connectedStatus = Status.UNCONNECTED;

    private volatile ScheduledExecutorService scheduledExecutorService = null;

    @Override
    protected StorageClient1 getStorageClient() {
        for (; ; ) {
            if (connectedStatus == Status.CONNECTED || connectedStatus == Status.RECONNECT) {
                return client;
            }

            if (connectedStatus == Status.UNCONNECTED &&
                    STATUS_UPDATER.compareAndSet(this, Status.UNCONNECTED, Status.CONNECTING)) {

                doConnect();

                if (connectedStatus == Status.CONNECTED) {
                    return client;
                }

                connectedStatus = Status.RECONNECT;
                retryConnect();
            }

        }
    }

    private void doConnect() {
        try {
            ClientGlobal.initByProperties(properties.getConfigLocation());

            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getTrackerServer();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);

            client = new StorageClient1(trackerServer, storageServer);
            connectedStatus = Status.CONNECTED;

            log.info("Success to connect fastdfs: {}", properties.getConfigLocation());
        } catch (Exception e) {
            log.error("Failed to initialize client of fastdfs", e);
        }
    }

    private void retryConnect() {

        log.warn("Set up scheduled executor to connect to fastdfs");

        class ConnectFactory implements ThreadFactory {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "connect-fastdfs");
            }
        }

        scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new ConnectFactory());
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            if (connectedStatus == Status.RECONNECT) {
                log.warn("try to reconnect to fastdfs");
                doConnect();
            }

            if (connectedStatus != Status.RECONNECT) {
                scheduledExecutorService.shutdownNow();
            }
        }, 1L, 1L, TimeUnit.MINUTES);

    }

}
