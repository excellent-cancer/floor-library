package gray.light.note.business;

import gray.light.owner.business.OwnerProjectFo;
import gray.light.owner.business.ProjectDetailsFo;
import gray.light.support.error.NormalizingFormException;
import lombok.Data;

/**
 * 笔记的创建表单
 *
 * @author XyParaCrim
 */
@Data
public class NoteFo {

    private OwnerProjectFo note;

    private ProjectDetailsFo source;

    /**
     * 对请求表单进行标准化和检查
     *
     * @throws NormalizingFormException 表单属性不合格的时候
     */
    public void normalize() throws NormalizingFormException {
        if (note == null) {
            NormalizingFormException.emptyProperty("note");
        }
        note.normalize();

        if (source == null) {
            NormalizingFormException.emptyProperty("source");
        }
        source.normalize();
    }

}
