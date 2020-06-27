package gray.light.owner.business;

import gray.light.support.error.NormalizingFormException;
import lombok.Data;
import perishing.constraint.treasure.chest.ResourceTreasureChest;

@Data
public class ProjectDetailsFo {

    private String http;


    /**
     * 对请求表单进行标准化和检查
     *
     * @throws NormalizingFormException 表单属性不合格的时候
     */
    public void normalize() throws NormalizingFormException {

        if (!ResourceTreasureChest.isGitUrl(http)) {
            NormalizingFormException.emptyProperty("http");
        }

    }

}
