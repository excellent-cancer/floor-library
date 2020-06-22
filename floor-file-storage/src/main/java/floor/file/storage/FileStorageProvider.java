package floor.file.storage;

import floor.file.storage.config.FloorFileStorageProperties;

public interface FileStorageProvider {

    FileStorage fileStorage(FloorFileStorageProperties properties) throws FileStorageProviderException;

    static String storageType() {
        throw new UnsupportedOperationException();
    }

}
