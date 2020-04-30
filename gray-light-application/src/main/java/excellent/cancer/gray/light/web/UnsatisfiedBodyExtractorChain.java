package excellent.cancer.gray.light.web;

import lombok.NonNull;
import org.springframework.web.reactive.function.server.ServerResponse;
import perishing.constraint.treasure.chest.functions.UnsatisfiedPropertyExtractor;
import perishing.constraint.treasure.chest.functions.UnsatisfiedPropertyExtractorChain;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 从哈希Map中提取不满意结果
 *
 * @author XyParaCrim
 */
public class UnsatisfiedBodyExtractorChain implements
        UnsatisfiedPropertyExtractorChain<Mono<ServerResponse>, Map<String, Object>, UnsatisfiedBodyExtractor> {

    private final List<Tuple2<String,
            UnsatisfiedPropertyExtractor<Mono<ServerResponse>, Map<String, Object>>>> chain = new LinkedList<>();

    @Override
    public Optional<Mono<ServerResponse>> extract(@NonNull Map<String, Object> properties) {
        Optional<Mono<ServerResponse>> unsatisfied = Optional.empty();
        for (Tuple2<String, UnsatisfiedPropertyExtractor<Mono<ServerResponse>, Map<String, Object>>> pair : chain) {
            if ((unsatisfied = pair.getT2().extract(pair.getT1(), properties)).isPresent()) {
                break;
            }
        }

        return unsatisfied;
    }

    @SuppressWarnings("unchecked")
    public Mono<ServerResponse> extractOrOther(Map<?, ?> properties, Supplier<Mono<ServerResponse>> other) {
        return extract((Map<String, Object>) properties).orElseGet(other);
    }

    @Override
    public UnsatisfiedBodyExtractorChain chain(String key, @NonNull UnsatisfiedBodyExtractor extractor) {
        chain.add(Tuples.of(key, extractor));
        return this;
    }
}
