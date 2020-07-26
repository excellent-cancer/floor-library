package gray.light.search;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public final class SearchSupport {

    public static QueryBuilder matchFields(String text, String... fields) {
        if (fields.length == 0) {
            throw new IllegalArgumentException();
        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (String field : fields) {
            boolQueryBuilder.should(QueryBuilders.matchQuery(field, text));
        }

        return boolQueryBuilder;
    }


}
