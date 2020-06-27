package gray.light.document.business;

import gray.light.owner.business.OwnerProjectFo;
import gray.light.owner.customizer.ProjectDetailsCustomizer;
import gray.light.owner.entity.OwnerProject;
import gray.light.owner.entity.ProjectDetails;
import gray.light.support.error.NormalizingFormException;
import lombok.Data;
import perishing.constraint.treasure.chest.ResourceTreasureChest;

/**
 * 创建文档的表单
 *
 * @author XyParaCrim
 */
@Data
public class DocumentFo {

    private Long worksId;

    private OwnerProjectFo document;

    private String http;

    /**
     * 对请求表单进行标准化和检查
     *
     * @throws NormalizingFormException 表单属性不合格的时候
     */
    public void normalize() throws NormalizingFormException {
        if (document == null) {
            NormalizingFormException.emptyProperty("document");
        }
        document.normalize();

        if (worksId == null) {
            NormalizingFormException.emptyProperty("worksId");
        }

        if (!ResourceTreasureChest.isGitUrl(http)) {
            NormalizingFormException.emptyProperty("http");
        }
    }

    public ProjectDetails generate(OwnerProject documentOwnerProject) {
        return ProjectDetailsCustomizer.ofNewBookFromOwner(documentOwnerProject.getId(), http);
    }

}
