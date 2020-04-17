package excellent.cancer.gray.light.service;

import excellent.cancer.gray.light.component.SuperOwnerRandomService;
import excellent.cancer.gray.light.config.ExcellentCancerProperties;
import excellent.cancer.gray.light.config.OwnerProperties;
import excellent.cancer.gray.light.jdbc.entities.Owner;
import excellent.cancer.gray.light.jdbc.entities.OwnerProject;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@CommonsLog
@SpringBootTest("excellent.cancer.jdbc.enabled=true")
public class SuperOwnerServiceTest {

    @Autowired
    private ExcellentCancerProperties excellentCancerProperties;

    @Autowired
    private SuperOwnerService superOwnerService;

    @Autowired
    private SuperOwnerRandomService superOwnerRandomService;

    @Test
    @DisplayName("验证超级所属者数据是否一致 - Spring属性与数据库数据是否一致")
    public void verifyConsistentSuperOwnerData() {
        Owner superOwner = superOwnerService.superOwner();
        OwnerProperties ownerProperties = excellentCancerProperties.getOwner();

        Assertions.assertEquals(ownerProperties.getName(), superOwner.getUsername());
        Assertions.assertEquals(ownerProperties.getOrganization(), superOwner.getOrganization());
    }

    @Nested
    @DisplayName("针对OwnerProject表进行测试: ")
    public class OwnerProjectTest {

        private final OwnerProject project = superOwnerRandomService.createOwnerProjectOptionally();

        @Test
        @DisplayName("保存一个新增项目 - 删除一个新增项目 - 查询不存在的项目")
        public void saveRandomProject() {
            log.info("新建项目: \n" + project);
            superOwnerService.addProject(project);
            log.info("已存项目: \n" + project);
            superOwnerService.removeProject(project);
            Assertions.assertNotNull(project.getId());
            Assertions.assertFalse(superOwnerService.existsProject(project.getId()));
            Assertions.assertFalse(superOwnerService.existsProject(Long.MAX_VALUE));
        }

    }

}
