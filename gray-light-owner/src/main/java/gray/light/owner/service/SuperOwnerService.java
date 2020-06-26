package gray.light.owner.service;

import gray.light.owner.entity.Privilege;
import gray.light.support.component.GlobalFinalVariables;
import gray.light.owner.entity.Owner;
import gray.light.owner.error.UniqueOwnerException;
import gray.light.owner.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;
import perishing.constraint.jdbc.Page;

import javax.annotation.PostConstruct;

import java.util.List;

import static perishing.constraint.treasure.chest.CollectionsTreasureChest.asList;

/**
 * 定义对于超级所属者的相关服务，例如：项目，介绍等等
 *
 * @author XyParaCrim
 */
@Service
@CommonsLog
@RequiredArgsConstructor
public final class SuperOwnerService {

    private final OwnerRepository ownerRepository;

    /**
     * 不仅在构造函数中设置必要的参数，而且会查询出唯一的owner，并且将其设置到全局变量中
     *
     * @throws UniqueOwnerException 如果不能够成功初始化唯一owner
     */
    @PostConstruct
    public void validateSuperOwner() {
        // 初始化唯一超级所属者
        List<Owner> superOwners = asList(ownerRepository.findBySuperPrivilege(Privilege.Y, Page.unlimited().nullable()));
        Owner owner = UniqueOwnerException.extractOwnerRequireUniqueOwner(superOwners);
        GlobalFinalVariables.set(Owner.class, owner);
    }

    /**
     *  获取超级所属者
     *
     * @return 获取超级所属者
     */
    public Owner superOwner() {
        return GlobalFinalVariables.get(Owner.class);
    }

}
