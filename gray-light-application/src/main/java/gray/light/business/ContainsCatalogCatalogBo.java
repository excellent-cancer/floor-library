package gray.light.business;

import gray.light.document.entity.DocumentCatalog;
import lombok.Getter;

/**
 * @author XyParaCrim
 */
public class ContainsCatalogCatalogBo extends CatalogBo {

    @Getter
    private final CatalogBo[] catalogs;

    public ContainsCatalogCatalogBo(DocumentCatalog catalog, CatalogBo[] catalogs) {
        super(catalog);
        this.catalogs = catalogs;
    }

    public ContainsCatalogCatalogBo(DocumentCatalog catalog) {
        super(catalog);
        this.catalogs = new CatalogBo[0];
    }

    public ContainsCatalogCatalogBo() {
        super();
        this.catalogs = new CatalogBo[0];
    }

}
