package gray.light.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 表示一篇文章的标签
 *
 * @author XyParaCrim
 */
@Data
@Alias("Tag")
@Builder
@AllArgsConstructor
public class Tag {

    private String name;

    private String color;

    private Date createdDate;

    private Date updatedDate;

}
