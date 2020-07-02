package gray.light.support.web;

import gray.light.support.error.ExtractRequestParamException;
import org.springframework.web.reactive.function.server.ServerRequest;

public interface RequestParamExtractor<T> {

    T extract(ServerRequest request, String key) throws ExtractRequestParamException;

}
