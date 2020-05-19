package excellent.cancer.gray.light.jdbc.support;

import lombok.Data;

/**
 * 表示分页的页码和个数
 *
 * @author XyParaCrim
 */
@Data
public class Page {

    private final int from;

    private final int count;

    public static Page newPage(int pages, int count) {
        return new Page((pages - 1) * count, count);
    }

    public static Page newPage(String pages, String count) {
        int parsedPages = Integer.parseInt(pages);
        int parsedCount = Integer.parseInt(count);

        return newPage(parsedPages, parsedCount);
    }

}
