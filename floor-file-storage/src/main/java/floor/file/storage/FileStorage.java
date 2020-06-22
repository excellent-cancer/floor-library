package floor.file.storage;

import java.nio.file.Path;

public interface FileStorage extends AutoCloseable {

    String upload(Path path, String suffix) throws UploadFileException;

}
