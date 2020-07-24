package gray.light.blog.service;

import floor.file.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * TODO：修改正确文件存储服务
 *
 * @author XyParaCrim
 */
@RequiredArgsConstructor
public class BlogSourceService {

    private static final String BLOG_EXTENSION = "md";

    private final FileStorage fileStorage;

    public String updateBlog(byte[] bytes) {
        return fileStorage.upload(bytes, BLOG_EXTENSION);
    }

    public String download(String link) {
        return fileStorage.download(link);
    }
}
