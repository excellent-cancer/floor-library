package excellent.cancer.gray.light.utils;

import lombok.Getter;
import lombok.NonNull;
import org.csource.common.MyException;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author XyParaCrim
 */
@Getter
public class FastdfsClient implements AutoCloseable {

    private final TrackerClient trackerClient;

    private final TrackerServer trackerServer;

    private final StorageServer storageServer;

    private final StorageClient1 storageClient;

    public FastdfsClient(@NonNull TrackerClient trackerClient) throws IOException, MyException {
        this.trackerClient = trackerClient;
        this.trackerServer = trackerClient.getTrackerServer();
        this.storageServer = trackerClient.getStoreStorage(trackerServer);
        this.storageClient = new StorageClient1(trackerServer, storageServer);
    }

    public String uploadMarkdown(File file) throws IOException, MyException {
        return storageClient.upload_file1(file.getAbsolutePath(), "md", null);
    }

    public String uploadMarkdown(Path path) throws IOException, MyException {
        return storageClient.upload_file1(path.toString(), "md", null);
    }

    @Override
    public void close() throws IOException {
        storageClient.close();
    }
}
