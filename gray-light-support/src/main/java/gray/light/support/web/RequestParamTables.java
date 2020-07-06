package gray.light.support.web;

import perishing.constraint.jdbc.Page;
import perishing.constraint.treasure.chest.CollectionsTreasureChest;

public final class RequestParamTables {

    public static final RequestParam<Long> OWNER_ID = paramTable("ownerId", RequestParamExtractors::extractLong);

    public static final RequestParam<Long> ID = paramTable("id", RequestParamExtractors::extractLong);

    public static final RequestParam<Page> PAGE = paramTable("page", RequestParamExtractors::extractPage);

    public static final RequestParam<Page> TAGS = paramTable("tage", RequestParamExtractors::extractPage);


    public static  <T> RequestParam<T> paramTable(String name, RequestParamExtractor<T> extractor) {
        return new RequestParam<>(CollectionsTreasureChest.entry(name, extractor));
    }

    public static RequestParam<String> paramTable(String name) {
        return paramTable(name, RequestParamExtractors::extract);
    }

    public static RequestParam<Long> ownerId() {
        return OWNER_ID;
    }

    public static RequestParam<Long> id() {
        return ID;
    }

    public static RequestParam<Page> page() {
        return PAGE;
    }

}
