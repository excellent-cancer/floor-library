package gray.light.blog.repository;

import gray.light.blog.entity.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import perishing.constraint.jdbc.Page;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BlogRepository {

    List<Blog> findByOwnerId(@Param("ownerId") Long ownerId, @Param("page") Page page);

    boolean save(Blog blog);

    Optional<Blog> find(@Param("id") Long id);
}
