package gray.light.book.business;

import gray.light.book.entity.BookCatalog;
import lombok.Getter;

/**
 * @author XyParaCrim
 */
public class ContainsCatalogCatalogBo extends CatalogBo {

    @Getter
    private final CatalogBo[] catalogs;

    public ContainsCatalogCatalogBo(BookCatalog catalog, CatalogBo[] catalogs) {
        super(catalog);
        this.catalogs = catalogs;
    }

    public ContainsCatalogCatalogBo(BookCatalog catalog) {
        super(catalog);
        this.catalogs = new CatalogBo[0];
    }

    public ContainsCatalogCatalogBo() {
        super();
        this.catalogs = new CatalogBo[0];
    }

}
