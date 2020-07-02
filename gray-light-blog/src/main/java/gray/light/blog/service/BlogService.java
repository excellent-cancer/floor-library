package gray.light.blog.service;

import gray.light.blog.business.BlogBo;
import gray.light.blog.entity.Blog;
import gray.light.blog.entity.Tag;
import gray.light.blog.entity.TagWithBlogId;
import gray.light.blog.repository.BlogRepository;
import gray.light.blog.repository.TagRepository;
import gray.light.support.web.PageChunk;
import gray.light.support.web.PageSupport;
import lombok.RequiredArgsConstructor;
import perishing.constraint.jdbc.Page;
import perishing.constraint.treasure.chest.CollectionsTreasureChest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 提供Blog这个领域的功能
 *
 * @author XyParaCrim
 */
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    private final TagRepository tagRepository;

    private final BlogSourceService blogSourceService;

    /**
     * 查找某个所有者的所有博客
     *
     * @param ownerId 所有者Id
     * @param page    分页
     * @return 查找某个所有者的所有博客
     */
    public PageChunk<Blog> findBlogs(Long ownerId, Page page) {
        return PageSupport.page(blogRepository.findByOwnerId(ownerId, page.nullable()), page);
    }

    /**
     * 查找某个所有者的使用指定标签的博客
     *
     * @param ownerId 所属者Id
     * @param tags    标签
     * @param page    分页
     * @return 查找某个所有者的使用指定标签的博客
     */
    public PageChunk<Blog> findBlogsByTags(Long ownerId, List<Tag> tags, Page page) {
        return PageSupport.page(blogRepository.findByTagsAndOwnerId(ownerId, tags, page.nullable()), page);
    }

    /**
     * 获取具体的博客
     *
     * @param id 博客id
     * @return 获取具体的博客
     */
    public Optional<Blog> findBlog(Long id) {
        return blogRepository.find(id);
    }

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
     * 查找某个所有者的所有博客及其使用到的标签
     *
     * @param ownerId 所有者Id
     * @param page    分页
     * @return 查找某个所有者的所有博客及其使用到的标签
     */
    public PageChunk<BlogBo> findBlogsPro(Long ownerId, Page page) {
        PageChunk<Blog> pageChunk = findBlogs(ownerId, page);
        List<TagWithBlogId> tags = tagRepository.findSpTagByOwnerId(ownerId);


        Map<Long, BlogBo> blogMap = pageChunk.getItems().stream().collect(Collectors.toMap(Blog::getId, e -> new BlogBo(e, new ArrayList<>())));

        for (TagWithBlogId tag : tags) {
            if (blogMap.containsKey(tag.getBlogId())) {
                blogMap.get(tag.getBlogId()).getTags().add(tag);
            }
        }

        return new PageChunk<>(pageChunk.getPages(), pageChunk.getCount(), pageChunk.getTotal(), new ArrayList<>(blogMap.values()));
    }

}
