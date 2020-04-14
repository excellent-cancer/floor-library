package excellent.cancer.gray.light.service;

import excellent.cancer.gray.light.component.UniqueOwnerRandomService;
import excellent.cancer.gray.light.jdbc.entities.DocumentCatalog;
import excellent.cancer.gray.light.jdbc.entities.OwnerProject;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

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
    private UniqueOwnerService uniqueOwnerService;

    @Autowired
    private UniqueOwnerRandomService uniqueOwnerRandomService;

    /**
     * 针对创建文档进行测试
     */
    @Nested
    @DisplayName("创建新文档 - ")
    public class CreateDocumentTest {

        // 当发生错误时，返回此DocumentCatalog，用于断言
        private final DocumentCatalog ERROR_RESULT = DocumentCatalog.
                builderOnCreateWithNoDefault(Long.MAX_VALUE, Long.MAX_VALUE)
                .build();

        // 随机生成一个项目用于测试
        private OwnerProject project = uniqueOwnerRandomService.createOwnerProjectOptionally();

        @BeforeEach
        public void createTestProject() {
            project = uniqueOwnerService.
                    addProject(project).
                    doOnSubscribe(subscription -> log.info("进行新建项目：" + project)).
                    doOnSuccess(p -> log.info("已存项目：" + p)).
                    block();
        }

        @AfterEach
        public void deleteTestProject() {
            uniqueOwnerService.
                    removeProject(project).
                    doOnSubscribe(subscription -> log.info("进行删除项目：" + project)).
                    doOnSuccess(aVoid -> log.info("已删除项目：" + project)).
                    doOnError(error -> log.error("删除项目发生错误", error)).
                    subscribe();
        }

        @Test
        @DisplayName("无效项目")
        public void createDocumentForInvalidTest() {
            // 设置一个无效项目ID
            DocumentCatalog invalidCatalog = DocumentCatalog.
                    builderOnCreateWithNoDefault(DocumentCatalog.ROOT, Long.MAX_VALUE).
                    title("Any title").
                    build();

            // 提交给服务，则期望其发布的是错误
            assertPublishError(
                    documentRelationService.
                            createCatalogForProject(invalidCatalog).
                            doOnError(throwable -> log.error("存储无效项目，服务报错：", throwable))
            );
        }

        @Test
        @DisplayName("创建新文档 - 已建文档项目")
        public void createDocumentForProjectTest() {
            DocumentCatalog documentCatalog = DocumentCatalog
                    .builderOnCreateWithNoDefault(DocumentCatalog.ROOT, project.getId())
                    .title("测试标题")
                    .build();

            // 如果创建文档时发生错误，则返回错误结果

            assertNotPublishError(
                    documentRelationService.
                            createCatalogForProject(documentCatalog).
                            doOnError(error -> log.error("创建文档错误，服务报错：", error)).
                            doOnSuccess(catalog -> log.info("新建文档已保存：" + catalog))
            );

            // 已建文档项目

            assertPublishError(
                    documentRelationService
                            .createCatalogForProject(
                                    // 再次创建一个该项目的文档目录
                                    DocumentCatalog.
                                            builderOnCreateWithNoDefault(DocumentCatalog.ROOT, project.getId()).
                                            build()
                            )
                            .doOnError(throwable -> log.info("保存新建文档至已建文档项目", throwable))
            );
        }

        private void assertPublishError(Mono<DocumentCatalog> publisher) {
            Assertions.assertEquals(ERROR_RESULT, publisher.onErrorReturn(ERROR_RESULT).block());
        }

        private void assertNotPublishError(Mono<DocumentCatalog> publisher) {
            Assertions.assertNotEquals(ERROR_RESULT, publisher.onErrorReturn(ERROR_RESULT).block());
        }

    }

}
