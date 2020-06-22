package gray.light.business;

import gray.light.document.entity.DocumentCatalog;
import lombok.Getter;

/**
 * @author XyParaCrim
 */
public class CatalogBo {

    @Getter
    private final DocumentCatalog data;

    public CatalogBo(DocumentCatalog data) {
        this.data = data;
    }

    public CatalogBo() {
        this(null);
    }

}
