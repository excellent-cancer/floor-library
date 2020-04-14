package excellent.cancer.gray.light.component;

import excellent.cancer.gray.light.jdbc.entities.OwnerProject;
import excellent.cancer.gray.light.service.UniqueOwnerService;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

/**
 * 用于测试的随机生成实体服务
 *
 * @author XyParaCrim
 */
@Service
public class UniqueOwnerRandomService {

    private final UniqueOwnerService uniqueOwnerService;

    @Autowired
    public UniqueOwnerRandomService(UniqueOwnerService uniqueOwnerService) {
        this.uniqueOwnerService = uniqueOwnerService;
    }

    /**
     * 随机生成一个项目
     *
     * @return 返回随机生成一个项目
     */
    public OwnerProject createOwnerProjectOptionally() {
        Date current = new Date();
        return OwnerProject.builder().
                ownerId(uniqueOwnerService.getOwner().getId()).
                name(RandomString.make(5)).
                description(RandomString.make(12)).
                docs(Collections.emptySet()).
                createdDate(current).
                updatedDate(current).
                build();
    }

}
