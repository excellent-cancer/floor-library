package gray.light.document.customizer;

import gray.light.document.entity.WorksDocument;
import gray.light.owner.entity.OwnerProject;

public final class WorksDocumentCustomizer {

    public static WorksDocument generate(OwnerProject document, Long worksId) {
        return WorksDocument.builder().
                documentId(document.getId()).
                worksId(worksId).
                build();
    }

}
