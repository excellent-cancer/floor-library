package gray.light.owner.customizer;

import gray.light.owner.entity.ProjectDetails;
import gray.light.owner.entity.ProjectStructure;

public final class ProjectDetailsCustomizer {

    public final static String OWNER_TYPE = "owner";

    public static ProjectDetails ofNewBookFromOwner(Long originId, String http) {
        return ProjectDetails.builder().
                originId(originId).
                type(OWNER_TYPE).
                http(http).
                structure(ProjectStructure.BOOK).
                build();
    }

}
