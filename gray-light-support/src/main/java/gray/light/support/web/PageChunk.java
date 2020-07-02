package gray.light.support.web;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class PageChunk<T> {

    private final int pages;

    private final int count;

    private final int total;

    private final List<T> items;

}
