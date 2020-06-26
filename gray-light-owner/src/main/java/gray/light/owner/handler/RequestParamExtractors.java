package gray.light.owner.handler;

import gray.light.support.error.ExtractRequestParamException;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Optional;

class RequestParamExtractors {

    interface RequestParamExtractor<T> {

        T extract(ServerRequest request, String key) throws ExtractRequestParamException;

    }

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

    static Long extractLong(ServerRequest request, String key) throws ExtractRequestParamException {
        return LONG_EXTRACTOR.extract(request, key);
    }

}
