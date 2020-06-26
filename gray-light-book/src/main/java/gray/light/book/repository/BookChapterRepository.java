package gray.light.book.repository;

import gray.light.book.entity.BookChapter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import perishing.constraint.jdbc.Page;

import java.util.List;

@Mapper
public interface BookChapterRepository {

    List<BookChapter> findByOwnerProjectId(@Param("ownerProjectId") Long ownerProjectId, @Param("page") Page page);

}
