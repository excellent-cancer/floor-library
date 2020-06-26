package gray.light.book.business;

import gray.light.book.entity.BookCatalog;
import lombok.Getter;

/**
 * @author XyParaCrim
 */
public class CatalogBo {

    @Getter
    private final BookCatalog data;

    public CatalogBo(BookCatalog data) {
        this.data = data;
    }

    public CatalogBo() {
        this(null);
    }

}
