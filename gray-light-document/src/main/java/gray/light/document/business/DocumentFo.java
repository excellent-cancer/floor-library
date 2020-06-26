package gray.light.document.business;

import gray.light.owner.business.OwnerProjectFo;
import gray.light.support.error.NormalizingFormException;
import lombok.Data;

/**
 * 创建文档的表单
 *
 * @author XyParaCrim
 */
@Data
public class DocumentFo {

    private Long worksId;

    private OwnerProjectFo document;

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
    }

}
