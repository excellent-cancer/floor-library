package excellent.cancer.gray.light.jdbc.entities.builder;

import excellent.cancer.gray.light.jdbc.entities.Document;
import excellent.cancer.gray.light.jdbc.entities.DocumentStatus;
import lombok.Generated;

import java.util.Date;
import java.util.Map;

/**
 * 文档实体的构建器
 *
 * @author XyParaCrim
 */
@Generated
public class DocumentBuilder {

    /**
     * 通过指定的properties构建文档实体，并添加必要的属性
     *
     * @param properties 属性值
     * @return 文档实体的构建器
     */
    public static DocumentBuilder buildNecessaryProperties(Map<?, ?> properties) {
        return builder().
                title((String) properties.get("title")).
                description((String) properties.get("description")).
                repoUrl((String) properties.get("repoUrl")).
                projectId((Long) properties.get("projectId")).
                status(DocumentStatus.EMPTY);
    }

    // 机器生成的代码

    private Long id;
    private Long projectId;
    private String title;
    private String description;
    private String repoUrl;
    private Date createdDate;
    private Date updatedDate;
    private DocumentStatus status;

    DocumentBuilder() {
    }

    public DocumentBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public DocumentBuilder projectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public DocumentBuilder title(String title) {
        this.title = title;
        return this;
    }

    public DocumentBuilder description(String description) {
        this.description = description;
        return this;
    }

    public DocumentBuilder repoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
        return this;
    }

    public DocumentBuilder createdDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public DocumentBuilder updatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public DocumentBuilder status(DocumentStatus status) {
        this.status = status;
        return this;
    }

    public Document build() {
        return null;
        // return new Document(id, projectId, title, description, repoUrl, createdDate, updatedDate, status);
    }

    @Override
    public String toString() {
        return "Document.DocumentBuilder(id=" + this.id + ", projectId=" + this.projectId + ", title=" + this.title + ", description=" + this.description + ", repoUrl=" + this.repoUrl + ", createdDate=" + this.createdDate + ", updatedDate=" + this.updatedDate + ", status=" + this.status + ")";
    }

    public static DocumentBuilder builder() {
        return new DocumentBuilder();
    }
}
