package gray.light.blog.handler;

import gray.light.blog.customizer.TagCustomizer;
import gray.light.blog.entity.Tag;
import gray.light.support.web.RequestParamExtractor;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.List;
import java.util.Optional;

class TagParamExtractor implements RequestParamExtractor<List<Tag>> {

    @Override
    public List<Tag> extract(ServerRequest request, String key) {
        Optional<String> param = request.queryParam(key);

        return param.map(TagCustomizer::batchWrapFromParam).orElse(null);
    }
}
