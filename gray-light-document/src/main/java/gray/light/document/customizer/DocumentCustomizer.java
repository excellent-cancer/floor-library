package gray.light.document.customizer;

import gray.light.document.entity.Document;
import gray.light.document.entity.DocumentStatus;

import java.util.Map;


/**
 * 文档实体的自定义器
 *
 * @author XyParaCrim
 */
public final class DocumentCustomizer {

    /**
     * 通过指定的properties构建文档实体，并添加必要的属性
     *
     * @param properties 属性值
     * @return 文档实体
     */
    public static Document necessary(Map<?, ?> properties) {
        return Document.builder().
                title((String) properties.get("title")).
                description((String) properties.get("description")).
                repoUrl((String) properties.get("repoUrl")).
                projectId((Long) properties.get("projectId")).
                documentStatus(DocumentStatus.EMPTY).
                build();
    }

}
