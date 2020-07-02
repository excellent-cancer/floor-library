package gray.light.blog.service;

import gray.light.blog.entity.Blog;
import gray.light.blog.entity.Tag;
import gray.light.blog.repository.TagRepository;
import gray.light.support.web.PageChunk;
import gray.light.support.web.PageSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import perishing.constraint.jdbc.Page;

import java.util.List;

/**
 * 提供标签领域的相关功能
 *
 * @author XyParaCrim
 */
@Slf4j
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    /**
     * 查询所属者博客所有用到的标签
     *
     * @param ownerId 所属者Id
     * @param page 分页
     * @return 查询所属者博客所有用到的标签
     */
    public PageChunk<Tag> allOwnerTags(Long ownerId, Page page) {
        return PageSupport.page(tagRepository.findByOwnerId(ownerId, page.nullable()), page);
    }

    /**
     * 查询博客所有用到的标签
     *
     * @param blogId 博客ID
     * @param page 分页
     * @return 查询博客所有用到的标签
     */
    public List<Tag> allBlogTags(Long blogId, Page page) {
        return tagRepository.findByBlogId(blogId, page.nullable());
    }

    /**
     * 所属者的博客中使用到这些标签的项目
     *
     * @param tags 使用到的标签
     * @param ownerId 所属者Id
     * @param page 分页
     * @return 所属者的博客中使用到这些标签的项目
     */
    public List<Blog> allUseTagBlogs(List<Tag> tags, Long ownerId, Page page) {
        return tagRepository.findBlogsByUseTagsAndOwnerId(ownerId, tags, page);
    }

    /**
     * 创建一个标签
     *
     * @param tag 创建的标签
     * @return 是否创建成功
     */
    public boolean createTag(Tag tag) {
        return tagRepository.save(tag);
    }

}
