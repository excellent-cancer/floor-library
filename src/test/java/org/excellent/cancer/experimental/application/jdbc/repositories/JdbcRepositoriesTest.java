package org.excellent.cancer.experimental.application.jdbc.repositories;

import lombok.extern.apachecommons.CommonsLog;
import org.assertj.core.util.Lists;
import org.excellent.cancer.experimental.application.config.ExcellentCancerProperties;
import org.excellent.cancer.experimental.application.config.OwnerProperties;
import org.excellent.cancer.experimental.application.jdbc.entities.Owner;
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
public class JdbcRepositoriesTest {

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

}
