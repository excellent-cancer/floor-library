package gray.light.support.web;

import gray.light.support.error.ExtractRequestParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import perishing.constraint.jdbc.Page;
import perishing.constraint.treasure.chest.CollectionsTreasureChest;
import perishing.constraint.treasure.chest.collection.FinalVariables;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static gray.light.support.web.ResponseToClient.failWithMessage;

/**
 * @author XyParaCrim
 */
@Slf4j
public final class RequestSupport {

    public static Page extractPage(ServerRequest request) {
        Optional<String> pages = request.queryParam("pages");
        Optional<String> count = request.queryParam("count");

        return pages.isPresent() && count.isPresent() ?
                Page.newPage(pages.get(), count.get()) :
                Page.unlimited();
    }

    public static Mono<ServerResponse> extract(ServerRequest request, Function<FinalVariables<String>,
            Mono<ServerResponse>> then, RequestParam<?> ...paramsTable) {
        Map<String, Object> paramValues = new HashMap<>(paramsTable.length);


        for (RequestParam<?> entry : paramsTable) {
            Object t;
            try {
                String name = entry.getKey();
                RequestParamExtractor<?> extractor = entry.getExtractor();

                t = extractor.extract(request, name);
            } catch (ExtractRequestParamException e) {
                log.error(e.getMessage());
                return failWithMessage(e.getMessage());
            }

            if (t != null) {
                paramValues.put(entry.getKey(), t);
            }
        }

        return then.apply(CollectionsTreasureChest.finalVariables(paramValues));
    }



    public static Mono<ServerResponse> extractLong(String name, ServerRequest request, Function<Long, Mono<ServerResponse>> then) {
        Long value;
        try {
            value = RequestParamExtractors.extractLong(request, name);
        } catch (ExtractRequestParamException e) {
            log.error(e.getMessage());
            return failWithMessage(e.getMessage());
        }

        return then.apply(value);
    }

    public static Mono<ServerResponse> extractOwnerId(ServerRequest request, Function<Long, Mono<ServerResponse>> then) {
        return extractLong("ownerId", request, then);
    }

    public static Mono<ServerResponse> extractLongAndPage(String name, ServerRequest request, BiFunction<Long, Page, Mono<ServerResponse>> then) {
        return extractLong(name, request, id -> {
            Page page = extractPage(request);

            return then.apply(id, page);
        });
    }

    public static Mono<ServerResponse> extractId(ServerRequest request, Function<Long, Mono<ServerResponse>> then) {
        return extractLong("id", request, then);
    }
}
