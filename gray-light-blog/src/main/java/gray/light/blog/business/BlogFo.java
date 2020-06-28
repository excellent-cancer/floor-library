package gray.light.blog.business;

import gray.light.support.error.NormalizingFormException;
import lombok.Data;

import static org.springframework.util.StringUtils.isEmpty;

@Data
public class BlogFo {

    private Long ownerId;

    private String title;

    private String content;

    /**
     * 对请求表单进行标准化和检查
     *
     * @throws NormalizingFormException 表单属性不合格的时候
     */
    public void normalize() throws NormalizingFormException {
        if (ownerId == null) {
            NormalizingFormException.emptyProperty("ownerId");
        }

        if (title == null || isEmpty(title)) {
            NormalizingFormException.emptyProperty("document");
        }

        if (content == null) {
            NormalizingFormException.emptyProperty("source");
        }
    }
}
