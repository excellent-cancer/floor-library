package gray.light.source.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ImagesRepository {

    boolean save(@Param("link") String link, @Param("suffix") String suffix);

    boolean delete(@Param("link") String link);
}
