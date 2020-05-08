package excellent.cancer.gray.light.step;

import excellent.cancer.gray.light.component.SuperOwnerRandomService;
import excellent.cancer.gray.light.jdbc.entities.OwnerProject;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@CommonsLog
@SpringBootTest(value = {"excellent.cancer.jdbc.enabled=true", "excellent.cancer.fastdfs.enabled=true"})
public class BatchUpdateDocumentRepositoriesStepTest {

    @Autowired
    private SuperOwnerRandomService superOwnerRandomService;

    @Test
    @DisplayName("遍历更新文档、目录、文章")
    public void updateTest() {
        OwnerProject savedProject = superOwnerRandomService.saveOwnerProjectOptionally();
        Assertions.assertNotNull(savedProject.getId());

    }

}
