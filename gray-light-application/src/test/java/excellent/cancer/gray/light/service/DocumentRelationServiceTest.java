package excellent.cancer.gray.light.service;

import excellent.cancer.gray.light.component.SuperOwnerRandomService;
import excellent.cancer.gray.light.jdbc.entities.DocumentCatalog;
import excellent.cancer.gray.light.jdbc.entities.OwnerProject;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * 对{@link DocumentCatalog}进行单元测试
 *
 * @author XyParaCrim
 */
@CommonsLog
@SpringBootTest("excellent.cancer.jdbc.enabled=true")
public class DocumentRelationServiceTest {

    @Autowired
    private DocumentRelationService documentRelationService;

    @Autowired
    private SuperOwnerService superOwnerService;

    @Autowired
    private SuperOwnerRandomService superOwnerRandomService;

    @Test
    @DisplayName("创建新文档 - 无效项目")
    public void createDocumentForInvalidTest() {
        // 设置一个无效项目ID
        DocumentCatalog invalidCatalog = DocumentCatalog.
                builderOnCreateWithNoDefault(DocumentCatalog.ROOT, Long.MAX_VALUE).
                title("Any title").
                build();

        // 提交给服务，则期望其发布的是错误
        Assertions.assertFalse(documentRelationService.createRootCatalogForProject(invalidCatalog));
        ;
    }

    @Test
    @Transactional
    @DisplayName("创建新文档 - 已建文档项目")
    public void createDocumentForProjectTest() {
        OwnerProject savedProject = superOwnerRandomService.saveOwnerProjectOptionally();
        Assertions.assertNotNull(savedProject.getId());

        DocumentCatalog documentCatalog = DocumentCatalog
                .builderOnCreateWithNoDefault(DocumentCatalog.ROOT, savedProject.getId())
                .title("测试标题")
                .build();

        // 如果创建文档时发生错误，则返回错误结果
        Assertions.assertTrue(documentRelationService.createRootCatalogForProject(documentCatalog));
        Assertions.assertNotNull(documentCatalog.getId());

        log.info("新建文档已保存：" + documentCatalog);

        // 已建文档项目
        Assertions.assertFalse(documentRelationService.createRootCatalogForProject(documentCatalog));
    }

}
