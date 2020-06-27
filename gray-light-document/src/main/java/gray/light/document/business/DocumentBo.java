package gray.light.document.business;

import gray.light.owner.entity.OwnerProject;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentBo {

    private Long worksId;

    private OwnerProject document;

    public static DocumentBo of(OwnerProject document, Long worksId) {
        return new DocumentBo(worksId, document);
    }

}
