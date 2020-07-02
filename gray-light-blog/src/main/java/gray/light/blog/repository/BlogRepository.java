package gray.light.blog.repository;

import gray.light.blog.business.BlogBo;
import gray.light.blog.entity.Blog;
import gray.light.blog.entity.Tag;
import gray.light.support.web.PageChunk;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import perishing.constraint.jdbc.Page;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BlogRepository {

    List<?> findByOwnerId(@Param("ownerId") Long ownerId, @Param("page") Page page);

    boolean save(Blog blog);

    Optional<Blog> find(@Param("id") Long id);

    List<?> findByTagsAndOwnerId(@Param("ownerId") Long ownerId, @Param("tags") List<Tag> tags, @Param("page") Page page);

    List<BlogBo> findByOwnerIdPro(@Param("ownerId") Long ownerId, @Param("page") Page page);

}
