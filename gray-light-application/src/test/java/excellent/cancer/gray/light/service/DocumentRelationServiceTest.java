package excellent.cancer.gray.light.service;

import excellent.cancer.gray.light.component.SuperOwnerRandomService;
import excellent.cancer.gray.light.jdbc.entities.Document;
import excellent.cancer.gray.light.jdbc.entities.DocumentCatalog;
import excellent.cancer.gray.light.jdbc.entities.DocumentStatus;
import excellent.cancer.gray.light.jdbc.entities.OwnerProject;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private SuperOwnerRandomService superOwnerRandomService;

    @Autowired
    private RepositoryService repositoryService;

    @Test
    @DisplayName("创建新文档 - 无效项目")
    public void createDocumentForInvalidTest() {
        // 设置一个无效项目ID
        DocumentCatalog invalidCatalog = DocumentCatalog.
                builderOnCreateWithNoDefault(DocumentCatalog.ROOT, Long.MAX_VALUE).
                title("Any title").
                build();

        // 提交给服务，则期望其发布的是错误
        Assertions.assertFalse(documentRelationService.createRootCatalogForDocument(invalidCatalog));
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
        Assertions.assertTrue(documentRelationService.createRootCatalogForDocument(documentCatalog));
        Assertions.assertNotNull(documentCatalog.getId());

        log.info("新建文档已保存：" + documentCatalog);

        // 已建文档项目
        Assertions.assertFalse(documentRelationService.createRootCatalogForDocument(documentCatalog));
    }

    @Test
    @Transactional
    @DisplayName("查询文档状态")
    public void queryDocumentStatusTest() {

        BiConsumer<DocumentStatus, Supplier<List<Document>>> test = (status, list) -> {
            List<Document> firstSyncDocs = list.get();

            // 保存一个新项目
            OwnerProject project = superOwnerRandomService.saveOwnerProjectOptionally();

            // 在新项目中添加一个状态已同步的文档
            Document document = Document.
                    builder().
                    title("新文档").
                    description("新文档描述").
                    projectId(project.getId()).
                    repoUrl("www.hh.com").
                    documentStatus(status).
                    build();

            Assertions.assertTrue(documentRelationService.createDocumentForProject(document));

            // 再次获取所有同步文档
            List<Document> secondSyncDocs = list.get();

            // 判断同步文档是否增加一
            Assertions.assertEquals(1, secondSyncDocs.size() - firstSyncDocs.size());

            // 通过第二次获取的同步文档建立id与document的映射
            Map<Long, Document> secondSyncMap = new HashMap<>();
            for (Document d : secondSyncDocs) {
                secondSyncMap.put(d.getId(), d);
            }

            // 将一次获取的同步文档从secondSyncMap删除，且必须存在
            for (Document d : firstSyncDocs) {
                Assertions.assertNotNull(secondSyncMap.remove(d.getId()));
            }

            // 只剩下一个新增
            Assertions.assertEquals(1, secondSyncMap.size());

            // 判断是否一致
            secondSyncMap.values().forEach(d -> {
                Assertions.assertEquals(d.getTitle(), document.getTitle());
                Assertions.assertEquals(d.getDescription(), document.getDescription());
                Assertions.assertEquals(d.getProjectId(), document.getProjectId());
                Assertions.assertEquals(d.getRepoUrl(), document.getRepoUrl());
                Assertions.assertEquals(d.getDocumentStatus(), document.getDocumentStatus());
            });
        };

        test.accept(DocumentStatus.SYNC, documentRelationService::allSyncDocument);
        test.accept(DocumentStatus.EMPTY, documentRelationService::allEmptyDocument);
    }

    @Test
    @Transactional
    @DisplayName("批量更新文档状态")
    public void batchUpdateDocumentStatusTest() {
        DocumentStatus status = DocumentStatus.EMPTY;

        // 保存一个新项目
        OwnerProject project = superOwnerRandomService.saveOwnerProjectOptionally();
        Assertions.assertNotNull(project.getId());

        // 批量储存20条文档
        List<Document> documents = Stream.
                generate(() -> Document.
                        builder().
                        title("新文档").
                        description("新文档描述").
                        projectId(project.getId()).
                        repoUrl("www.hh.com").
                        documentStatus(status).
                        build()).
                limit(20).
                collect(Collectors.toList());

        Assertions.assertTrue(documentRelationService.batchUpdateDocumentRepositories(documents, Collections.emptyList(), Collections.emptyList()));

        // 将所有文档的状态更改为INVALID，然后从数据库取出后再进行对比

        List<Document> saveDocuments = repositoryService.document().findAll();

        saveDocuments.forEach(document -> document.setDocumentStatus(DocumentStatus.INVALID));

        documentRelationService.batchUpdateDocumentStatus(saveDocuments);
        repositoryService.document().findAll().forEach(document -> Assertions.assertEquals(DocumentStatus.INVALID, document.getDocumentStatus()));
    }

}
