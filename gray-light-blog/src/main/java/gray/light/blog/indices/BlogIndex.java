package gray.light.blog.indices;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

/**
 * 定义博客索引，包括标题、标签和内容
 *
 * @author XyParaCrim
 */
@Data
@Builder
@Document(useServerConfiguration = true, indexName = BlogIndex.INDEX_NAME)
public class BlogIndex {

    public static final String INDEX_NAME = "blog-index-1";

    @Id
    private Long id;

    private String title;

    private List<String> tags;

    private String content;

}
