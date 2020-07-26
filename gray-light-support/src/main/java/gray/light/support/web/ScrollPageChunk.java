package gray.light.support.web;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ScrollPageChunk<T> {

    private final String scrollId;

    private final List<T> items;

}
