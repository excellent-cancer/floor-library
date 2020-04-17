package excellent.cancer.gray.light.jdbc.repositories;

import excellent.cancer.gray.light.jdbc.entities.Owner;
import excellent.cancer.gray.light.service.RepositoryService;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * 对JdbcRepositories的基本测试
 *
 * @author XyParaCrim
 */
@CommonsLog
@SpringBootTest("excellent.cancer.jdbc.enabled=true")
public class OwnerRepositoriesTest {

    @Autowired
    private RepositoryService repositoryService;

    @CsvSource("'test-name', 'test-organization'")
    @ParameterizedTest
    @Transactional
    @DisplayName("验证save方法的返回值")
    public void verifyWhetherExecuteSqlWhenCallGetFromReference(String ownerName, String ownerOrganization) {
        Owner owner = new Owner(null, ownerName, ownerOrganization);

        Assertions.assertTrue(repositoryService.ofOwner().save(owner));
        Assertions.assertNotNull(owner.getId());
    }

    @CsvSource("'test-name', 'test-organization'")
    @ParameterizedTest
    @Transactional
    @DisplayName("验证exists方法的返回值")
    public void verifyReturnTypeWhenCallExistsMethod(String ownerName, String ownerOrganization) {
        Owner owner = new Owner(null, ownerName, ownerOrganization);

        repositoryService.ofOwner().save(owner);

        Assertions.assertNotNull(owner.getId());
        Assertions.assertTrue(repositoryService.ofOwner().existsById(owner.getId()));
        Assertions.assertFalse(repositoryService.ofOwner().existsById(Long.MAX_VALUE));
    }
}
