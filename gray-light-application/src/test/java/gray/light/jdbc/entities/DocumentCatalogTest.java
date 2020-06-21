package gray.light.jdbc.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DocumentCatalogTest {

    @Test
    @DisplayName("通过Builder创建DocumentCatalog - 缺少必要属性")
    public void createErrorDocumentCatalogByBuilder() {
/*        Assertions.assertThrows(NullPointerException.class, () -> DocumentCatalog.builder().build());
        Assertions.assertThrows(NullPointerException.class, () -> DocumentCatalog.builderOnCreate().build());*/
    }

    @Test
    @DisplayName("通过Builder创建DocumentCatalog - 设置必要的属性")
    public void createDocumentCatalogByBuilder() {
/*        Assertions.assertDoesNotThrow(() -> {
            DocumentCatalog documentCatalog = DocumentCatalog.
                    builderOnCreate().
                    projectId(1L).
                    parentId(1L).
                    build();

            Assertions.assertEquals(DocumentCatalog.Folder.EMPTY, documentCatalog.getFolder());
            Assertions.assertEquals("", documentCatalog.getTitle());
        });*/
    }

}
