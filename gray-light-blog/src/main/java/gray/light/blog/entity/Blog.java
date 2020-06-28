package gray.light.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 一篇博客
 *
 * @author XyParaCrim
 */
@Data
@Alias("Blog")
@Builder
@AllArgsConstructor
public class Blog {

    private Long id;

    private Long ownerId;

    private Date createdDate;

    private Date updatedDate;

    @Builder.Default
    private String downloadLink = "";

    private String title;

}
