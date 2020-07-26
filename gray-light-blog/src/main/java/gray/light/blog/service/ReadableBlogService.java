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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequiredArgsConstructor
public class ReadableBlogService {

    private final BlogRepository blogRepository;

    private final TagRepository tagRepository;

    private final TagService tagService;

    /**
     * 查找某个所有者的所有博客
     *
     * @param ownerId 所有者Id
     * @param page    分页
     * @return 查找某个所有者的所有博客
     */
    public PageChunk<Blog> findBlogs(Long ownerId, Page page) {
        if (page.isUnlimited() || page.getPage() * page.getPer() < 5000) {
            return PageSupport.page(blogRepository.findByOwnerId(ownerId, page.nullable()), page);
        } else {
            return PageSupport.page(blogRepository.largeFindByOwnerId(ownerId, page), page);
        }

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
     * 查找某个所有者的所有博客及其使用到的标签
     *
     * @param ownerId 所有者Id
     * @param page    分页
     * @return 查找某个所有者的所有博客及其使用到的标签
     */
    public PageChunk<BlogBo> findBlogsIncludeTags(Long ownerId, Page page) {
        PageChunk<Blog> pageChunk = findBlogs(ownerId, page);
        List<TagWithBlogId> tags = tagRepository.findByBlogIds(pageChunk.getItems().stream().map(Blog::getId).collect(Collectors.toList()));
        List<BlogBo> blogBos = BlogUtils.assemblyBlogAndTag(pageChunk.getItems(), tags);

        return new PageChunk<>(pageChunk.getPages(), pageChunk.getCount(), pageChunk.getTotal(), blogBos);
    }

    /**
     * 根据博客Id列表，返回所有符合条件的博客及其标签
     *
     * @param ids 博客Id列表
     * @return 返回所有符合条件的博客及其标签
     */
    public List<BlogBo> findBlogsBoByIds(List<Long> ids) {
        List<Blog> blogs = findBlogsByIds(ids);
        List<TagWithBlogId> tags = tagService.findTagsByBlogIds(ids);

        return BlogUtils.assemblyBlogAndTag(blogs, tags);
    }

    /**
     * 根据博客Id列表，返回所有符合条件的博客
     *
     * @param ids 博客Id列表
     * @return 返回所有符合条件的博客
     */
    public List<Blog> findBlogsByIds(List<Long> ids) {
        return blogRepository.findByIds(ids);
    }



    public Optional<BlogBo> findBlogDetails(Long blogId) {
        Optional<Blog> blog = findBlog(blogId);
        if (blog.isEmpty()) {
            return Optional.empty();
        }

        List<Tag> tags = tagService.allBlogTags(blogId, Page.unlimited());

        return Optional.of(new BlogBo(blog.get(), tags));
    }

}
