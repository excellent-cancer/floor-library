package gray.light.blog.repository;

import gray.light.blog.entity.Blog;
import gray.light.blog.entity.Tag;
import gray.light.blog.entity.TagWithBlogId;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import perishing.constraint.jdbc.Page;

import java.util.List;

@Mapper
public interface TagRepository {

    List<?> findByOwnerId(@Param("ownerId") Long ownerId, @Param("page") Page page);

    List<Tag> findByBlogId(@Param("blogId") Long blogId, @Param("page") Page page);

    List<Blog> findBlogsByUseTagsAndOwnerId(@Param("ownerId") Long ownerId, 
                                            @Param("tags") List<Tag> tags,
                                            @Param("page") Page page);

    List<TagWithBlogId> findSpTagByOwnerId(@Param("ownerId") Long ownerId);

    boolean save(@Param("tag") Tag tag);
}
