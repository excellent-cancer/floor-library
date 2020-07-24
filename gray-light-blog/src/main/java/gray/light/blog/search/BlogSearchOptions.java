package gray.light.blog.search;

import gray.light.blog.indices.BlogIndex;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;

/**
 * 搜索操作
 *
 * @author XyParaCrim
 */
public class BlogSearchOptions {

    private final IndexOperations indexOperations;

    public BlogSearchOptions(ElasticsearchOperations elasticsearchOperations) {
        this.indexOperations = elasticsearchOperations.indexOps(BlogIndex.class);
    }

}
