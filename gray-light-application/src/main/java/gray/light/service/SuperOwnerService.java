package gray.light.service;

import gray.light.error.UniqueOwnerException;
import gray.light.document.entity.Document;
import gray.light.owner.entity.Owner;
import gray.light.owner.entity.OwnerProject;
import gray.light.document.repository.DocumentRepository;
import gray.light.owner.repository.OwnerProjectRepository;
import gray.light.owner.repository.OwnerRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import perishing.constraint.jdbc.Page;

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

    private final DocumentRepository documentRepository;

    /**
     * 不仅在构造函数中设置必要的参数，而且会查询出唯一的owner，并且将其设置到全局变量中
     *
     * @param repositoryService 仓库服务
     * @throws UniqueOwnerException 如果不能够成功初始化唯一owner
     */
    @Autowired
    public SuperOwnerService(RepositoryService repositoryService) {
        this.ownerRepository = repositoryService.owner();
        this.projectRepository = repositoryService.ownerProject();
        this.documentRepository = repositoryService.document();

        // 初始化唯一超级所属者
        this.owner = UniqueOwnerException.extractOwnerRequireUniqueOwner(asList(ownerRepository.findAll()));
        GlobalFinalVariables.set(Owner.class, this.owner);

        List<Document> documents = repositoryService.document().findAll();
        documents.forEach(log::error);
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
     * 返回超级所属者的所有文档
     *
     * @return 返回超级所属者的所有文档
     */
    public List<Document> documents() {
        return documentRepository.findAllByOwnerId(owner.getId(), null);
    }

    /**
     * 返回指定数量超级所属者文档
     *
     * @param page 分页
     * @return 返回超级所属者的所有文档
     */
    public List<Document> documents(Page page) {
        return documentRepository.findAllByOwnerId(owner.getId(), page);
    }

}
