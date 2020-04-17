package excellent.cancer.gray.light.service;

import excellent.cancer.gray.light.error.UniqueOwnerException;
import excellent.cancer.gray.light.jdbc.entities.Owner;
import excellent.cancer.gray.light.jdbc.entities.OwnerProject;
import excellent.cancer.gray.light.jdbc.repositories.OwnerProjectRepository;
import excellent.cancer.gray.light.jdbc.repositories.OwnerRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static perishing.constraint.treasure.chest.CollectionsTreasureChest.asList;

/**
 * 定义对于超级所属者的相关服务，例如：项目，介绍等等
 *
 * @author XyParaCrim
 */
@Service
@CommonsLog
public final class SuperOwnerService {

    private final Owner owner;

    private final OwnerRepository ownerRepository;

    private final OwnerProjectRepository projectRepository;

    /**
     * 不仅在构造函数中设置必要的参数，而且会查询出唯一的owner，并且将其设置到全局变量中
     *
     * @param ownerRepository   owner jdbc repository
     * @param projectRepository project jdbc repository
     * @throws UniqueOwnerException 如果不能够成功初始化唯一owner
     */
    @Autowired
    public SuperOwnerService(OwnerRepository ownerRepository, OwnerProjectRepository projectRepository) {
        this.ownerRepository = ownerRepository;
        this.projectRepository = projectRepository;

        // 初始化唯一超级所属者
        this.owner = UniqueOwnerException.extractOwnerRequireUniqueOwner(asList(ownerRepository.findAll()));
        GlobalFinalVariables.set(Owner.class, this.owner);
    }

    public Owner superOwner() {
        return owner;
    }

    /**
     * 返回超级所属者的所有项目
     *
     * @return 返回超级所属者的所有项目
     */
    public List<OwnerProject> projects() {
        return projectRepository.findByOwnerId(owner.getId());
    }

    /**
     * 查询超级所属者项目中是否有ID匹配的项目
     *
     * @param project 请求项目
     * @return 返回超级所属者项目中ID匹配的项目
     */
    public Optional<OwnerProject> project(OwnerProject project) {
        return projectRepository.findByOwnerIdAndId(owner.getId(), project.getId());
    }

    /**
     * 查询超级所属者项目中是否有ID匹配的项目
     *
     * @param projectId project 请求项目ID
     * @return 返回超级所属者项目中是否有ID匹配的项目
     */
    public boolean existsProject(Long projectId) {
        return projectRepository.existsByIdAndOwnerId(projectId, owner.getId());
    }

    public boolean addProject(OwnerProject project) {
        project.setOwnerId(owner.getId());
        return projectRepository.save(project);
    }

    public boolean removeProject(OwnerProject project) {
        project.setOwnerId(owner.getId());
        return projectRepository.delete(project);
    }

    /**
     * 根据Id查询项目，只在查询到匹配项目时发布订阅
     *
     * @param project 请求项目
     * @return publisher of OwnerProject which will publish on matched
     */
/*    public Mono<OwnerProject> matchedProject(OwnerProject project) {
        return project(project).flatMap(flatMapperIfPresent(project));
    }*/

}
