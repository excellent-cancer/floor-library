package gray.light.document.service;

import floor.file.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

/**
 * 定义有关于文档文件的上传、下载和更改
 *
 * @author XyParaCrim
 */
@Service
@CommonsLog
@RequiredArgsConstructor
public class DocumentSourceService {

    private static final String CHAPTER_EXTENSION = "md";

    private final FileStorage fileStorage;

    /**
     * 更新指定章节文件
     *
     * @param path 更新指定章节路径
     * @return 更新ID
     */
    public String updateChapter(Path path) {
        return fileStorage.upload(path, CHAPTER_EXTENSION);
    }

    // TODO 更加visitor更新整个仓库文件，及其旧文件删除

}
