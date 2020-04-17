package excellent.cancer.gray.light.component;

import excellent.cancer.gray.light.jdbc.entities.OwnerProject;
import excellent.cancer.gray.light.service.SuperOwnerService;
import lombok.extern.apachecommons.CommonsLog;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用于测试的随机生成实体服务
 *
 * @author XyParaCrim
 */
@Service
@CommonsLog
public class SuperOwnerRandomService {

    private final SuperOwnerService superOwnerService;

    @Autowired
    public SuperOwnerRandomService(SuperOwnerService superOwnerService) {
        this.superOwnerService = superOwnerService;
    }

    /**
     * 随机生成一个项目
     *
     * @return 返回随机生成一个项目
     */
    public OwnerProject createOwnerProjectOptionally() {
        return OwnerProject.
                builder().
                ownerId(superOwnerService.superOwner().getId()).
                name(RandomString.make(5)).
                description(RandomString.make(12)).
                build();
    }

    /**
     * 随机保存一个项目并返回
     *
     * @return 随机保存一个项目并返回
     */
    @Transactional
    public OwnerProject saveOwnerProjectOptionally() {
        OwnerProject ownerProject = createOwnerProjectOptionally();
        log.info("进行新建项目：" + ownerProject);

        if (superOwnerService.addProject(ownerProject)) {
            log.info("已存项目：" + ownerProject);
        } else {
            log.error("项目无法保存");
        }

        return ownerProject;
    }

}
