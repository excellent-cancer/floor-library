package gray.light.source.service;

import floor.file.storage.FileStorage;
import gray.light.source.repository.ImagesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;

@Slf4j
@RequiredArgsConstructor
public class SourceService {

    private final FileStorage fileStorage;

    private final ImagesRepository imagesRepository;

    public String uploadFile(String path, String suffix) {
        String link = fileStorage.upload(Path.of(path), suffix);
        imagesRepository.save(link, suffix);

        return link;
    }

    public void deleteFile(String link) {
        fileStorage.delete(link);
        imagesRepository.delete(link);
    }

}
