package excellent.cancer.gray.light.service;

import excellent.cancer.gray.light.component.UniqueOwnerRandomService;
import excellent.cancer.gray.light.jdbc.entities.OwnerProject;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@CommonsLog
// @SpringBootTest(value = "excellent.cancer.jdbc.enabled=true", classes = UniqueOwnerRandomService.class)
@SpringBootTest("excellent.cancer.jdbc.enabled=true")
public class UniqueOwnerServiceTest {

    @Autowired
    private UniqueOwnerRandomService uniqueOwnerRandomService;

    @Autowired
    private UniqueOwnerService uniqueOwnerService;

    /**
     * 针对OwnerProject表进行测试
     */
    @Nested
    @DisplayName("owner-project: ")
    public class OwnerProjectTest {

        private final OwnerProject project = uniqueOwnerRandomService.createOwnerProjectOptionally();

        @Test
        @DisplayName("保存一个新增项目 - 删除一个新增项目")
        public void saveRandomProject() {
            log.info("新建项目: \n" + project);
            OwnerProject saved = uniqueOwnerService.addProject(project).block();

            log.info("已存项目: \n" + project);
            Assertions.assertNotNull(saved);
            Assertions.assertNotNull(saved.getId());

            Optional<OwnerProject> found;
            Assertions.assertNotNull(found = uniqueOwnerService.project(project).block());
            Assertions.assertTrue(found.isPresent());

            uniqueOwnerService.removeProject(saved).block();
            Assertions.assertNotNull(found = uniqueOwnerService.project(project).block());
            Assertions.assertTrue(found.isEmpty());
        }

        @Test
        @DisplayName("查询不存在的项目")
        public void queryProjectTest() {
            Optional<OwnerProject> found = uniqueOwnerService.project(OwnerProject.justIdProject(Long.MAX_VALUE)).block();

            Assertions.assertNotNull(found);
            Assertions.assertTrue(found.isEmpty());
        }
    }

}
