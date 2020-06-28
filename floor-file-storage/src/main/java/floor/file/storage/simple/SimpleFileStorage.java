package floor.file.storage.simple;

import floor.file.storage.FileStorage;
import floor.file.storage.UploadFileException;
import lombok.NonNull;
import org.csource.common.MyException;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import java.io.IOException;
import java.nio.file.Path;

public class SimpleFileStorage implements FileStorage {

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
    public String upload(Path path, String suffix) throws UploadFileException {
        try {
            return storageClient.upload_file1(path.toString(), suffix, null);
        } catch (MyException myException) {
            throw new UploadFileException("Failed to upload file", myException);
        } catch (IOException ioException) {
            throw new UploadFileException("Failed to access upload file", ioException);
        }
    }

    @Override
    public String upload(byte[] bytes, String suffix) throws UploadFileException {
        try {
            return storageClient.upload_file1(bytes, suffix, null);
        } catch (MyException myException) {
            throw new UploadFileException("Failed to upload file", myException);
        } catch (IOException ioException) {
            throw new UploadFileException("Failed to access upload file", ioException);
        }
    }

    @Override
    public void close() throws Exception {
        storageClient.close();
    }
}