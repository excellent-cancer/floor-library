package floor.file.storage.simple;

import floor.file.storage.AbstractFileStorage;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import java.io.IOException;

/**
 * 立即连接文件服务器，无法连接时报错
 *
 * @author XyParaCrim
 */
@Slf4j
public class SimpleFileStorage extends AbstractFileStorage {

    private final TrackerClient trackerClient;

    private final TrackerServer trackerServer;

    private final StorageServer storageServer;

    private final StorageClient1 storageClient;

    public SimpleFileStorage(@NonNull TrackerClient trackerClient) throws IOException, MyException {
        this.trackerClient = trackerClient;
        this.trackerServer = trackerClient.getTrackerServer();
        this.storageServer = trackerClient.getStoreStorage(trackerServer);
        this.storageClient = new StorageClient1(trackerServer, storageServer);
    }

    @Override
    protected StorageClient1 getStorageClient() {
        return storageClient;
    }
}