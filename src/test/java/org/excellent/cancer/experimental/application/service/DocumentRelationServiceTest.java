package org.excellent.cancer.experimental.application.service;

import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@CommonsLog
@SpringBootTest("excellent.cancer.jdbc.enabled=true")
public class DocumentRelationServiceTest {

    @Autowired
    private DocumentRelationService documentRelationService;

    @Test
    @DisplayName("测试在已创建文档的项目里创建项目")
    public void createDocumentInExistedDocsTest() {
        documentRelationService.
                createDocumentForProject(1L, "Any title").
                doOnSuccess(Assertions::assertNotNull).
                subscribe();
    }

    @Test
    @DisplayName("测试在无效项目中创建文档")
    public void createDocumentForInvalidTest() {
        Long invalidProjectId = Long.MAX_VALUE;
        documentRelationService.
                createDocumentForProject(invalidProjectId, "Any title").
                doOnError(throwable -> {
                    log.error(throwable);
                    Assertions.assertNotNull(throwable);
                }).
                subscribe();
    }
}
