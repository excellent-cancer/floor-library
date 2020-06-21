package gray.light.shared.entities;

import gray.light.document.entity.DocumentCatalog;
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
