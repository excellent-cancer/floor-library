package gray.light.support.web;

import gray.light.support.error.ExtractRequestParamException;
import org.springframework.web.reactive.function.server.ServerRequest;
import perishing.constraint.jdbc.Page;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RequestParamExtractors {

    public static Long extractLong(ServerRequest request, String key) throws ExtractRequestParamException {
        return LONG_EXTRACTOR.extract(request, key);
    }

    public static Page extractPage(ServerRequest request, String key) throws ExtractRequestParamException {
        return PAGE_EXTRACTOR.extract(request, key);
    }

    public static String extract(ServerRequest request, String key) throws ExtractRequestParamException {
        return STRING_EXTRACTOR.extract(request, key);
    }

    private static final Map<Class<?>, RequestParamExtractor<?>> EXTRACTOR_MAP = new HashMap<>(){{
        put(Long.class, RequestParamExtractors::extractLong);
    }};

    @SuppressWarnings("unchecked")
    public static <T> T extract(ServerRequest request, Class<T> type, String key) throws ExtractRequestParamException {
        if (EXTRACTOR_MAP.containsKey(type)) {
            return (T) EXTRACTOR_MAP.get(type).extract(request ,key);
        }

        throw new RuntimeException("Failed to match param extractor: " + type);
    }



    // 提取器实现

    private final static RequestParamExtractor<Long> LONG_EXTRACTOR = (request, key) -> {
        Optional<String> param = request.queryParam(key);

        if (param.isEmpty()) {
            throw new ExtractRequestParamException("Missing value of parameter: " + key);
        }

        try {
            return Long.valueOf(param.get());
        } catch (NumberFormatException e) {
            throw new ExtractRequestParamException("Parameter of wrong type: " + key, e);
        }
    };

    private final static RequestParamExtractor<Page> PAGE_EXTRACTOR = (request, key) -> {
        Optional<String> pages = request.queryParam("pages");
        Optional<String> count = request.queryParam("count");

        if (pages.isEmpty() && count.isEmpty()) {
            return Page.unlimited();
        }

        if (pages.isEmpty() || count.isEmpty()) {
            throw new ExtractRequestParamException("Illegal params 'pages,count': " + pages.orElse("<empty>") + "," + count.orElse("<empty>"));
        }

        Page page;
        try {
            page = Page.newPage(pages.get(), count.get());
        } catch (NumberFormatException e) {
            throw new ExtractRequestParamException(e);
        }

        if (page.isUnlimited()) {
            throw new ExtractRequestParamException("Illegal params 'pages,count': " + pages.get() + "," + count.get());
        }

        return page;

    };

    private final static RequestParamExtractor<String> STRING_EXTRACTOR = ((request, key) -> {
        Optional<String> param = request.queryParam(key);

        if (param.isEmpty()) {
            throw new ExtractRequestParamException("Missing value of parameter: " + key);
        }

        return param.get();
    });

}
