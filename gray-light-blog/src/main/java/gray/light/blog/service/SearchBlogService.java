package gray.light.blog.service;

import gray.light.blog.business.BlogBo;
import gray.light.blog.search.BlogSearchOptions;
import gray.light.blog.search.hits.BlogHit;
import gray.light.support.web.ScrollPageChunk;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.SearchScrollHits;
import perishing.constraint.jdbc.Page;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 提供独特的搜索服务
 *
 * @author XyParaCrim
 */
@RequiredArgsConstructor
public class SearchBlogService {

    private final BlogSearchOptions blogSearchOptions;

    private final ReadableBlogService readableBlogService;

    public ScrollPageChunk<BlogBo> search(String word, Page page) {
        return hitsToScrollPageChunk(blogSearchOptions.searchScrollStart(word, page));
    }

    public ScrollPageChunk<BlogBo> continueSearch(String scrollId) {
        return hitsToScrollPageChunk(blogSearchOptions.searchScrollContinue(scrollId));
    }

    public void clearSearch(String scrollId) {
        blogSearchOptions.searchScrollClear(scrollId);
    }

    private ScrollPageChunk<BlogBo> hitsToScrollPageChunk(SearchScrollHits<BlogHit> hits) {
        List<Long> blogIds = hits.get().map(hit -> hit.getContent().getId()).collect(Collectors.toList());
        List<BlogBo> blogs = readableBlogService.findBlogsBoByIds(blogIds);

        return new ScrollPageChunk<>(hits.getScrollId(), blogs);
    }
}
