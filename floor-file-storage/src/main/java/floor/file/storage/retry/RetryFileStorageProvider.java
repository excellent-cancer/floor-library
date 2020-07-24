package floor.file.storage.retry;

import floor.file.storage.FileStorage;
import floor.file.storage.FileStorageProvider;
import floor.file.storage.FileStorageProviderException;
import floor.file.storage.config.FloorFileStorageProperties;

public class RetryFileStorageProvider implements FileStorageProvider {

    @Override
    public FileStorage fileStorage(FloorFileStorageProperties properties) throws FileStorageProviderException {
        return null;
    }
    
}
