package excellent.cancer.gray.light.shared.entities;

import excellent.cancer.gray.light.jdbc.entities.DocumentCatalog;
import lombok.Getter;

/**
 * @author XyParaCrim
 */
public class CatalogSE {

    @Getter
    private final DocumentCatalog data;

    public CatalogSE(DocumentCatalog data) {
        this.data = data;
    }

    public CatalogSE() {
        this(null);
    }

}
