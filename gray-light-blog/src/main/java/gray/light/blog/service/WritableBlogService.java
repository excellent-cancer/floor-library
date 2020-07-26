package gray.light.blog.service;

import gray.light.blog.entity.Blog;
import gray.light.blog.entity.Tag;
import gray.light.blog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 可以修改博客的服务
 *
 * @author XyParaCrim
 */
@Slf4j
@RequiredArgsConstructor
public class WritableBlogService {

    private final BlogRepository blogRepository;

    private final BlogSourceService blogSourceService;

    /**
     * 创建一个新博客，并且存储博客文件
     *
     * @param blog    博客
     * @param content 博客内容
     * @return 是否添加成功
     */
    public boolean addBlog(Blog blog, byte[] content) {
        String downloadLink = blogSourceService.updateBlog(content);
        blog.setDownloadLink(downloadLink);
        return blogRepository.save(blog);
    }

    /**
     * 为一篇博客增加标签
     *
     * @param blog 博客
     * @param tags 标签
     * @return 是否添加成功
     */
    public boolean addTags(Blog blog, List<Tag> tags) {
        return blogRepository.saveTags(blog.getOwnerId(), blog.getId(), tags);
    }

}
