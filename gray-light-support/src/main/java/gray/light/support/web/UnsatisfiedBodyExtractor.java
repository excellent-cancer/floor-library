package gray.light.support.web;

import org.springframework.web.reactive.function.server.ServerResponse;
import perishing.constraint.treasure.chest.functions.UnsatisfiedPropertyExtractor;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author XyParaCrim
 */
@FunctionalInterface
public interface UnsatisfiedBodyExtractor extends
        UnsatisfiedPropertyExtractor<Mono<ServerResponse>, Map<String, Object>> {

}
