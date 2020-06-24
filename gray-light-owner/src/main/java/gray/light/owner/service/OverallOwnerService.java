package gray.light.owner.service;

import gray.light.definition.entity.Scope;
import gray.light.owner.entity.Owner;
import gray.light.owner.entity.OwnerProject;
import gray.light.owner.repository.OwnerProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import perishing.constraint.jdbc.Page;

import java.util.List;
import java.util.Optional;

/**
 * 定义对于所属者的相关服务，例如：项目，介绍等等
 *
 * @author XyParaCrim
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OverallOwnerService {

    private final OwnerProjectRepository ownerProjectRepository;

    /**
     * 返回指定所属者的所有项目
     *
     * @param owner 指定所属者
     * @param page 分页
     * @return 返回指定所属者的所有项目
     */
    public List<OwnerProject> projects(Owner owner, Page page) {
        return ownerProjectRepository.findByOwnerId(owner.getId(), page);
    }

    /**
     * 返回指定所属者的程序项目
     *
     * @param owner 指定所属者
     * @param page 分页
     * @return 返回指定所属者的程序项目
     */
    public List<OwnerProject> programmingProjects(Owner owner, Page page) {
        return projects(owner, Scope.CODING, page);
    }

    /**
     * 返回指定所属者的文档项目
     *
     * @param owner 指定所属者
     * @param page 分页
     * @return 返回指定所属者的文档项目
     */
    public List<OwnerProject> documentProjects(Owner owner, Page page) {
        return projects(owner, Scope.DOCUMENT, page);
    }

    /**
     * 返回指定所属者的笔记项目
     *
     * @param owner 指定所属者
     * @param page 分页
     * @return 返回指定所属者的笔记项目
     */
    public List<OwnerProject> noteProjects(Owner owner, Page page) {
        return projects(owner, Scope.NOTE, page);
    }

    /**
     * 查询指定所属者项目中是否有ID匹配的项目
     *
     * @param owner 指定所属者
     * @param project 请求项目
     * @return 返回指定所属者项目中ID匹配的项目
     */
    public Optional<OwnerProject> project(Owner owner, OwnerProject project) {
        return ownerProjectRepository.findByOwnerIdAndId(owner.getId(), project.getId());
    }

    /**
     * 查询指定所属者项目中是否有ID匹配的项目
     *
     * @param owner 指定所属者
     * @param projectId project 请求项目ID
     * @return 返回指定所属者项目中是否有ID匹配的项目
     */
    public boolean existsProject(Owner owner, Long projectId) {
        return ownerProjectRepository.existsByIdAndOwnerId(projectId, owner.getId());
    }

    public boolean addProject(Owner owner, OwnerProject project) {
        project.setOwnerId(owner.getId());
        return ownerProjectRepository.save(project);
    }

    public boolean removeProject(Owner owner, OwnerProject project) {
        project.setOwnerId(owner.getId());
        return ownerProjectRepository.delete(project);
    }

    // TODO

    private List<OwnerProject> projects(Owner owner, Scope scope, Page page) {
        // TODO
        throw new RuntimeException();
    }

}
