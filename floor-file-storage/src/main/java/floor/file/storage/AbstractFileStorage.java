package floor.file.storage;

import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.csource.fastdfs.StorageClient1;

import java.io.IOException;
import java.nio.file.Path;

/**
 * 实现基本的与文件交互操作
 *
 * @author XyParaCrim
 */
@Slf4j
public abstract class AbstractFileStorage implements FileStorage {

    /**
     * 返回文件服务器交互客户端
     *
     * @return StorageClient1 文件服务器交互客户端
     */
    protected abstract StorageClient1 getStorageClient();

    @Override
    public String upload(Path path, String suffix) throws UploadFileException {
        try {
            return getStorageClient().upload_file1(path.toString(), suffix, null);
        } catch (MyException myException) {
            throw new UploadFileException("Failed to upload file", myException);
        } catch (IOException ioException) {
            throw new UploadFileException("Failed to access upload file", ioException);
        }
    }

    @Override
    public String upload(byte[] bytes, String suffix) throws UploadFileException {
        try {
            return getStorageClient().upload_file1(bytes, suffix, null);
        } catch (MyException myException) {
            throw new UploadFileException("Failed to upload file", myException);
        } catch (IOException ioException) {
            throw new UploadFileException("Failed to access upload file", ioException);
        }
    }

    @Override
    public void delete(String link) {
        try {
            int i = getStorageClient().delete_file1(link);
            if (i != 0) {
                log.error("删除文件失败: {}", link);
            }
        } catch (IOException | MyException e) {
            throw new DeleteFileException(e);
        }
    }

    @Override
    public String download(String link) {
        try {
            return new String(getStorageClient().download_file1(link));
        } catch (IOException e) {
            throw new DownloadFileException("Failed to download file", e);
        } catch (MyException e) {
            throw new DownloadFileException("Failed to access download file", e);
        }
    }

    @Override
    public void close() throws Exception {
        getStorageClient().close();
    }

}
