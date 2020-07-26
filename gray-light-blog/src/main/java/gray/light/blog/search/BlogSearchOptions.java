package gray.light.blog.search;

import gray.light.blog.indices.BlogIndex;
import gray.light.blog.search.hits.BlogHit;
import gray.light.search.SearchSupport;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchScrollHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import perishing.constraint.jdbc.Page;

import java.util.Collections;

/**
 * 搜索操作
 *
 * @author XyParaCrim
 */
public class BlogSearchOptions {

    private static final long DEFAULT_SCROLL = 1000L * 60 * 5;

    private final IndexCoordinates indexCoordinates;

    private final IndexOperations indexOperations;

    private final ElasticsearchRestTemplate elasticsearchOperations;

    public BlogSearchOptions(ElasticsearchRestTemplate elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.indexOperations = elasticsearchOperations.indexOps(BlogIndex.class);
        this.indexCoordinates = elasticsearchOperations.getIndexCoordinatesFor(BlogIndex.class);
    }

    public SearchScrollHits<BlogHit> searchScrollStart(String words, Page page) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder().
                withFields(BlogHit.ID_FIELD).
                withPageable(PageRequest.of(page.getFrom(), page.getCount())).
                withQuery(SearchSupport.matchFields(words, "title", "content", "tags"));

        return elasticsearchOperations.
                searchScrollStart(DEFAULT_SCROLL, builder.build(), BlogHit.class, indexCoordinates);
    }

    public SearchScrollHits<BlogHit> searchScrollContinue(String scrollId) {
        return elasticsearchOperations.
                searchScrollContinue(scrollId, DEFAULT_SCROLL, BlogHit.class, indexCoordinates);
    }

    public void searchScrollClear(String scrollId) {
        elasticsearchOperations.searchScrollClear(Collections.singletonList(scrollId));
    }

    public void addDocument(BlogIndex blogIndex) {
        elasticsearchOperations.save(blogIndex, indexCoordinates);
    }

}
