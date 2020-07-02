package gray.light.blog.customizer;

import gray.light.blog.entity.Tag;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author XyParaCrim
 */
public final class TagCustomizer {

    public static Tag wrap(String tag) {
        return Tag.builder().name(tag).build();
    }

    public static List<Tag> batchWrapFromParam(String param) {
        return Arrays.stream(param.split(",")).map(TagCustomizer::wrap).collect(Collectors.toList());
    }
}
