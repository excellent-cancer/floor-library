package org.excellent.cancer.experimental.application.jdbc.repositories;

import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@CommonsLog
@SpringBootTest("excellent.cancer.jdbc.enabled=true")
public class JdbcRepositoriesTest {

    @Autowired
    private OwnerRepository ownerRepository;

    @Test
    public void connected() {
        Assertions.assertEquals(1, ownerRepository.count());
    }

}
