package excellent.cancer.gray.light.shared.entities;

import excellent.cancer.gray.light.jdbc.entities.DocumentCatalog;
import lombok.Getter;

/**
 * @author XyParaCrim
 */
public class ContainsCatalogCatalogSE extends CatalogSE {

    @Getter
    private final CatalogSE[] catalogs;

    public ContainsCatalogCatalogSE(DocumentCatalog catalog, CatalogSE[] catalogs) {
        super(catalog);
        this.catalogs = catalogs;
    }

    public ContainsCatalogCatalogSE(DocumentCatalog catalog) {
        super(catalog);
        this.catalogs = new CatalogSE[0];
    }

    public ContainsCatalogCatalogSE() {
        super();
        this.catalogs = new CatalogSE[0];
    }

}
