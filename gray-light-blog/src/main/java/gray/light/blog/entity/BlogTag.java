package gray.light.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 表示标签与博客的关系
 *
 * @author XyParaCrim
 */
@Data
@Alias("BlogTag")
@Builder
@AllArgsConstructor
public class BlogTag {

    private String tag;

    private Long blogId;

    private Date createdDate;

    private Date updatedDate;

}
