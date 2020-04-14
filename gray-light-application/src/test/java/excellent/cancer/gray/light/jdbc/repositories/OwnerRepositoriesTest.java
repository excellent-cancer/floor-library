package excellent.cancer.gray.light.jdbc.repositories;

import excellent.cancer.gray.light.config.ExcellentCancerProperties;
import excellent.cancer.gray.light.config.OwnerProperties;
import excellent.cancer.gray.light.jdbc.entities.Owner;
import lombok.extern.apachecommons.CommonsLog;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 对JdbcRepositories的基本测试
 *
 * @author XyParaCrim
 */
@CommonsLog
@SpringBootTest("excellent.cancer.jdbc.enabled=true")
public class OwnerRepositoriesTest {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private ExcellentCancerProperties excellentCancerProperties;

    @Test
    @DisplayName("验证唯一用户名")
    public void verifyUserUniqueness() {
        List<Owner> allOwners = Lists.newArrayList(ownerRepository.findAll());
        Assertions.assertEquals(1, allOwners.size(), "存在多个所属者");

        Owner owner = allOwners.get(0);
        OwnerProperties ownerProperties = excellentCancerProperties.getOwner();

        Assertions.assertEquals(ownerProperties.getName(), owner.getUsername());
        Assertions.assertEquals(ownerProperties.getOrganization(), owner.getOrganization());
    }

    @Test
    @DisplayName("验证关联表在实体类调用get方法时，是否执行Sql操作")
    public void verifyWhetherExecuteSqlWhenCallGetFromReference() {
/*        Owner owner = UniqueOwnerException.extractOwnerRequireUniqueOwner(asList(ownerRepository.findAll()));
        OwnerLink ownerLink = new OwnerLink(null, owner.getId(), "www.excellent-cancer.com", "主页" + new Random().nextInt());
        owner.getLinks().add(ownerLink);

        Assertions.assertNull(ownerLink.getId());

        Set<OwnerLink> origin = owner.getLinks();

        // 执行储存
        Owner savedOwner = ownerRepository.save(owner);

        Assertions.assertEquals(owner, savedOwner);
        Assertions.assertEquals(origin, savedOwner.getLinks());
        Assertions.assertTrue(savedOwner.getLinks().contains(ownerLink));
        Assertions.assertNull(ownerLink.getId());*/
    }

}
